package com.savent.inventory.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.savent.inventory.ui.theme.SaventInventarioTheme
import com.savent.inventory.presentation.viewmodel.SplashViewModel
import com.savent.inventory.ui.screen.splash.SplashScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SaventInventarioTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val splashViewModel by viewModel<SplashViewModel>()
                    SplashScreen { splashViewModel.checkLogin() }
                    splashViewModel.isLogged.observe(this) {
                        if (it == null) return@observe
                        if (it)
                            startActivity(Intent(this, MainActivity::class.java))
                        else
                            startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    SaventInventarioTheme {
        Greeting2("Android")
    }
}