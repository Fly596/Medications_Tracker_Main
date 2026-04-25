package com.galeria.medtracker2.core.common

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    // Список запрашиваемых разрешений.
    var visiblePermissionsDialogQueue = mutableListOf<String>()

    fun dismissDialog() {
        visiblePermissionsDialogQueue.removeLast()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted) {
            visiblePermissionsDialogQueue.add(0, permission)
        }
    }
}