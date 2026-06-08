package com.teamsasa.buonappetito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.teamsasa.buonappetito.data.local.SessionManager
import com.teamsasa.buonappetito.ui.components.EpicureanBottomNavigation
import com.teamsasa.buonappetito.ui.auth.LoginScreen
import com.teamsasa.buonappetito.ui.auth.RegisterScreen
import com.teamsasa.buonappetito.ui.menu.DishDetailScreen
import com.teamsasa.buonappetito.ui.menu.HomeScreen
import com.teamsasa.buonappetito.ui.menu.MenuScreen
import com.teamsasa.buonappetito.ui.cart.CartScreen
import com.teamsasa.buonappetito.ui.profile.ProfileScreen
import com.teamsasa.buonappetito.ui.order.OrderTrackingScreen
import com.teamsasa.buonappetito.ui.order.OrderHistoryScreen
import com.teamsasa.buonappetito.ui.order.QrScannerScreen
import com.teamsasa.buonappetito.ui.theme.EpicureanTheme
import com.teamsasa.buonappetito.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EpicureanTheme {
                val context = LocalContext.current
                val sessionManager = remember { SessionManager(context) }
                val navController = rememberNavController()
                val factory = remember { ViewModelFactory(sessionManager) }
                
                val authViewModel: AuthViewModel = viewModel(factory = factory)
                val menuViewModel: MenuViewModel = viewModel(factory = factory)
                val cartViewModel: CartViewModel = viewModel(factory = factory)
                val orderViewModel: OrderViewModel = viewModel(factory = factory)

                var currentRoute by remember { mutableStateOf("home") }

                Scaffold(
                    bottomBar = {
                        val noBottomBarRoutes = listOf("login", "register", "scanner")
                        if (currentRoute !in noBottomBarRoutes) {
                            EpicureanBottomNavigation(
                                currentScreen = currentRoute,
                                onScreenSelected = { route ->
                                    currentRoute = route
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = if (sessionManager.fetchAuthToken() != null) "home" else "login",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("login") {
                            currentRoute = "login"
                            LoginScreen(
                                viewModel = authViewModel,
                                onLoginSuccess = { 
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = { navController.navigate("register") }
                            )
                        }
                        composable("register") {
                            currentRoute = "register"
                            RegisterScreen(
                                viewModel = authViewModel,
                                onRegisterSuccess = { 
                                    navController.navigate("home") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                },
                                onNavigateToLogin = { navController.navigate("login") }
                            )
                        }
                        composable("home") {
                            currentRoute = "home"
                            HomeScreen(
                                viewModel = menuViewModel,
                                authViewModel = authViewModel,
                                onNavigateToDetail = { dishId -> navController.navigate("detail/$dishId") }
                            )
                        }
                        composable("menu") {
                            currentRoute = "menu"
                            MenuScreen(
                                viewModel = menuViewModel,
                                onDishClick = { dishId -> navController.navigate("detail/$dishId") }
                            )
                        }
                        composable("cart") {
                            currentRoute = "cart"
                            CartScreen(
                                viewModel = cartViewModel,
                                onCheckoutClick = { checkoutRequest ->
                                    orderViewModel.checkout(checkoutRequest) { orderId ->
                                        navController.navigate("track/$orderId")
                                        cartViewModel.clearCart()
                                    }
                                },
                                onNavigateToScanner = { navController.navigate("scanner") }
                            )
                        }
                        composable("scanner") {
                            currentRoute = "scanner"
                            QrScannerScreen(onQrCodeScanned = { tableInfo ->
                                cartViewModel.setTableNumber(tableInfo)
                                navController.popBackStack()
                            })
                        }
                        composable("profile") {
                            currentRoute = "profile"
                            ProfileScreen(
                                authViewModel = authViewModel,
                                onLogout = {
                                    authViewModel.logout {
                                        navController.navigate("login") {
                                            popUpTo(0)
                                        }
                                    }
                                },
                                onNavigateToHistory = { navController.navigate("history") }
                            )
                        }
                        composable("history") {
                            currentRoute = "profile"
                            LaunchedEffect(Unit) {
                                orderViewModel.loadOrderHistory()
                            }
                            OrderHistoryScreen(viewModel = orderViewModel)
                        }
                        composable(
                            route = "detail/{dishId}",
                            arguments = listOf(navArgument("dishId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val dishId = backStackEntry.arguments?.getLong("dishId") ?: 0L
                            val dishes by menuViewModel.dishes.collectAsStateWithLifecycle()
                            val dish = remember(dishes, dishId) { dishes.find { it.id == dishId } }
                            
                            if (dish != null) {
                                DishDetailScreen(
                                    dish = dish, 
                                    cartViewModel = cartViewModel, 
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                        composable(
                            route = "track/{orderId}",
                            arguments = listOf(navArgument("orderId") { type = NavType.LongType })
                        ) { entry ->
                            val orderId = entry.arguments?.getLong("orderId") ?: 0L
                            OrderTrackingScreen(orderId = orderId, viewModel = orderViewModel)
                        }
                    }
                }
            }
        }
    }
}
