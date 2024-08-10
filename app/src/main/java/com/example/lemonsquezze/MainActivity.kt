package com.example.lemonsquezze

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonsquezze.ui.theme.LemonSquezzeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LemonSquezzeTheme {
                LemonSqueezeApp(context = applicationContext)
            }
        }
    }
}

@Composable
fun LemonSqueezeApp(context : Context) {
    Box(
        modifier = Modifier
            .background(Color.Yellow)
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = "Lemonade",
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 20.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
    }
    var value by remember { mutableIntStateOf(1) }
    var squeezeCount by remember { mutableIntStateOf(0) }
    var randomSqueezeCount by remember { mutableIntStateOf(0) }
    val currImage = when (value) {
        1 -> R.drawable.lemon_tree
        2 -> R.drawable.lemon_squeeze
        3 -> R.drawable.lemon_drink
        else -> R.drawable.lemon_restart
    }
    var currString = when (value) {
        1 -> stringResource(R.string.lemon_tree)
        2 -> if (squeezeCount == 0) {
            stringResource(R.string.lemon)
        } else if (squeezeCount >= 1 && squeezeCount < randomSqueezeCount) {
            stringResource(R.string.tapping)
        } else {
            "Yamete kudussai"
        }

        3 -> stringResource(R.string.lemonade)
        else -> stringResource(R.string.empty_glass)
    }
    val mediaPlayer = remember { MediaPlayer() }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

        ) {
        Button(
            onClick = {
                if (value == 1) {
                    value++
                    randomSqueezeCount = (4..10).random()
                    playSound(context, mediaPlayer, R.raw.memesnd1)
                } else if (value == 2) {
                    if (squeezeCount < randomSqueezeCount) {
                        squeezeCount++
                        playSound(context, mediaPlayer, R.raw.squeezeaudio)
                        if (squeezeCount == randomSqueezeCount) {
                            playSound(context, mediaPlayer, R.raw.yametekudusai)
                        }
                    } else {
                        playSound(context, mediaPlayer, R.raw.lethimcook)
                        value++
                        squeezeCount = 0
                    }
                } else if (value == 3) {
                    value++
                    playSound(context, mediaPlayer, R.raw.ronaldodrinking)
                } else {
                    value = 1
                    playSound(context, mediaPlayer, R.raw.ooooooooomemesound)
                }
            },
            modifier = Modifier,
            colors = ButtonDefaults.buttonColors(Color(0xFFcaf0f8)),
            shape = RoundedCornerShape(24.dp),
            elevation = ButtonDefaults.buttonElevation(20.dp),
        ) {
            Image(
                painter = painterResource(id = currImage),
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.padding(24.dp))
        Text(
            text = currString,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
}

private fun playSound(context : Context, mediaPlayer : MediaPlayer, soundResourceId : Int) {
    mediaPlayer.reset()
    mediaPlayer.setDataSource(
        context,
        Uri.parse("android.resource://${context.packageName}/$soundResourceId")
    )
    mediaPlayer.prepare()
    mediaPlayer.start()
}

@Preview(showBackground = true)
@Composable
fun LemonSquezzePreview() {
    LemonSquezzeTheme {
        LemonSqueezeApp(LocalContext.current)
    }
}