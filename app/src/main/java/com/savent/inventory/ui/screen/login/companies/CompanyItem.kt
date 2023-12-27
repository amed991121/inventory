package com.savent.inventory.ui.screen.login.companies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Company
import com.savent.inventory.ui.component.RoundedBox

@Composable
fun CompanyItem(company: Company, onClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(company.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundedBox(
            width = 42.dp,
            height = 42.dp,
            cornerSize = 15.dp,
            Color.LightGray.copy(alpha = 0.2f),
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_company),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    tint = Color.Black
                )
            }
        )

        Text(
            fontSize = 19.sp,
            fontFamily = FontFamily(Font(R.font.comforta_regular)),
            text = company.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(start = 15.dp, end = 15.dp)
                .align(Alignment.CenterVertically)
        )

    }
}