package com.savent.inventory.ui.screen.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.savent.inventory.R
import com.savent.inventory.ui.component.CustomTextField
import com.savent.inventory.ui.screen.login.changeValueBehavior
import com.savent.inventory.ui.theme.DarkGray
import com.savent.inventory.ui.theme.LightGray

@Composable
fun AddGroupDialog(
    group: String,
    status: AddGroupStatus,
    onEvent: (AddGroupDialogEvent) -> Unit,
    onGroupChange: (String) -> Unit,
) {
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
                    .clickable { onEvent(AddGroupDialogEvent.Close) }
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
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                fontSize = 21.sp,
                fontFamily = FontFamily(Font(R.font.comforta_bold)),
                text = stringResource(id = R.string.add_new_group),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black.copy(alpha = 1f),
                modifier = Modifier.padding(start = 20.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            val groupHint = stringResource(id = R.string.group)
            val shape = RoundedCornerShape(13.dp)
            val textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.comforta_medium)),
                fontSize = 19.sp
            )
            CustomTextField(
                value = group,
                onValueChange = {
                    onGroupChange(it.changeValueBehavior(hint = groupHint, text = group))
                },
                modifier = Modifier
                    .requiredHeight(50.dp)
                    .width(200.dp)
                    .clip(shape)
                    .background(LightGray.copy(alpha = 1f)),
                textStyle = if (group == groupHint) textStyle.copy(
                    color = DarkGray.copy(alpha = 0.4f)
                ) else textStyle,
                leadingIcon = {
                    Row() {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_group),
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp)
                                .padding(top = 5.dp, bottom = 5.dp),
                            tint = DarkGray.copy(alpha = 0.5f)
                        )
                    }

                },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = group != groupHint,
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .size(38.dp)
                                .padding(end = 10.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    onGroupChange(groupHint)
                                },
                            tint = DarkGray.copy(alpha = 0.5f)
                        )
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(15.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .clip(shape)
                    .background(Color.Black)
                    .clickable {
                        onEvent(AddGroupDialogEvent.Add(if (group == groupHint) "" else group))
                    }
                    .padding(10.dp)

            ) {
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = stringResource(id = R.string.save),
                    style = MaterialTheme.typography.h5.copy(
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.comforta_bold))
                    ),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.width(20.dp))

                AnimatedVisibility(
                    visible = status == AddGroupStatus.Loading,
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

                if (status == AddGroupStatus.Success) {
                    onEvent(AddGroupDialogEvent.Close)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

sealed class AddGroupDialogEvent {
    class Add(val group: String) : AddGroupDialogEvent()
    object Close : AddGroupDialogEvent()
}

enum class AddGroupStatus {
    Default, Loading, Success, Error
}