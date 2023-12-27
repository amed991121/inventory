package com.savent.inventory.ui.screen

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.savent.inventory.AppConstants
import com.savent.inventory.ui.activity.BarCodeScannerActivity
import com.savent.inventory.utils.CheckPermissions


sealed class NavEvent() {
    object Back : NavEvent()
    object Inventory : NavEvent()
    object BarCodeScanner : NavEvent()
}

fun Activity.onNavEvent(navController: NavController, navEvent: NavEvent) {
    when (navEvent) {
        NavEvent.Back -> {
            if (navController.currentDestination?.route == Screen.Products.route) {
                finish()
                return
            }
            navController.navigate(Screen.Products.route) {
                navController.graph.startDestinationRoute?.let { route ->
                    popUpTo(route) {
                        saveState = true
                    }
                }
                launchSingleTop = true
                restoreState = true
            }
        }
        NavEvent.Inventory -> navController.navigate(Screen.Inventory.route) {
            navController.graph.startDestinationRoute?.let { route ->
                popUpTo(route) {
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }
        NavEvent.BarCodeScanner ->{
            if (!CheckPermissions.check(this, arrayOf(AppConstants.CAMERA_PERMISSION))) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(AppConstants.CAMERA_PERMISSION),
                    AppConstants.REQUEST_CODE_CAMERA
                )
                return
            }
            val intent = Intent(this, BarCodeScannerActivity::class.java)
            startActivityForResult(intent, AppConstants.CODE_SCANNER)
        }

    }
}