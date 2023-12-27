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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.savent.inventory.presentation.viewmodel.LoginViewModel
import com.savent.inventory.toast
import com.savent.inventory.ui.screen.login.LoginScreen
import com.savent.inventory.ui.theme.SaventInventarioTheme
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SaventInventarioTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val loginViewModel by viewModel<LoginViewModel>()
                    val state by loginViewModel.state.collectAsState()
                    LoginScreen(state = state, onEvent = loginViewModel::onEvent)
                    LaunchedEffect(Unit) {
                        loginViewModel.isLogged.observe(this@LoginActivity) {
                            if (!it)return@observe
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                        loginViewModel.message.collectLatest { message ->
                            this@LoginActivity.toast(message)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting3(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    SaventInventarioTheme {
        Greeting3("Android")
    }
}