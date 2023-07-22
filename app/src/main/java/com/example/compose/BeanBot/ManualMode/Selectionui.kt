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

package com.example.compose.BeanBot.ManualMode
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.outlinedButtonColors
import androidx.compose.material.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.compose.BeanBot.R

/* creaeert de ui voor de vragen*/
/*todo: EXPIRIMENTELE API GEBRUIKT */
/*todo: REGELE DAK DOOR KAN SCROLLEN ALSK OP ENTER DRUK*/
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Manual_Question(
    viewmodel: Manual_SelectionViewModel,
    modifier: Modifier = Modifier,


) {
    /*https://proandroiddev.com/the-big-form-with-jetpack-compose-7bec9cde157e*/
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp)

    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))
            QuestionTitle(R.string.hoeveelheid)
            Spacer(modifier = Modifier.height(12.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = "kies een silo",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(bottom = 18.dp, start = 4.dp, end = 8.dp,top=0.dp)
                )
            }
            SILO_chooser(
                modifier = Modifier,
                SILO_name = "Silo 1 ",
                Onclick = {viewmodel.silo.value = 1;},viewmodel.silo.value==1)
            SILO_chooser(
                modifier = Modifier,
                SILO_name = "Silo 2",
                Onclick = {viewmodel.silo.value = 2;},viewmodel.silo.value==2)
            SILO_chooser(
                modifier = Modifier,
                SILO_name = "Silo 3",
                Onclick = {viewmodel.silo.value = 3;},viewmodel.silo.value==3)
            Spacer(modifier = Modifier.height(12.dp))

            textinput(modifier = Modifier,"Gewicht", viewmodel.gewicht)


        }
        }
    }


@Composable
private fun QuestionTitle(@StringRes title: Int) {
    val backgroundColor = if (MaterialTheme.colors.isLight) {
        MaterialTheme.colors.onSurface.copy(alpha = 0.04f)
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.06f)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp)
        )
    }
}

@Composable
private fun SILO_chooser(
    modifier: Modifier = Modifier,
    SILO_name: String,
    Onclick: ()->Unit,
    enabled: Boolean
){
    var color = if (enabled){outlinedButtonColors(contentColor = MaterialTheme.colors.primary)} else{outlinedButtonColors(contentColor = MaterialTheme.colors.onSurface.copy(0.4f))}
    OutlinedButton(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 3.dp, horizontal = 0.dp),
        onClick=Onclick,
        shape = MaterialTheme.shapes.small,
        colors=color
    ){   Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp),
        text = SILO_name,

        ) }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun textinput(
    modifier: Modifier = Modifier,
    Name: String,
    ID: MutableState<String>

){
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp, horizontal = 0.dp),
                        value = ID.value,
                        onValueChange = { ID.value = it},
                        placeholder = { Text(Name,color = MaterialTheme.colors.onSurface.copy(0.3f))},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape =  MaterialTheme.shapes.small,
                        colors = outlinedTextFieldColors(unfocusedBorderColor =MaterialTheme.colors.onSurface.copy(0.10f) )
                    )


            }



