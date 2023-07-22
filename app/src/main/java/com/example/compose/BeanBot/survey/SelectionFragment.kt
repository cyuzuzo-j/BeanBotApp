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
import com.example.compose.BeanBot.R
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.example.compose.BeanBot.Screen
import com.example.compose.BeanBot.navigate
import com.example.compose.BeanBot.theme.AppTheme
import com.example.compose.BeanBot.util.Arduino_Communicate

class SelectionFragment : Fragment() {

    private val viewModel: SelectionViewModel by viewModels {
        SelectionViewModelFactory()


    }
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.navigateTo.observe(viewLifecycleOwner) { navigateToEvent ->
            navigateToEvent.getContentIfNotHandled()?.let { navigateTo ->

                navigate(navigateTo, Screen.Survey)

            }


        }
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val ipadress = sharedPref?.getString(getString(R.string.Ipadress), getString(R.string.nrmlIP))
        if (ipadress != null) {
            viewModel.ipadress = ipadress
        }
        else{
            viewModel.ipadress =   getString(R.string.nrmlIP)

        }
        println(viewModel.ipadress)
        return ComposeView(requireContext()).apply {
            id = R.id.survey_fragment

            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT

            )

            setContent {
                AppTheme {
                    /* via manier adere code doen */

                            SurveyQuestionsScreen(
                                onDonePressed = { println("z") },
                                onBackPressed = {activity?.onBackPressedDispatcher?.onBackPressed()},
                                /* todo :regelen dat de backbutton werkt*/
                                onEvent = { viewModel.procses()

                                },
                                viewmodel = viewModel
                            )
                        }
                    }

            }
        }}

