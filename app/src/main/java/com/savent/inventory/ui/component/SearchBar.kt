package com.savent.inventory.ui.component

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.savent.inventory.R
import com.savent.inventory.ui.screen.login.changeValueBehavior
import com.savent.inventory.ui.theme.DarkGray

@Composable
fun SearchBar(
    text: String,
    textStyle: TextStyle = LocalTextStyle.current,
    onValueChange: (String) -> Unit,
) {

    val hint = stringResource(id = R.string.search)

    CustomTextField(
        value = text,
        onValueChange = {
            onValueChange(it.changeValueBehavior(hint, text))
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clip(RoundedCornerShape(13.dp))
            .background(color = Color.LightGray.copy(alpha = 0.25f))
            .padding(3.dp),
        textStyle = if (text == hint)
            textStyle.copy(color = DarkGray.copy(alpha = 0.4f)) else textStyle,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier
                    .size(37.dp)
                    .padding(8.dp),
                tint = DarkGray.copy(alpha = 0.4f)
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .padding(8.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() })
                    {
                        onValueChange(hint)
                    },
                tint = DarkGray.copy(alpha = 0.4f)
            )

        },
        singleLine = true
    )

}
