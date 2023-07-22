package com.example.compose.BeanBot.util

import android.os.AsyncTask
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.compose.BeanBot.ManualMode.Manual_SelectionViewModel
import com.example.compose.BeanBot.survey.SelectionViewModel
import kotlinx.coroutines.delay
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.CoroutineContext

import kotlin.random.Random.Default.nextBoolean


/*todo: object of data class of beiden*/
object  Arduino_Communicate{
    var Max_weight = mutableStateOf(  "120")
    var SERVOS = listOf("speed","Servo_glijbaan","vooraan")
    var servo_angles = mutableStateListOf("","","")
    var logbook = mutableStateListOf("")
    fun get_data_servos(ipadress:String="192.168.1.5"): List<String> {
        /* */
        new_log(super.toString(),SERVOS.toString())
        return SERVOS
    }
    fun get_data_dc(ipadress:String="192.168.1.5"):List<String>{
        var DCS = listOf("loopband")
        new_log(super.toString(),DCS.toString())
        return DCS
    }
    fun get_current_ETA(ipadress:String="192.168.1.5"):String{
        return "80"
    }
    fun new_log(orgin:String,message:String){
        logbook.set(0,"${orgin}:${message}\n")
    }

    suspend fun detect_device(Connected: MutableState<Boolean>, vibrator: Vibrator, Ip: String) {
        while (true){
            val thread = Thread {
                try {
                    val connection: HttpURLConnection =
                        URL("http://${Ip}").openConnection() as HttpURLConnection
                    connection.setRequestMethod("HEAD")
                    connection.readTimeout = 200
                    connection.connectTimeout = 200
                    val responseCode: Int = connection.getResponseCode()
                    Connected.value = true
                    if (Connected.value){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            vibrator.vibrate(
                                VibrationEffect.startComposition()
                                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_SLOW_RISE, 0.5f)
                                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_QUICK_FALL, 0.5f)
                                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_TICK, 1.0f, 100)
                                .compose())
                        } else {
                            vibrator.vibrate(200)
                        }
                    }
                }
                catch (e: Exception) {
                    /* e.printStackTrace()*/
                    Connected.value = true

                }
            }
            thread.start()
            delay(1000)
            thread.join()
    }}
    fun RequestSilo( viewmodel: Manual_SelectionViewModel,Ip: String) {
        val thread = Thread {
            try {
                URL("http://${Ip}/CMDS/Startproces/${viewmodel.silo.value}/${viewmodel.gewicht.value}/CMDEND").readText()
                println("http://${Ip}/CMDS/Startproces/${viewmodel.silo.value}/${viewmodel.gewicht.value}/CMDEND")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        thread.start()}
    fun RequestBonen(viewmodel: SelectionViewModel, Ip: String) {
        val thread = Thread {
            try {
                URL("http://${Ip}/CMDS/proces_kleur/${viewmodel.bruine_bonen.value.filter({ it -> it.isDigit() })}/${viewmodel.wite_bonen.value.filter({ it -> it.isDigit() })}/${viewmodel.Zwarte_bonen.value.filter({ it -> it.isDigit() })}/CMDEND").readText()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        thread.start()}
    fun pauze( Ip: String){
        println("pauze")

        val thread = Thread {
            try {
                URL("http://${Ip}/CMDS/PAUZE/CMDEND").readText()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        thread.start()}
    fun reset( Ip: String){
        println("reset")

        val thread = Thread {
            try {
                URL("http://${Ip}/CMDS/RESET/CMDEND").readText()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        thread.start()}
    fun continu(Ip: String){
        println("continu")

        val thread = Thread {
            try {
                URL("http://${Ip}/CMDS/CONTINU/CMDEND").readText()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        thread.start()}
    /*TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!! PROCSES*/

}
