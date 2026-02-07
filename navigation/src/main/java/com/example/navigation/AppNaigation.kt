package com.example.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.authorization.presentation.AuthMainScreen
import com.example.authorization.presentation.AuthScreenViewModel
import com.example.registration.presentation.RegisterMainScreen
import com.example.registration.presentation.RegisterScreenViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthScreenViewModel,
    registerScreenViewModel: RegisterScreenViewModel
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavigationScreens.AUTHORIZATION.routeName
    ) {
        composable(NavigationScreens.AUTHORIZATION.routeName) {
            AuthMainScreen(authViewModel){
                navController.navigate(NavigationScreens.REGISTER.routeName)
            }
        }

        composable(NavigationScreens.REGISTER.routeName) {
            RegisterMainScreen(registerScreenViewModel) {
                navController.navigate(NavigationScreens.MAIN.routeName)
            }
        }

        composable(NavigationScreens.MAIN.routeName) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "success"
                )
            }
        }

    }
}