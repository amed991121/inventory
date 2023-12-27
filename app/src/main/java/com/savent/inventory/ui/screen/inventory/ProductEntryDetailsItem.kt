package com.savent.inventory.ui.screen.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.savent.inventory.R
import com.savent.inventory.ui.theme.LightGray

@Composable
fun ProductEntryDetailsItem(entry: ProductEntryDetails, unit: String) {

    Divider(modifier = Modifier
        .height(1.dp)
        .padding(start = 3.dp, end = 1.dp)
        .background(Color.Black))
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(start = 4.dp, end = 12.dp)
    ) {

        itemRow(
            painter = painterResource(id = R.drawable.ic_user),
            text = entry.employeeName,
            paddingValues = PaddingValues(10.dp)
        )

        itemRow(
            painter = painterResource(id = R.drawable.ic_store),
            text = entry.storeName,
            paddingValues = PaddingValues(10.dp)
        )

        itemRow(
            painter = painterResource(id = R.drawable.ic_group),
            text = entry.group,
            paddingValues = PaddingValues(8.dp)
        )

        itemRow(
            painter = painterResource(id = R.drawable.ic_box),
            text = "${entry.amount} $unit",
            paddingValues = PaddingValues(8.dp)
        )

        itemRow(
            painter = painterResource(id = R.drawable.ic_clock),
            text = entry.datetime.toLowerCase(),
            paddingValues = PaddingValues(10.dp)
        )
    }
}

@Composable
private fun itemRow(painter: Painter, text: String, paddingValues: PaddingValues) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(LightGray)
                .padding(paddingValues)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            fontSize = 17.sp,
            fontFamily = FontFamily(Font(R.font.comforta_medium)),
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black.copy(alpha = 1f)
        )
    }
}