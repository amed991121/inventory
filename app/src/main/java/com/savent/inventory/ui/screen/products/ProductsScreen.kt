package com.savent.inventory.ui.screen.products

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.savent.inventory.ui.screen.NavEvent
import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Group
import com.savent.inventory.ui.component.SearchBar
import com.savent.inventory.ui.theme.LightGray
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ProductsScreen(
    state: ProductsState,
    query: String,
    onQueryChange: (String) -> Unit,
    onEvent: (ProductsEvent) -> Unit,
    navEvent: (NavEvent) -> Unit,
    pullRefreshState: PullRefreshState,
    onCloseSession: () -> Unit,
    sheetState: ModalBottomSheetState,
    onChangeDialogState: (DialogState) -> Unit,
    actionDialog: ActionDialog,
) {

    val lazyListState = rememberLazyListState()

    val groupHint = stringResource(id = R.string.group)
    val amountHint = stringResource(id = R.string.one_decimal)
    var group by remember { mutableStateOf(groupHint) }
    var amount by remember { mutableStateOf(amountHint) }

    var isAddEntryDialogOpen by remember { mutableStateOf(false) }
    var isAnotherDialogOpen by remember { mutableStateOf(false) }

    if (sheetState.currentValue != ModalBottomSheetValue.Hidden) {
        DisposableEffect(Unit) {
            onDispose {
                if (isAddEntryDialogOpen &&  isAnotherDialogOpen) {
                    isAnotherDialogOpen = false
                    onChangeDialogState(DialogState.Show(ActionDialog.AddProductEntry))
                    return@onDispose
                }
                if(isAddEntryDialogOpen){
                    isAddEntryDialogOpen = false
                    return@onDispose
                }
                if(isAnotherDialogOpen) isAnotherDialogOpen = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.91f)
            .pullRefresh(pullRefreshState)

    ) {
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetBackgroundColor = Color.White,
            sheetContent = {
                when (actionDialog) {
                    ActionDialog.AddProductEntry -> {
                        AddProductEntryDialog(
                            product = state.selectedProduct,
                            status = state.addEntryStatus,
                            onEvent = { event ->
                                when (event) {
                                    is AddEntryDialogEvent.Add ->
                                        onEvent(
                                            ProductsEvent.AddProductEntry(
                                                amount = event.amount,
                                                groupName = event.group
                                            )
                                        )
                                    AddEntryDialogEvent.Close -> {
                                        isAddEntryDialogOpen = false
                                        onChangeDialogState(DialogState.Hide)
                                    }
                                    AddEntryDialogEvent.ShowGroupList ->{
                                        isAnotherDialogOpen = true
                                        onChangeDialogState(
                                            DialogState.Show(ActionDialog.GroupsList)
                                        )
                                    }



                                }
                            },
                            currentGroup = state.currentGroup,
                            amount = amount,
                            onAmountChange = { amount = it }
                        )
                    }

                    ActionDialog.AddGroup -> {
                        AddGroupDialog(
                            group = group,
                            status = state.addGroupStatus,
                            onEvent = { event ->
                                when (event) {
                                    is AddGroupDialogEvent.Add -> onEvent(
                                        ProductsEvent.AddGroup(
                                            Group(name = event.group)
                                        )
                                    )
                                    AddGroupDialogEvent.Close -> onChangeDialogState(DialogState.Hide)
                                }
                            },
                            onGroupChange = {
                                group = it
                            }
                        )

                    }

                    ActionDialog.GroupsList ->
                        GroupListDialog(
                            currentGroup = state.currentGroup,
                            groups = state.groups,
                            onEvent = { event ->
                                when (event) {
                                    GroupListDialogEvent.AddNewGroup -> {
                                        isAnotherDialogOpen = true
                                        onChangeDialogState(DialogState.Show(ActionDialog.AddGroup))
                                    }
                                    GroupListDialogEvent.Close -> onChangeDialogState(DialogState.Hide)
                                    is GroupListDialogEvent.SelectGroup -> {
                                        onEvent(ProductsEvent.SelectDefaultGroup(event.group))
                                    }
                                }
                            })

                    ActionDialog.Exit ->
                        CloseSessionDialog(
                            employeeName = state.employeeName,
                            storeName = state.storeName,
                            onEvent = { event ->
                                when (event) {
                                    CloseSessionDialogEvent.Close ->
                                        onChangeDialogState(DialogState.Hide)
                                    CloseSessionDialogEvent.CloseSession -> {
                                        onChangeDialogState(DialogState.Hide)
                                        onCloseSession()
                                    }
                                }
                            })
                }
            },
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            text = stringResource(id = R.string.products),
                            style = TextStyle(
                                fontSize = 35.sp,
                                fontFamily = FontFamily(Font(R.font.comforta_semibold))
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 5.dp, end = 5.dp)
                        )
                        Row(modifier = Modifier.wrapContentSize()) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_user),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(top = 7.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(LightGray.copy(alpha = 1f))
                                    .clickable {
                                        onChangeDialogState(DialogState.Show(ActionDialog.Exit))
                                    }
                                    .padding(8.dp),
                                tint = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(top = 15.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(7.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_barcode_scan),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(LightGray.copy(alpha = 1f))
                                .clickable { navEvent(NavEvent.BarCodeScanner) }
                                .padding(10.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        val textHint = stringResource(id = R.string.search)
                        SearchBar(
                            text = query,
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.comforta_medium))
                            )
                        ) {
                            onQueryChange(it)
                            onEvent(ProductsEvent.SearchProducts(if (it == textHint) "" else it))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(7.dp),
                    ) {
                        items(
                            count = state.products.size,
                            key = { state.products[it].id },
                            itemContent = {
                                Box(modifier = Modifier.animateItemPlacement(tween())) {
                                    ProductItem(product = state.products[it],
                                        onClick = {
                                            onEvent(ProductsEvent.SelectProduct(state.products[it]))
                                            group = groupHint
                                            amount = amountHint
                                            isAddEntryDialogOpen = true
                                            onChangeDialogState(DialogState.Show(ActionDialog.AddProductEntry))
                                        })
                                }
                            })
                    }
                    Divider(modifier = Modifier.height(1.dp), color = Color.Black)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.72f)
                            .clip(RoundedCornerShape(15.dp))
                            .background(LightGray.copy(alpha = 1f))
                            .clickable {
                                isAnotherDialogOpen = true
                                if (state.groups.isNotEmpty())
                                    onChangeDialogState(DialogState.Show(ActionDialog.GroupsList))
                                else
                                    onChangeDialogState(DialogState.Show(ActionDialog.AddGroup))
                            }
                            .padding(5.dp)
                    ) {

                        Spacer(modifier = Modifier.width(5.dp))

                        if (state.groups.isNotEmpty()) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_reload),
                                contentDescription = null,
                                modifier = Modifier.size(25.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = if (state.currentGroup == null)
                                    stringResource(id = R.string.group)
                                else state.currentGroup.name,
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.comforta_medium))
                                ),
                                modifier = Modifier
                                    .padding(bottom = 5.dp),
                                color = Color.Black
                            )

                        } else {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .border(
                                        border = BorderStroke(width = 2.dp, Color.Black),
                                        shape = CircleShape
                                    )
                                    //.background(DarkGray.copy(alpha = 0.08f))
                                    .padding(3.dp),
                                tint = Color.Black
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = stringResource(id = R.string.add_group),
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.comforta_medium))
                                ),
                                modifier = Modifier
                                    .padding(bottom = 5.dp),
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(5.dp))
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(15.dp))
                                .background(LightGray.copy(alpha = 1f))
                                .clickable { navEvent(NavEvent.Inventory) }
                                .padding(
                                    start = 15.dp,
                                    end = 15.dp,
                                    top = 5.dp,
                                    bottom = 5.dp
                                )
                        ) {
                            Text(
                                text = stringResource(id = R.string.inventory),
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.comforta_medium))
                                ),
                                modifier = Modifier
                                    .padding(bottom = 5.dp),
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_go_inventory),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp),
                                tint = Color.Black
                            )
                        }
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

enum class ActionDialog {
    AddProductEntry, AddGroup, GroupsList, Exit
}

sealed class DialogState {
    class Show(val type: Any) : DialogState()
    object Hide : DialogState()
}
