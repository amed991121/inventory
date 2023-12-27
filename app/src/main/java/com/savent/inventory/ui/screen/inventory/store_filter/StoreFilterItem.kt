package com.savent.inventory.ui.screen.inventory.store_filter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.savent.inventory.ui.component.CircularCheckBox
import com.savent.inventory.R
import com.savent.inventory.ui.theme.LightGray


@Composable
fun StoreFilterItem(storeFilter: StoreFilter, onChecked: (Boolean) -> Unit) {
    var isChecked = remember {
        mutableStateOf(storeFilter.isSelected)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onChecked(!storeFilter.isSelected)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_store),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(LightGray)
                .padding(10.dp),
            tint = Color.Black
        )
        Text(
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.comforta_regular))
            ),
            text = storeFilter.store.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(start = 15.dp, end = 15.dp)
                .align(Alignment.CenterVertically)
        )

        CircularCheckBox(
            checked = storeFilter.isSelected,
            onCheckedChange = {
                onChecked(it)
            },
            size = 23.dp,
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Black
            )
        )

    }
}