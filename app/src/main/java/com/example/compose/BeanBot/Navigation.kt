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

package com.example.compose.BeanBot

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.security.InvalidParameterException

enum class Screen { Welcome, Survey, debug,Proces,Splash,Manual_Survey }

fun Fragment.navigate(to: Screen, from: Screen) {
    if (to == from) {
        throw InvalidParameterException("Can't navigate to $to")
    }
    when (to) {
        Screen.Splash -> {
            findNavController().navigate(R.id.welcome_fragment)
        }
        Screen.Welcome -> {
            findNavController().navigate(R.id.welcome_fragment)
        }
        Screen.Survey -> {
            findNavController().navigate(R.id.SelectionFragment)
        }
        Screen.Manual_Survey -> {
            findNavController().navigate(R.id.Manual_SelectionFragment)
        }
        Screen.debug -> {
            findNavController().navigate(R.id.debug_fragment)
        }
        Screen.Proces -> {
            findNavController().navigate(R.id.Procses_Fragment)
        }
    }
}
