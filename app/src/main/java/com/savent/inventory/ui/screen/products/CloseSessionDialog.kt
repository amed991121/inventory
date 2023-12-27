package com.savent.inventory.ui.screen.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.savent.inventory.R
import com.savent.inventory.ui.theme.LightGray

@Composable
fun CloseSessionDialog(
    employeeName: String,
    storeName: String,
    onEvent: (CloseSessionDialogEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 10.dp, end = 15.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.2f))
                    .align(
                        Alignment.TopEnd
                    )
                    .clickable { onEvent(CloseSessionDialogEvent.Close) }
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(45.dp))
            Column() {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = null,
                        modifier = Modifier
                            .size(37.dp)
                            .clip(CircleShape)
                            .background(LightGray.copy(alpha = 1f))
                            .padding(8.dp),
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        fontSize = 21.sp,
                        fontFamily = FontFamily(Font(R.font.comforta_semibold)),
                        text = employeeName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 0.dp),
                        color = Color.Black.copy(alpha = 1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_store),
                        contentDescription = null,
                        modifier = Modifier
                            .size(37.dp)
                            .clip(CircleShape)
                            .background(LightGray.copy(alpha = 1f))
                            .padding(8.dp),
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        fontSize = 21.sp,
                        fontFamily = FontFamily(Font(R.font.comforta_semibold)),
                        text = storeName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 0.dp),
                        color = Color.Black.copy(alpha = 1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .clip(RoundedCornerShape(13.dp))
                    .background(Color.Black)
                    .clickable {
                        onEvent(CloseSessionDialogEvent.CloseSession)
                    }
                    .padding(5.dp)

            ) {
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    modifier = Modifier.padding(bottom = 5.dp),
                    text = stringResource(id = R.string.close_session),
                    style = MaterialTheme.typography.h5.copy(
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.comforta_bold))
                    ),
                    textAlign = TextAlign.Center,
                )

            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

sealed class CloseSessionDialogEvent {
    object Close : CloseSessionDialogEvent()
    object CloseSession : CloseSessionDialogEvent()
}