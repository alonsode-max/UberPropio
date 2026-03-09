package com.example.easy_send

import androidx.activity.compose.setContent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.easy_send.navigation.AppNavigation
import com.example.easy_send.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController= rememberNavController()

                val authVm: AuthViewModel = hiltViewModel()

                AppNavigation(
                    navController = navController,
                    authVm        = authVm
                )
            }
        }
    }
}
