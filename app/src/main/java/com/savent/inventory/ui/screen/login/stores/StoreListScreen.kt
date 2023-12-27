package com.savent.inventory.ui.screen.login.stores

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.savent.inventory.data.comom.model.Store
import com.savent.inventory.ui.component.SearchBar
import com.savent.inventory.ui.screen.ListScreenEvent
import com.savent.inventory.R

@Composable
fun StoreListScreen(stores: List<Store>, onEvent: (ListScreenEvent) -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(800.dp)) {
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 10.dp, end = 15.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.2f))
                    .align(
                        Alignment.TopEnd
                    )
                    .clickable { onEvent(ListScreenEvent.Close) }
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .size(35.dp)
                        .align(Alignment.Center),
                    tint = Color.Black
                )
            }
        }
        Column(modifier = Modifier
            .padding(15.dp)
            .wrapContentHeight()) {
            Text(
                text = stringResource(id = R.string.stores),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.comforta_semibold))
                ),
                modifier = Modifier.padding(start = 5.dp)
            )
            Spacer(modifier = Modifier.padding(top = 15.dp))
            val textHint = stringResource(id = R.string.search)
            var text by remember { mutableStateOf(textHint) }
            SearchBar(
                text = text,
                onValueChange = {
                    text = it
                    onEvent(ListScreenEvent.Search(if (it == textHint) "" else it))
                },
                textStyle = TextStyle(
                    fontSize = 19.sp,
                    fontFamily = FontFamily(Font(R.font.comforta_medium))
                )
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(
                    count = stores.size,
                    key = { idx -> stores[idx].id },
                    itemContent = { idx ->
                        StoreItem(store = stores[idx], onClick = {
                            onEvent(ListScreenEvent.Select(it))
                            onEvent(ListScreenEvent.Close)
                        })
                    })
            }
        }
    }

}
