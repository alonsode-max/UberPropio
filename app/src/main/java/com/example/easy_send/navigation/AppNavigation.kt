package com.example.easy_send.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.easy_send.ui.*
import com.example.easy_send.viewmodel.AuthViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    authVm:        AuthViewModel
) {

    //val startRoute = if (authVm.isLoggedIn) Routes.HOME else Routes.ONBOARD

    NavHost(navController = navController, startDestination = Routes.MAP) {

        composable(route = Routes.ONBOARD) {
            OnboardScreen(
                onNext={ navController.navigate(Routes.LOGIN) }
            )
        }

        composable(route = Routes.MAP) {
            MapScreen(
                onBack = { navController.navigate(Routes.MAP) }
            )
        }

        composable(route = Routes.EMAIL) {
            EmailLoginScreen(
                authVm   = authVm,
                onLoginOk = {
                    navController.navigate(Routes.SAFETY)
                },
                onRegister = {
                    navController.navigate(Routes.SAFETY)
                }
            )
        }

        composable(route = Routes.LOGIN) {
            LoginScreen(
                authVm   = authVm,
                onLoginOk = {
                    navController.navigate(Routes.SAFETY)
                },
                onNext={
                    navController.navigate(Routes.CODE)
                }
            )
        }

        composable(route = Routes.CODE) {
            CodeScreen(
                authVm=authVm,
                onLoginOk = {
                    navController.navigate(Routes.SAFETY) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onEmail={
                    navController.navigate(Routes.EMAIL)
                },
                onBack = {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }

        composable(route = Routes.SAFETY) {
            SafetyScreen(
                //Falta onGuidelines
                onBack={ navController.navigate(Routes.LOGIN) },
                onNext={ navController.navigate(Routes.HOME) }
            )
        }

        composable(route = Routes.REGISTER) {
            RegisterScreen(
                authVm=authVm,
                onRegisterOk={ navController.navigate(Routes.SAFETY) },
                onLogin={ navController.navigate(Routes.EMAIL) }
            )
        }

        composable(route = Routes.HOME){
            HomeScreen(
                //Falta onFav
                onMap={ navController.navigate(Routes.MAP) }
            )
        }
    }
}