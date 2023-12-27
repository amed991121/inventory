package com.savent.inventory.ui.screen.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Product

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {

    var isCollapsed by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            //.background(LightGray.copy(alpha = 1f))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_package),
            contentDescription = null,
            modifier = Modifier
                .size(45.dp)
                .fillMaxSize(),
            tint = Color.Black
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                fontSize = 19.sp,
                fontFamily = FontFamily(Font(R.font.comforta_medium)),
                text = product.description,
                maxLines = if (isCollapsed) 1 else Int.MAX_VALUE,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black.copy(alpha = 1f),
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { isCollapsed = !isCollapsed }
            )

            Spacer(modifier = Modifier.height(2.dp))

            if (product.barcode.isNotEmpty())
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_barcode),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp),
                        tint = Color.DarkGray.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.comforta_regular)),
                        text = product.barcode,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 3.dp),
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                }
        }

        Spacer(modifier = Modifier.width(15.dp))
        Row() {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null,
                modifier = Modifier
                    .size(31.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .padding(5.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(1.dp))
        }

    }


}