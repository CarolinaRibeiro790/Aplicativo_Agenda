package com.example.appagenda.view.telas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}