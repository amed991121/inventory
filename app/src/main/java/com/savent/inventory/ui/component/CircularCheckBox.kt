package com.savent.inventory.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CircularCheckBox(
    size: Dp = defaultSize,
    borderStroke: Dp = defaultBorderStroke,
    borderColor: Color = defaultBorderColor,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(
        checkedColor = defaultCheckedColor,
        checkmarkColor = defaultCheckMarkColor,
    )
) {
    CustomCheckBox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .border(
                border = BorderStroke(
                    width = if(checked) 0.dp else borderStroke,
                    color = borderColor
                ), CircleShape
            ),
        enabled = enabled,
        colors = colors
    )
}

private val defaultSize = 25.dp
private val defaultBorderStroke = 2.dp
private val defaultBorderColor = Color.LightGray
private val defaultCheckedColor = Color.Blue.copy(alpha = 0.7f)
private val defaultCheckMarkColor = Color.White
