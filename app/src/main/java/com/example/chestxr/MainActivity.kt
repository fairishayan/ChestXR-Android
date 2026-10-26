package com.example.chestxr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.chestxr.ui.MainScreen
import com.example.chestxr.ui.theme.ChestXRTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChestXRTheme {
                MainScreen()
            }
        }
    }
}