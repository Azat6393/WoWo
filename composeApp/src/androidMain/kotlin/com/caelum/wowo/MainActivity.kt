package com.caelum.wowo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import mobile_ui.MobileApp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MobileApp()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    MobileApp()
}