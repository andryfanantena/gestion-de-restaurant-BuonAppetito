package com.teamsasa.buonappetito.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamsasa.buonappetito.data.model.Dish
import com.teamsasa.buonappetito.data.repository.MenuRepository
import kotlinx.coroutines.launch
import kotlinx.flow.MutableStateFlow
import kotlinx.flow.StateFlow
import kotlinx.flow.asStateFlow

class MenuViewModel(private val repository: MenuRepository) : ViewModel() {

    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> = _dishes.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    init {
        loadMenuData()
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
        loadMenuData()
    }

    private fun loadMenuData() {
        viewModelScope.launch {
            val result = if (_selectedCategory.value == "All") {
                repository.getPopularDishes()
            } else {
                repository.getAllDishes(_selectedCategory.value)
            }

            result.onSuccess { list ->
                _dishes.value = list
            }.onFailure {
                // En cas de déconnexion, simulation avec des données locales de démo en Ariary (Ar)
                _dishes.value = listOf(
                    Dish(1, "Le Signature Burger", "Bœuf Wagyu, cheddar affiné, bacon croustillant, sauce...", 35000.0, "", "Burgers"),
                    Dish(2, "Margherita Classica", "Sauce tomate San Marzano, mozzarella di bufala...", 24000.0, "", "Pizza")
                )
            }
        }
    }
}
