package com.savent.inventory.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.savent.inventory.R


@Composable
fun SplashScreen(animationEnd: (Boolean) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.animation1))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            restartOnPlay = true,
            iterations = 1
        )
        if (progress == 1f) animationEnd(true)
        Spacer(modifier = Modifier.height(50.dp))
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier
                .requiredHeight(400.dp)
                .fillMaxWidth(),
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.savent),
                style = MaterialTheme.typography.h3.copy(
                    fontFamily = FontFamily(Font(R.font.comforta_bold)),
                    color = Color.Black
                ),
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(id = R.string.inventory),
                style = MaterialTheme.typography.h3.copy(
                    fontFamily = FontFamily(Font(R.font.comforta_bold)),
                    color = Color.Black
                ),
            )
            Spacer(modifier = Modifier.height(35.dp))
            Image(
                painter = painterResource(id = R.drawable.savent_logo_128),
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp),
                //tint = DarkBlue
            )
        }

        Spacer(modifier = Modifier.height(65.dp))
    }
}