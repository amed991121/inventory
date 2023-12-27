package com.savent.inventory.ui.screen.inventory

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.savent.inventory.R
import com.savent.inventory.ui.component.CustomTextField
import com.savent.inventory.ui.component.SearchBar
import com.savent.inventory.ui.screen.NavEvent
import com.savent.inventory.ui.screen.inventory.store_filter.StoreFilterDialog
import com.savent.inventory.ui.screen.login.changeValueBehavior
import com.savent.inventory.ui.theme.DarkGray
import com.savent.inventory.ui.theme.LightGray
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun InventoryScreen(
    state: InventoryState,
    onEvent: (InventoryEvent) -> Unit,
    navEvent: (NavEvent) -> Unit,
    pullRefreshState: PullRefreshState
) {

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()

    val lazyListState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.91f)
            .pullRefresh(pullRefreshState)
    ) {
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                StoreFilterDialog(
                    storeFilterList = state.storeFilterList,
                    onChangeFilter = { storeId, isChecked ->
                        onEvent(
                            InventoryEvent.ChangeStoreFilter(
                                storeId = storeId,
                                isSelected = isChecked
                            )
                        )
                    },
                    onClearFilters = { onEvent(InventoryEvent.ClearAllStoreFilter) },
                    onClose = { scope.launch { sheetState.hide() } }
                )
            },
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetBackgroundColor = Color.White,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(modifier = Modifier.padding(15.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_round_arrow_back_ios_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape)
                                .background(LightGray)
                                .clickable { navEvent(NavEvent.Back) }
                                .padding(10.dp),
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = stringResource(id = R.string.inventory),
                            style = TextStyle(
                                fontSize = 35.sp,
                                fontFamily = FontFamily(Font(R.font.comforta_semibold))
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 7.dp, end = 5.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))
                    val textHint = stringResource(id = R.string.search)
                    var text by remember { mutableStateOf(textHint) }
                    Row() {
                        Spacer(modifier = Modifier.width(2.dp))
                        SearchBar(
                            text = text,
                            textStyle = TextStyle(
                                fontSize = 19.sp,
                                fontFamily = FontFamily(Font(R.font.comforta_medium))
                            )
                        ) {
                            text = it
                            onEvent(InventoryEvent.SearchEntriesByQuery(if (it == textHint) "" else it))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        items(
                            count = state.inventory.size,
                            key = { state.inventory[it].product.id },
                            itemContent = {
                                Box(modifier = Modifier.animateItemPlacement(tween())) {
                                    InventoryItem(productInventory = state.inventory[it])
                                }
                            })
                    }
                    Divider(modifier = Modifier.height(1.dp), color = Color.Black)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(LightGray.copy(alpha = 1f))
                                .clickable {
                                    scope.launch { sheetState.show() }
                                }
                                .padding(10.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        val groupHint = stringResource(id = R.string.group)
                        var text by rememberSaveable { mutableStateOf(groupHint) }
                        val textStyle = TextStyle(
                            fontSize = 19.sp,
                            fontFamily = FontFamily(Font(R.font.comforta_medium))
                        )
                        CustomTextField(
                            value = text,
                            onValueChange = {
                                text = it.changeValueBehavior(groupHint, text)
                                onEvent(
                                    InventoryEvent.SearchEntriesByGroup(
                                        if (text != groupHint) text else ""
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(align = Alignment.Top)
                                .clip(RoundedCornerShape(13.dp))
                                .background(color = Color.LightGray.copy(alpha = 0.25f))
                                .padding(3.dp),
                            textStyle = if (text == groupHint)
                                textStyle.copy(color = DarkGray.copy(alpha = 0.4f)) else textStyle,
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_menu),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(6.dp),
                                    tint = DarkGray.copy(alpha = 0.4f)
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .padding(6.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() })
                                        {
                                            onEvent(InventoryEvent.SearchEntriesByGroup(""))
                                            text = groupHint
                                        },
                                    tint = DarkGray.copy(alpha = 0.4f)
                                )

                            },
                            singleLine = true
                        )
                    }
                }

            }
        }

        PullRefreshIndicator(
            refreshing = state.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(
                Alignment.TopCenter
            )
        )
    }
}