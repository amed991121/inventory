package com.savent.inventory.ui.screen.inventory

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
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
import com.savent.inventory.ui.theme.LightGray

@Composable
fun InventoryItem(productInventory: ProductInventory) {

    var isCollapse by remember { mutableStateOf(true) }

    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    isCollapse = !isCollapse
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_checklist),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .fillMaxSize(),
                tint = Color.Black
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    fontSize = 19.sp,
                    fontFamily = FontFamily(Font(R.font.comforta_medium)),
                    text = productInventory.product.description,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black.copy(alpha = 1f)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.comforta_regular)),
                        text = "${productInventory.totalAmount}  ${productInventory.product.unit}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 3.dp),
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                }

            }

            Spacer(modifier = Modifier.width(8.dp))
            Row() {
                Icon(
                    imageVector = if (isCollapse)
                        Icons.Rounded.KeyboardArrowDown
                    else Icons.Rounded.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(LightGray)
                        .clickable { isCollapse = !isCollapse }
                        .padding(5.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(1.dp))
            }


        }

        AnimatedVisibility(!isCollapse) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Spacer(modifier = Modifier.padding(3.dp))
                productInventory.entries.forEach { entry ->
                    ProductEntryDetailsItem(entry = entry, unit = productInventory.product.unit)
                }
            }
        }
    }

}