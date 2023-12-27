package com.savent.inventory.ui.screen.products

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Group
import com.savent.inventory.data.comom.model.Product
import com.savent.inventory.ui.component.CustomTextField
import com.savent.inventory.ui.screen.login.changeValueBehavior
import com.savent.inventory.ui.theme.LightGray

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddProductEntryDialog(
    product: Product,
    status: AddEntryStatus,
    onEvent: (AddEntryDialogEvent) -> Unit,
    currentGroup: Group?,
    amount: String,
    onAmountChange: (String) -> Unit
) {


    var isCollapsed by remember { mutableStateOf(true) }

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
                    .clickable { onEvent(AddEntryDialogEvent.Close) }
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
            Spacer(modifier = Modifier.height(50.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(LightGray)
                        .padding(6.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    fontSize = 23.sp,
                    fontFamily = FontFamily(Font(R.font.comforta_regular)),
                    text = product.description,
                    maxLines = if (isCollapsed) 1 else Int.MAX_VALUE,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(bottom = 3.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ){ isCollapsed = !isCollapsed },
                    color = Color.Black.copy(alpha = 1f),
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            val shape = RoundedCornerShape(13.dp)
            val textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.comforta_medium)),
                fontSize = 19.sp
            )

            val groupHint = stringResource(id = R.string.group)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(LightGray.copy(alpha = 1f))
                    .clickable { onEvent(AddEntryDialogEvent.ShowGroupList) }
                    .padding(
                        start = 15.dp,
                        end = 15.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
            ) {

                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_group),
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = currentGroup?.name ?: groupHint,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.comforta_medium))
                    ),
                    modifier = Modifier
                        .padding(bottom = 5.dp),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(10.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                val amountHint = stringResource(id = R.string.one_decimal)
                CustomTextField(
                    value = amount,
                    onValueChange = {
                        onAmountChange(it.changeValueBehavior(hint = amountHint, text = amount))
                    },
                    modifier = Modifier
                        .requiredHeight(50.dp)
                        .width(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(LightGray.copy(alpha = 1f)),
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.comforta_regular)),
                        color = Color.DarkGray.copy(alpha = 1f),
                        textAlign = TextAlign.End
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = product.unit,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.comforta_semibold))
                    ),
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .clip(shape)
                    .background(Color.Black)
                    .clickable {
                        onEvent(
                            AddEntryDialogEvent.Add(
                                amount = amount
                                    .ifEmpty { "0" }
                                    .trim()
                                    .toFloat(),
                                group = currentGroup?.name?:""
                            )
                        )
                    }
                    .padding(5.dp)

            ) {
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    modifier = Modifier.padding(bottom = 5.dp),
                    text = stringResource(id = R.string.register),
                    style = MaterialTheme.typography.h5.copy(
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.comforta_bold))
                    ),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.width(20.dp))

                AnimatedVisibility(
                    visible = status == AddEntryStatus.Loading,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(28.dp),
                        color = Color.White,
                        strokeCap = StrokeCap.Round,
                        strokeWidth = 3.dp
                    )
                }

                if (status == AddEntryStatus.Success) {
                    onEvent(AddEntryDialogEvent.Close)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

}


sealed class AddEntryDialogEvent {
    object ShowGroupList: AddEntryDialogEvent()
    class Add(val amount: Float, val group: String) : AddEntryDialogEvent()
    object Close : AddEntryDialogEvent()
}

enum class AddEntryStatus {
    Default, Loading, Success, Error
}