package com.example.compose.BeanBot.survey

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.compose.BeanBot.util.Arduino_Communicate
import com.example.compose.BeanBot.util.Arduino_Communicate.continu
import com.example.compose.BeanBot.util.Arduino_Communicate.pauze
import com.example.compose.BeanBot.util.Arduino_Communicate.reset
import com.example.compose.BeanBot.util.supportWideScreen
import kotlinx.coroutines.delay
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun Procses_screen(oncancelPressed: () -> Unit, viewModel: ProcssViewModel) {


    var ETA = remember { mutableStateOf(Arduino_Communicate.get_current_ETA())}
    var procses_state = remember { mutableStateOf(true)}
    var current_weight = remember { mutableStateOf("-")}

    var seconde= remember{mutableStateOf(0)}
    /*IPADRESS CALL¨*/
    val  Ip = viewModel.ipadress

    LaunchedEffect(key1 = procses_state.value,key2 =current_weight.value,key3 = seconde) {

        /* todo zien of da in 2 efficienter i dan in 1*/
            seconde.value +=1
            println("ip")

             println(Ip)
            if  (procses_state.value ){
                delay(100)
                val thread = Thread {
                    try {
                        val url = URL("http://${Ip}")

                        val http: HttpURLConnection = url.openConnection() as HttpURLConnection
                        http.setRequestProperty("Accept", "*/*")
                        println(http.inputStream.read())
                        println(http.getResponseCode().toString() + " " + http.getResponseMessage())
                        http.disconnect()



                    } catch (e: Exception) {
                        ETA.value = "not ready"

                        e.printStackTrace()
                    }
                }
                thread.start()}



    }
    Surface(modifier = Modifier.supportWideScreen()) {
        Scaffold(
            topBar = {
                AppBar(
                    modifier = Modifier
                    .padding(vertical = 20.dp),ETA)
            },
            content = {
                Column(modifier = Modifier.fillMaxHeight(),verticalArrangement = Arrangement.SpaceBetween){
                    Proces_Body(modifier = Modifier,current_weight)
                state_button(
                    modifier = Modifier.align(Alignment.End),oncancelPressed,
                    { procses_state.value = !procses_state.value },procses_state,Ip)}
            },
            bottomBar = {
            }
        )
    }}



@Composable
private fun Proces_Body(modifier: Modifier, current_weight: MutableState<String>){
    /* https://www.geeksforgeeks.org/how-to-create-a-timer-using-jetpack-compose-in-android/ */

        Column(modifier = Modifier
            .padding(vertical = 24.dp, horizontal = 24.dp)
            .shadow(10.dp, shape = MaterialTheme.shapes.large)
            .background(MaterialTheme.colors.background, shape = MaterialTheme.shapes.large)
            .border(
                width = 8.dp,
                color = MaterialTheme.colors.primary,
                shape = MaterialTheme.shapes.large
            )
            .height(350.dp) ,
                    verticalArrangement = Arrangement.Center)
             {
                 Box(
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(horizontal = 10.dp, vertical = 10.dp),
                 ){

                     Text(
                        text = "${current_weight.value}g",
                        style = MaterialTheme.typography.h1,
                         fontWeight = FontWeight.Bold,
                         modifier = Modifier.align(Center)
                 )}
        }}

@Composable
private fun AppBar(

    modifier: Modifier = Modifier,
    ETA: MutableState<String>
) {

    Column(modifier = modifier){
    Box(modifier = Modifier.fillMaxWidth()) {

    val indexStyle = MaterialTheme.typography.caption.toSpanStyle().copy(
        fontWeight = FontWeight.Bold
    )
    val text = buildAnnotatedString {
        withStyle(style = indexStyle) {
            append("${ETA.value}")
        }
    }
    Text(
        text = text,
        style = MaterialTheme.typography.caption,
        modifier = modifier.align(Alignment.Center)
    )
    }
}
}
@Composable
private fun state_button(

    modifier: Modifier = Modifier,
    oncancelPressed:  () ->Unit?,
    onPauzePressed: () -> Unit,
    Procses_State: MutableState<Boolean>,
    Ip:String

    ) {
    if (Procses_State.value) {pauze(Ip)} else {continu(Ip)}
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { reset(Ip); },

                    )
            },
        onClick = { onPauzePressed() },
    )
        {   Text(
            text = if (Procses_State.value) {"Pauze"} else {"Start"})}

            }

