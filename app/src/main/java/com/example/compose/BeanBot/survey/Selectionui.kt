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
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.BeanBot.R

/* creaeert de ui voor de vragen*/
/*todo: EXPIRIMENTELE API GEBRUIKT */
/*todo: REGELE DAK DOOR KAN SCROLLEN ALSK OP ENTER DRUK*/
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Question(
    viewmodel: SelectionViewModel,
    modifier: Modifier = Modifier,


) {
    /*https://proandroiddev.com/the-big-form-with-jetpack-compose-7bec9cde157e*/
    var (bruine_bonen_focus,wite_bonen_focus) = remember({ FocusRequester.createRefs() })
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp)

    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))
            QuestionTitle(R.string.hoeveelheid)
            Spacer(modifier = Modifier.height(24.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = "kies welke bonen en hoeveel",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(bottom = 18.dp, start = 8.dp, end = 8.dp)
                )

                /*todo:dees regel da da auto werkt*/
            }

            textinput(
                modifier = Modifier ,"zwarte bonen", ID = viewmodel.Zwarte_bonen.value,
                onNameChange = { viewmodel.Zwarte_bonen.value = it },bruine_bonen_focus, viewmodel )
            textinput(
                modifier = Modifier,
                Name = "witte bonen", ID = viewmodel.wite_bonen.value ,
                onNameChange = { viewmodel.wite_bonen.value = it },wite_bonen_focus, viewmodel )
            textinput(
                modifier = Modifier,
                Name = "bruine bonen", ID =  viewmodel.bruine_bonen.value ,
                onNameChange = { viewmodel.bruine_bonen.value = it },wite_bonen_focus, viewmodel )

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

@OptIn(ExperimentalComposeUiApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
private fun textinput(
    modifier: Modifier = Modifier,
    Name: String,
    ID: String,
    onNameChange: (String) -> Unit,
    FocusRequester: FocusRequester,
    viewModel: SelectionViewModel
){

                /*bron code fix: https://stackoverflow.com/questions/64181930/request-focus-on-textfield-in-jetpack-compose*/
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth().focusRequester(FocusRequester).padding(vertical = 3.dp, horizontal = 0.dp),
        value = if (ID !=""){"${ID.filter{it.isDigit()}}g" }else{""},
        onValueChange = onNameChange,
        placeholder = { Text(Name)},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape =  MaterialTheme.shapes.small,
        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(
                0.10f)))

            }

