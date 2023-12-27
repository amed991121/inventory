package com.savent.inventory.ui.activity

import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.savent.inventory.AppConstants
import com.savent.inventory.ui.theme.SaventInventarioTheme
import com.savent.inventory.presentation.viewmodel.InventoryViewModel
import com.savent.inventory.presentation.viewmodel.ProductsViewModel
import com.savent.inventory.toast
import com.savent.inventory.ui.screen.Screen
import com.savent.inventory.ui.screen.inventory.InventoryScreen
import com.savent.inventory.ui.screen.onNavEvent
import com.savent.inventory.ui.screen.products.ProductsScreen
import com.savent.inventory.R
import com.savent.inventory.ui.screen.products.ActionDialog
import com.savent.inventory.ui.screen.products.DialogState
import com.savent.inventory.ui.screen.products.ProductsEvent
import com.savent.inventory.utils.DeviceVibration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val productsViewModel by viewModel<ProductsViewModel>()
    lateinit var query: MutableState<String>
    var launchAddProductDialog: () -> Unit = {  }

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SaventInventarioTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = Screen.Products.route) {
                        composable(Screen.Products.route) {
                            val state by productsViewModel.state.collectAsState()
                            val queryHint = stringResource(id = R.string.search)
                            query = remember { mutableStateOf(queryHint) }

                            val pullRefreshState = rememberPullRefreshState(
                                refreshing = state.isRefreshing,
                                onRefresh = productsViewModel::refreshData
                            )

                            val sheetState = rememberModalBottomSheetState(
                                initialValue = ModalBottomSheetValue.Hidden
                            )
                            val scope = rememberCoroutineScope()

                            var actionDialog by remember { mutableStateOf(ActionDialog.AddProductEntry) }

                            launchAddProductDialog = {
                                scope.launch {
                                    actionDialog = ActionDialog.AddProductEntry
                                    sheetState.show()
                                }
                            }
                            ProductsScreen(
                                state = state,
                                query = query.value,
                                onQueryChange = { query.value = it },
                                onEvent = productsViewModel::onEvent,
                                navEvent = { navEvent ->
                                    onNavEvent(navController, navEvent)
                                },
                                pullRefreshState = pullRefreshState,
                                onCloseSession = {
                                    try {
                                        (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
                                            .clearApplicationUserData()
                                        startActivity(
                                            Intent(
                                                this@MainActivity,
                                                LoginActivity::class.java
                                            )
                                        )
                                    } catch (e: Exception) {
                                        startActivity(
                                            Intent(
                                                this@MainActivity,
                                                LoginActivity::class.java
                                            )
                                        )
                                        finish()
                                    }

                                },
                                sheetState = sheetState,
                                onChangeDialogState = { dialogState ->
                                    when (dialogState) {
                                        DialogState.Hide -> scope.launch { sheetState.hide() }
                                        is DialogState.Show -> scope.launch {
                                            actionDialog = dialogState.type as ActionDialog
                                            sheetState.show()
                                        }
                                    }
                                },
                                actionDialog = actionDialog
                            )
                            LaunchedEffect(Unit) {
                                productsViewModel.message.flowWithLifecycle(
                                    lifecycle,
                                    Lifecycle.State.STARTED
                                ).collectLatest { message ->
                                    toast(message)
                                }
                            }
                        }

                        composable(Screen.Inventory.route) {
                            val inventoryViewModel by viewModel<InventoryViewModel>()
                            val state by inventoryViewModel.state.collectAsState()

                            val pullRefreshState = rememberPullRefreshState(
                                refreshing = state.isRefreshing,
                                onRefresh = inventoryViewModel::refreshData
                            )

                            InventoryScreen(
                                state = state,
                                onEvent = inventoryViewModel::onEvent,
                                navEvent = { navEvent ->
                                    onNavEvent(navController, navEvent)
                                },
                                pullRefreshState = pullRefreshState
                            )

                            LaunchedEffect(Unit) {
                                inventoryViewModel.message.flowWithLifecycle(
                                    lifecycle,
                                    Lifecycle.State.STARTED
                                ).collectLatest { message ->
                                    toast(message)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstants.CODE_SCANNER && resultCode == Activity.RESULT_OK) {
            val barcode = data?.getStringExtra(AppConstants.RESULT_CODE_SCANNER) ?: ""
            productsViewModel.getProductByBarcode(barcode) { product ->
                if (product != null) {
                    query.value = barcode
                    productsViewModel.onEvent(ProductsEvent.SearchProducts(barcode))
                    productsViewModel.onEvent(ProductsEvent.SelectProduct(product))
                    launchAddProductDialog()
                    return@getProductByBarcode
                }
                DeviceVibration.execute(this)
            }
        }
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SaventInventarioTheme {
        Greeting("Android")
    }
}