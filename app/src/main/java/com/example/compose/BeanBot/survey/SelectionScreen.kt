/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.BeanBot.survey

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.compose.BeanBot.R
import com.example.compose.BeanBot.util.Arduino_Communicate
import com.example.compose.BeanBot.util.Arduino_Communicate.RequestBonen
import com.example.compose.BeanBot.util.supportWideScreen
import kotlinx.coroutines.delay
import java.net.HttpURLConnection
import java.net.URL

sealed class SelectionEvent {
    object StartProsces : SelectionEvent()

}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SurveyQuestionsScreen(

    onDonePressed: () -> Unit,
    onEvent: (SelectionEvent) -> Unit,
    onBackPressed: () -> Unit,
    viewmodel: SelectionViewModel
){
    /*https://stackoverflow.com/questions/47880450/how-to-vibrate-android-device-on-button-click-using-vibrator-effects-using-kotli*/
    /* IP ADRESS*/

    val Ip = viewmodel.ipadress
    var Connected = remember { mutableStateOf(false)}
    val vibrator =  LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    LaunchedEffect(key1 = Connected.value) {
        Arduino_Communicate.detect_device(Connected, vibrator, Ip)
    }


        Surface(modifier = Modifier.supportWideScreen()) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        /*todo : deze shit automereren*/

                        onBackPressed = onBackPressed,
                        Connected = Connected
                    )
                },
                content = { innerPadding ->
                    Question(viewmodel,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding))
                },

                bottomBar = {
                    BottomBar(
                        onEvent = onEvent,
                        onBackPressed = onBackPressed,
                        viewmodel = viewmodel,
                        Ip = Ip,
                        Connected=Connected
                    )
                }
            )
        }
    }



@Composable
private fun TopAppBarTitle(

    modifier: Modifier = Modifier,
    Connected:MutableState<Boolean>
) {
    val indexStyle = MaterialTheme.typography.caption.toSpanStyle().copy(
        fontWeight = FontWeight.Bold
    )
    val text = buildAnnotatedString {
        withStyle(style = indexStyle) {
            if( Connected.value){
                append("connected")}
            else {append("connecting")}
        }
    }
    Text(
        text = text,
        style = MaterialTheme.typography.caption,
        modifier = modifier
    )
}

@Composable
private fun TopAppBar(
    onBackPressed: () -> Unit,
    Connected:MutableState<Boolean>

){
Column(modifier = Modifier.fillMaxWidth()) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TopAppBarTitle(

            modifier = Modifier
                .padding(vertical = 10.dp)
                .align(Alignment.Center),
            Connected = Connected
        )

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription ="close",
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
    val animatedProgress by animateFloatAsState(
        targetValue = (1f + 1f) / 2f,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    var BackgroundColorBar = if (Connected.value){MaterialTheme.colors.primary} else{MaterialTheme.colors.error}
    LinearProgressIndicator(
        progress = animatedProgress,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp).clickable {  },
        backgroundColor = BackgroundColorBar,
        color = BackgroundColorBar
    )
}
}

@Composable
private fun BottomBar(
    onEvent: (SelectionEvent) -> Unit,
    onBackPressed: () -> Unit,
    viewmodel: SelectionViewModel,
    Ip: String,
    Connected: MutableState<Boolean>
) {

    Surface(
        elevation = 7.dp,
        modifier = Modifier.fillMaxWidth() // .border(1.dp, MaterialTheme.colors.primary)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            /* todo : automatiseren */
            OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = { onBackPressed() }
                ) {
                    Text(text = stringResource(id = R.string.previous))
                }
                Spacer(modifier = Modifier.width(16.dp))


            Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = ({
                        onEvent(SelectionEvent.StartProsces);
                        RequestBonen(viewmodel , Ip)}),
                    /*enabelee*/
                    enabled = true
                ) {
                    Text(text = stringResource(id = R.string.done))
                }
            }

        }
    }

@Composable
private fun TopAppBarTitle_delivering(
    modifier: Modifier = Modifier
) {

    Text(
        text = "32 seconden left",
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )



}

