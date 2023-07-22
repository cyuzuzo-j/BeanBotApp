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
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.compose.BeanBot.R
import com.example.compose.BeanBot.Screen
import com.example.compose.BeanBot.navigate
import com.example.compose.BeanBot.theme.AppTheme

/**
 * Fragment containing the welcome UI.
 */
class WelcomeFragment : Fragment() {

    private val viewModel: WelcomeViewModel by viewModels { WelcomeViewModelFactory() }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflater = TransitionInflater.from(requireContext())

        enterTransition = inflater.inflateTransition(R.transition.fade)
        viewModel.navigateTo.observe(viewLifecycleOwner) { navigateToEvent ->
            navigateToEvent.getContentIfNotHandled()?.let { navigateTo ->
                navigate(navigateTo, Screen.Welcome)
            }

        }

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val ipadress = sharedPref?.getString("featureRequested", getString(R.string.nrmlIP))
        println(ipadress)
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    WelcomeScreen(
                        onEvent = { event ->
                            when (event) {
                                WelcomeEvent.StartSurvey -> viewModel.Start()
                                WelcomeEvent.startDebug -> viewModel.Debug()

                            }
                        },
                    View = view)
                    }

                }
            }
        }
    }


