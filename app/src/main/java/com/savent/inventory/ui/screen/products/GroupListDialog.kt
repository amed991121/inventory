package com.savent.inventory.ui.screen.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.savent.inventory.R
import com.savent.inventory.data.comom.model.Group
import com.savent.inventory.ui.theme.LightGray

@Composable
fun GroupListDialog(
    currentGroup: Group?,
    groups: List<Group>,
    onEvent: (GroupListDialogEvent) -> Unit
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
                    .clickable { onEvent(GroupListDialogEvent.Close) }
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

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    fontSize = 23.sp,
                    fontFamily = FontFamily(Font(R.font.comforta_bold)),
                    text = stringResource(id = R.string.groups),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black.copy(alpha = 1f),
                    modifier = Modifier.padding(start = 20.dp)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                )
                {
                    items(
                        count = groups.size,
                        key = { idx -> groups[idx].id },
                        itemContent = { idx ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(15.dp))
                                        .background(LightGray.copy(alpha = 1f))
                                        .clickable { onEvent(GroupListDialogEvent.SelectGroup(groups[idx])) }
                                        .padding(
                                            start = 15.dp,
                                            end = 15.dp,
                                            top = 5.dp,
                                            bottom = 5.dp
                                        )
                                ) {
                                    Text(
                                        text = groups[idx].name,
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            fontFamily = FontFamily(Font(R.font.comforta_medium))
                                        ),
                                        modifier = Modifier
                                            .padding(bottom = 5.dp),
                                        color = Color.Black
                                    )
                                }

                                AnimatedVisibility(
                                    visible = currentGroup == groups[idx],
                                    enter = scaleIn(),
                                    exit = scaleOut()
                                ) {
                                    Row() {
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Icon(
                                            imageVector = Icons.Rounded.Check,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(25.dp)
                                                .clip(CircleShape)
                                                .background(Color.Black)
                                                .padding(3.dp),
                                            tint = Color.White
                                        )
                                    }
                                }
                            }

                        })
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(LightGray.copy(alpha = 1f))
                        .clickable { onEvent(GroupListDialogEvent.AddNewGroup) }
                        .padding(
                            start = 15.dp,
                            end = 15.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        )
                ) {

                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                            .padding(3.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = stringResource(id = R.string.add_new),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.comforta_medium))
                        ),
                        modifier = Modifier
                            .padding(bottom = 5.dp),
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

            }
        }


    }
}

sealed class GroupListDialogEvent {
    object Close : GroupListDialogEvent()
    class SelectGroup(val group: Group) : GroupListDialogEvent()
    object AddNewGroup : GroupListDialogEvent()
}