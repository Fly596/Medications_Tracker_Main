package com.galeria.medicationstracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

@Composable
fun NavKey.navigationState(): NavigationState {
  return NavigationState(backStacks = rememberNavBackStack(this))
}

class NavigationState(val backStacks: NavBackStack<NavKey>) {

  val stacksInUse: NavBackStack<NavKey>
    get() = backStacks
}
