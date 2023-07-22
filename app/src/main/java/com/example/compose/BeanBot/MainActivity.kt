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

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.compose.BeanBot.util.Event
import com.google.android.material.navigation.NavigationView
import java.net.URL
import java.security.AccessController.getContext
import androidx.core.content.pm.ShortcutManagerCompat

import android.R.id

import androidx.core.content.pm.ShortcutInfoCompat



/*https://developers.google.com/assistant/app/dynamic-shortcuts*/
/*https://sonique6784.medium.com/integrating-your-android-app-with-google-assistant-cb97f54c5fc8*/
/*https://developers.google.com/assistant/app/custom-intents#shortcuts.xml*/
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        connectToWifiWPA2(getString(R.string.ssid),getString(R.string.pswd),getApplicationContext())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        val SiloOfChoice = intent.data?.getQueryParameter("item_name")
        val NumbaOfgrams = intent.data?.getQueryParameter("number_of_items")
        val intent = Intent(this@MainActivity, MainActivity::class.java)
        intent.setPackage("com.BeanBOT.compose.BeanBot")
        intent.action = Intent.ACTION_VIEW
        println(SiloOfChoice)
        println(NumbaOfgrams)
        if ((NumbaOfgrams != null) && (SiloOfChoice != null)){

                val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
                val ipadress = sharedPref?.getString(getString(R.string.Ipadress), getString(R.string.nrmlIP))
                val thread = Thread {
                    try {

                        URL("http://${ipadress}/CMDS/Startproces/${SiloOfChoice}/${NumbaOfgrams}/CMDEND").readText() }
                    catch (e: Exception) {
                        e.printStackTrace()
                        }
                }

                thread.start()

                println(ipadress)
                navController.navigate(R.id.Procses_Fragment)

            }


    }



}
/*https://stackoverflow.com/questions/47466211/using-wifimanager-to-connect-to-a-wireless-network-wpa2*/
private fun connectToWifiWPA2(
    networkSSID: String,
    networkPassword: String,
    applicationContext: Context,
) {
    val conf = WifiConfiguration()
    conf.SSID = "\"" + networkSSID + "\""
    conf.preSharedKey = "\"" + networkPassword + "\""
    val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager?
    conf.status = WifiConfiguration.Status.ENABLED
    conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
    conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
    conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
    conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
    conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
    conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
    val netId = wifiManager!!.addNetwork(conf)
    wifiManager.disconnect()
    wifiManager.enableNetwork(netId, true)
    wifiManager.reconnect()
}


