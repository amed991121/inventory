package com.savent.inventory.ui.screen.inventory.store_filter

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.savent.inventory.R
import com.savent.inventory.ui.theme.LightGray

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StoreFilterDialog(
    storeFilterList: List<StoreFilter>,
    onChangeFilter: (Int, Boolean) -> Unit,
    onClearFilters: ()->Unit,
    onClose: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 10.dp, end = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.2f))
                    .align(
                        Alignment.TopEnd
                    )
                    .clickable { onClose() }
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    tint = Color.Black
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                fontSize = 21.sp,
                fontFamily = FontFamily(Font(R.font.comforta_bold)),
                text = stringResource(id = R.string.filter_by_store),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black.copy(alpha = 1f),
                modifier = Modifier.padding(start = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                items(
                    count = storeFilterList.size,
                    key = { storeFilterList[it].store.id },
                    itemContent = {
                        Box(modifier = Modifier.animateItemPlacement(tween())) {
                            StoreFilterItem(
                                storeFilter = storeFilterList[it],
                                onChecked = { isChecked ->
                                    onChangeFilter(storeFilterList[it].store.id, isChecked)
                                })
                        }
                    })
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.comforta_medium)),
                    text = stringResource(id = R.string.clear_filters),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black.copy(alpha = 1f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .background(LightGray)
                        .clickable { onClearFilters() }
                        .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 8.dp)
                )
            }
        }
    }
}