package com.savent.inventory.ui.screen.login

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Credentials
import com.savent.inventory.ui.component.CustomTextField
import com.savent.inventory.ui.screen.ListScreenEvent
import com.savent.inventory.ui.screen.login.companies.CompanyListScreen
import com.savent.inventory.ui.screen.login.stores.StoreListScreen
import com.savent.inventory.ui.theme.DarkGray
import com.savent.inventory.ui.theme.LightGray
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalMaterialApi::class
)
@Composable
fun LoginScreen(state: LoginState, onEvent: (LoginEvent) -> Unit) {
    var animationVisibility by rememberSaveable {
        mutableStateOf(false)
    }


    val inventoryComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.animation4)
    )
    val inventoryProgress by animateLottieCompositionAsState(
        composition = inventoryComposition,
        restartOnPlay = true,
        iterations = Int.MAX_VALUE
    )

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()
    var dialog by rememberSaveable { mutableStateOf(Dialog.Companies) }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetBackgroundColor = Color.White,
        sheetContent = {
            if (dialog == Dialog.Companies) {
                CompanyListScreen(
                    companies = state.companies, onEvent = { event ->
                        when (event) {
                            ListScreenEvent.Close ->
                                scope.launch {
                                    modalSheetState.hide()
                                }
                            is ListScreenEvent.Search -> {
                                onEvent(LoginEvent.SearchCompany(event.query))
                            }
                            is ListScreenEvent.Select -> {
                                onEvent(LoginEvent.SelectCompany(event.id))
                            }
                        }
                    }
                )
                return@ModalBottomSheetLayout
            }

            StoreListScreen(stores = state.stores, onEvent = { event ->
                when (event) {
                    ListScreenEvent.Close -> scope.launch { modalSheetState.hide() }
                    is ListScreenEvent.Search -> onEvent(LoginEvent.SearchStore(event.query))
                    is ListScreenEvent.Select -> onEvent(LoginEvent.SelectStore(event.id))
                }
            })
        },
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Column(
                    modifier = Modifier
                        .requiredHeight(420.dp)
                        .fillMaxWidth(0.8f)
                )
                {
                    AnimatedVisibility(visible = animationVisibility, enter = scaleIn()) {
                        LottieAnimation(
                            composition = inventoryComposition,
                            progress = inventoryProgress,
                            modifier = Modifier
                                .fillMaxSize()

                        )
                    }

                }

                animationVisibility = true

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.wrapContentSize()
                ) {
                    val shape = RoundedCornerShape(13.dp)
                    val fieldModifier = Modifier
                        .requiredHeight(50.dp)
                        .fillMaxWidth(0.8f)
                        .border(
                            border = BorderStroke(2.dp, Color.Gray.copy(alpha = 0.0f)),
                            shape = shape
                        )
                        .clip(shape)
                        .background(LightGray.copy(alpha = 1f))
                    val textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.comforta_medium)),
                        fontSize = 19.sp
                    )

                    val rfcHint = stringResource(id = R.string.rfc_hint)
                    var rfc by rememberSaveable { mutableStateOf(rfcHint) }
                    CustomTextField(
                        value = rfc,
                        onValueChange = {
                            rfc = it.changeValueBehavior(hint = rfcHint, text = rfc, maxLength = 13)
                            Log.d("log_","str--$it//$rfc")
                        },
                        modifier = fieldModifier,
                        textStyle = if (rfc == rfcHint) textStyle.copy(
                            color = DarkGray.copy(alpha = 0.4f),
                        ) else textStyle,
                        leadingIcon = {
                            Row() {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_user),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(top = 6.dp, bottom = 6.dp),
                                    tint = DarkGray.copy(alpha = 0.5f)
                                )
                            }

                        },
                        trailingIcon = {
                            AnimatedVisibility(
                                visible = rfc != rfcHint,
                                enter = scaleIn(),
                                exit = scaleOut()
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .padding(end = 10.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }) {
                                            rfc = rfcHint
                                        },
                                    tint = DarkGray.copy(alpha = 0.5f)
                                )
                            }
                        },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    val pinHint = stringResource(id = R.string.pin_hint)
                    var pin by rememberSaveable { mutableStateOf(pinHint) }
                    CustomTextField(
                        value = pin,
                        onValueChange = {
                            pin = it.changeValueBehavior(hint = pinHint, text = pin)
                        },
                        modifier = fieldModifier,
                        textStyle = if (pin == pinHint) textStyle.copy(
                            color = DarkGray.copy(alpha = 0.4f)
                        ) else textStyle,
                        leadingIcon = {
                            Row() {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_lock),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .padding(top = 5.dp, bottom = 5.dp),
                                    tint = DarkGray.copy(alpha = 0.5f)
                                )
                            }

                        },
                        trailingIcon = {
                            AnimatedVisibility(
                                visible = pin != pinHint,
                                enter = scaleIn(),
                                exit = scaleOut()
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .padding(end = 10.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }) {
                                            pin = pinHint
                                        },
                                    tint = DarkGray.copy(alpha = 0.5f)
                                )
                            }
                        },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                    val companyHint = stringResource(id = R.string.company)
                    val company =
                        state.selectedCompany.ifEmpty { companyHint }
                    Box(modifier = fieldModifier.clickable {
                        dialog = Dialog.Companies
                        scope.launch {
                            if (!modalSheetState.isVisible) {
                                modalSheetState.show()
                            }
                        }
                        onEvent(LoginEvent.ReloadCompanies)
                    }) {
                        Row(
                            verticalAlignment = CenterVertically,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_company),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(
                                        start = 5.dp,
                                        bottom = 5.dp,
                                        top = 5.dp,
                                        end = 4.dp
                                    ),
                                tint = DarkGray.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = company,
                                style = if (company == companyHint) textStyle.copy(
                                    color = DarkGray.copy(alpha = 0.4f)
                                ) else textStyle
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(15.dp))
                    val storeHint = stringResource(id = R.string.store)
                    val store =
                        state.selectedStore.ifEmpty { storeHint }

                    Box(modifier = fieldModifier.clickable {
                        dialog = Dialog.Stores
                        scope.launch {
                            if (!modalSheetState.isVisible) {
                                modalSheetState.show()
                            }
                        }
                        onEvent(LoginEvent.ReloadStores)
                    }) {
                        Row(
                            verticalAlignment = CenterVertically,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_store),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(
                                        start = 8.dp,
                                        bottom = 4.dp,
                                        top = 4.dp,
                                        end = 5.dp
                                    ),
                                tint = DarkGray.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = store,
                                style = if (store == storeHint) textStyle.copy(
                                    color = DarkGray.copy(alpha = 0.4f)
                                ) else textStyle
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .requiredHeight(50.dp)
                            .fillMaxWidth(0.7f)
                            .clip(shape)
                            .background(Color.Black)
                            .clickable {
                                if (state.isLoading) return@clickable
                                onEvent(
                                    LoginEvent.Login(
                                        Credentials(
                                            rfc.let { if (it == rfcHint) "" else it.trim() },
                                            pin.let { if (it == pinHint) "" else it.trim() }
                                        )
                                    )
                                )
                            }
                    ) {
                        Row(
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.savent_logo_128),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(
                                        start = 3.dp,
                                        bottom = 4.dp,
                                        top = 4.dp,
                                        end = 3.dp
                                    ),
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                modifier = Modifier.padding(bottom = 5.dp),
                                text = stringResource(id = R.string.login),
                                style = MaterialTheme.typography.h5.copy(
                                    color = Color.White,
                                    fontFamily = FontFamily(Font(R.font.comforta_bold))
                                ),
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.width(20.dp))

                            AnimatedVisibility(
                                visible = state.isLoading,
                                enter = scaleIn(),
                                exit = scaleOut()
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(30.dp),
                                    color = Color.White,
                                    strokeCap = StrokeCap.Round,
                                    strokeWidth = 3.5.dp
                                )
                            }

                            AnimatedVisibility(
                                visible = !state.isLoading,
                                enter = scaleIn(),
                                exit = fadeOut()
                            ) {
                            }
                        }

                    }

                }
                Spacer(modifier = Modifier.height(70.dp))
            }


        }
    }

}

private enum class Dialog {
    Companies, Stores
}

fun String.changeValueBehavior(
    hint: String,
    text: String,
    maxLength: Long = Long.MAX_VALUE
): String {
    if (this.length > maxLength) return text
    if (this.isEmpty()) return hint
    if (text == hint) {
        var tempText = this
        hint.forEach { c ->
            tempText = tempText.replaceFirst(c.toString(), "")
        }
        return tempText
    }
    return this
}
