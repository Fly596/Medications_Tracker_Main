package com.galeria.medicationstracker.ui

import androidx.lifecycle.ViewModel
import com.galeria.medicationstracker.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

// TODO: What is this?
@HiltViewModel
class HeadViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _selectedItemIndex = MutableStateFlow(0)
    val selectedItemIndex = _selectedItemIndex.asStateFlow()

    fun updateSelectedItemIndex(index: Int) {
        _selectedItemIndex.value = index
    }
}
