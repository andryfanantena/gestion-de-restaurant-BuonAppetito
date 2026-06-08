package com.teamsasa.buonappetito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.teamsasa.buonappetito.ui.components.EpicureanBottomNavigation
import com.teamsasa.buonappetito.ui.auth.LoginScreen
import com.teamsasa.buonappetito.ui.auth.RegisterScreen
import com.teamsasa.buonappetito.ui.menu.DishDetailScreen
import com.teamsasa.buonappetito.ui.menu.HomeScreen
import com.teamsasa.buonappetito.ui.menu.MenuScreen
import com.teamsasa.buonappetito.ui.cart.CartScreen
import com.teamsasa.buonappetito.ui.profile.ProfileScreen
import com.teamsasa.buonappetito.ui.order.OrderTrackingScreen
import com.teamsasa.buonappetito.ui.theme.EpicureanTheme
import com.teamsasa.buonappetito.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EpicureanTheme {
                val navController = rememberNavController()
                
                // Note: In a real app, you'd use a DI framework or a Factory to provide these ViewModels
                // because they have constructor parameters.
                val authViewModel: AuthViewModel = viewModel()
                val menuViewModel: MenuViewModel = viewModel()
                val cartViewModel: CartViewModel = viewModel()
                val orderViewModel: OrderViewModel = viewModel()

                var currentRoute by remember { mutableStateOf("home") }

                Scaffold(
                    bottomBar = {
                        val noBottomBarRoutes = listOf("login", "register")
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
                        startDestination = "login",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("login") {
                            currentRoute = "login"
                            LoginScreen(
                                viewModel = authViewModel,
                                onLoginSuccess = { navController.navigate("home") },
                                onNavigateToRegister = { navController.navigate("register") }
                            )
                        }
                        composable("register") {
                            currentRoute = "register"
                            RegisterScreen(
                                viewModel = authViewModel,
                                onRegisterSuccess = { navController.navigate("home") },
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
                                onCheckoutClick = {
                                    // Simulation de commande passée
                                    navController.navigate("track/1")
                                }
                            )
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
                                }
                            )
                        }
                        composable(
                            route = "detail/{dishId}",
                            arguments = listOf(navArgument("dishId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val dishId = backStackEntry.arguments?.getLong("dishId")
                            val dish = menuViewModel.dishes.value.find { it.id == dishId }
                            dish?.let {
                                DishDetailScreen(dish = it, cartViewModel = cartViewModel, onBack = { navController.popBackStack() })
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
