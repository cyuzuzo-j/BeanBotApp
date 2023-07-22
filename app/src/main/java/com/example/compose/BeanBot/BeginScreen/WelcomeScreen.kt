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

package com.example.compose.BeanBot.BeginScreen

import android.content.Context
import android.view.View
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.BeanBot.R
import com.example.compose.BeanBot.theme.AppTheme
import com.example.compose.BeanBot.util.supportWideScreen

sealed class WelcomeEvent {
    object StartSurvey : WelcomeEvent()
    object startDebug : WelcomeEvent()


}

@Composable
fun WelcomeScreen(onEvent: (WelcomeEvent) -> Unit, View: View?) {
    var brandingBottom by remember { mutableStateOf(0f) }
    var showBranding by remember { mutableStateOf(true) }
    var heightWithBranding by remember { mutableStateOf(0) }
    val currentOffsetHolder = remember { mutableStateOf(0f) }
    currentOffsetHolder.value = if (showBranding) 0f else -brandingBottom
    val currentOffsetHolderDp =
        with(LocalDensity.current) { currentOffsetHolder.value.toDp() }
    val heightDp = with(LocalDensity.current) { heightWithBranding.toDp() }

    Surface(modifier = Modifier.supportWideScreen()) {
        val offset by animateDpAsState(targetValue = currentOffsetHolderDp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .brandingPreferredHeight(showBranding, heightDp)
                .offset(y = offset)
                .onSizeChanged {
                    if (showBranding) {
                        heightWithBranding = it.height
                    }
                }
        ) {
            Branding(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .onGloballyPositioned {
                        if (brandingBottom == 0f) {
                            brandingBottom = it.boundsInParent().bottom
                        }
                    },
                v = View
            )
            launch(
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                v = View

            )
        }
    }
}

private fun Modifier.brandingPreferredHeight(
    showBranding: Boolean,
    heightDp: Dp
): Modifier {
    return if (!showBranding) {
        this
            .wrapContentHeight(unbounded = true)
            .height(heightDp)
    } else {
        this
    }
}

@Composable
private fun Branding(modifier: Modifier = Modifier,v: View?) {
    var setip =  remember { mutableStateOf(false) }
    if (setip.value){
        if (v != null) {
            set_ipadress(v,setip)

        }
    }

    Column(
        modifier = modifier.wrapContentHeight(align = Alignment.CenterVertically)
    ) {
        Logo(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(id = R.string.teamnaam),
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 1.dp)
                .fillMaxWidth()
                .clickable { setip.value = !setip.value}
        )
    }
}

@Composable
private fun Logo(
    modifier: Modifier = Modifier,
) {
    Text(
        text =stringResource(id = R.string.app_name),
        style = MaterialTheme.typography.h2,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun launch(
    onEvent: (WelcomeEvent) -> Unit,
    modifier: Modifier = Modifier,
    v: View?
) {
    var setip =  remember { mutableStateOf(false) }
    if (setip.value){
        if (v != null) {
            set_ipadress(v,setip)

        }
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { onEvent(WelcomeEvent.startDebug) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, bottom = 3.dp)
        ) {
            Text(
                text =
                    stringResource(id = R.string.Start),
                style = MaterialTheme.typography.subtitle2
            )
        }
        OutlinedButton(
            onClick = { setip.value = true },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 24.dp)
        ) {
            Text(text = "set ip adress")
        }
}

}
@Composable
fun set_ipadress(v: View,opened:MutableState<Boolean>){
    /*https://stackoverflow.com/questions/68852110/show-custom-alert-dialog-in-jetpack-compose*/
    var text by remember { mutableStateOf("") }

    if (opened.value) {
    AlertDialog(
        onDismissRequest = {
            opened.value = false
        },
        title = {
            Text(text =  stringResource(id = R.string.Ipnotset))
        },
        text = {
            Column() {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = {Text(stringResource(id = R.string.getIp))})
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    onClick = {
                        /*Kan potentiel chrashes veroorzake*/
                        var  myActivity: android.app.Activity? = (v.getContext()) as android.app.Activity?
                        val sharedPref = myActivity?.getPreferences(Context.MODE_PRIVATE)
                        if (sharedPref != null) {
                            with (sharedPref.edit()) {
                                putString("Ipadress", text)
                                apply()
                            }
                        }
                        opened.value = false
                    }
                ) {
                    Text("accept")
                }
            }
        }
    )
}}
