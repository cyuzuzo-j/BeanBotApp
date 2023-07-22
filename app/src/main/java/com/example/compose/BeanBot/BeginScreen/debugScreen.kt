import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.compose.BeanBot.BeginScreen.DebugViewModel
import com.example.compose.BeanBot.R
import com.example.compose.BeanBot.util.Arduino_Communicate
import com.example.compose.BeanBot.util.supportWideScreen
import java.net.URL

/* crash log LayoutCoordinate operations are only valid when isAttached is true*/
@Composable
fun debug_screen(viewModel: DebugViewModel) {
    Surface(modifier = Modifier.supportWideScreen()) {
        var logbook = remember { Arduino_Communicate.logbook}
        var Arduino = Arduino_Communicate
        var Servos = remember { mutableStateOf(Arduino.get_data_servos()) }
        var DCS = remember  { mutableStateOf(Arduino.get_data_dc()) }
        /*IPADRES CALLLLL*/
        val  Ip = stringResource(id = R.string.Ipadress)

        Scaffold(
            topBar = {
                AppBar(modifier = Modifier
                    .padding(top = 20.dp))
            },
            content = {
                /*https://stackoverflow.com/questions/4389480/print-array-without-brackets-and-commas*/
                debug_Body(modifier = Modifier.padding(bottom=it.calculateBottomPadding()),logbook.joinToString { "${it} \n" }.replace(",", "") )
            },
            bottomBar = {
                Surface(
                    elevation = 7.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ){Column(modifier = Modifier
                    .height(200.dp)
                    .verticalScroll(rememberScrollState())){

                    Servos.value.forEachIndexed{ Servo_index,servo->Servoinput(modifier = Modifier, Name = servo, angle = viewModel.arduino_servos_angles[Servo_index],Onchange = {logbook.add(0,"${servo} is op ${it} graden gezet \n") ; viewModel.arduino_servos_angles[Servo_index] = it ;
                        val thread = Thread {
                            try {
                                logbook.add(0,"Respose: ${URL("http://${Ip}/${servo}?angle= ${it}").readText()}")
                            } catch (e: Exception) {
                                logbook.add(0,e.stackTraceToString())
                            }
                        }
                        thread.start()})}
                    DCS.value.forEach{Dcmotor->Dccontrol_button(Modifier,Dcmotor,Onclick = { logbook.add(0,"${Dcmotor} is getriggerd\n");
                        val thread = Thread {
                        try {
                            logbook.add(0,"Response: ${URL("http://${Ip}/${Dcmotor}").readText()}")

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                        thread.start()})}

                }}
            }
        )
    }}



@Composable
private fun debug_Body(
    modifier: Modifier,
    logbook:String,
){
    Column(modifier = modifier.padding( horizontal = 16.dp,vertical = 16.dp)){
        val backgroundColor = if (MaterialTheme.colors.isLight) {
            MaterialTheme.colors.onSurface.copy(alpha = 0.04f)
        } else {
            MaterialTheme.colors.onSurface.copy(alpha = 0.06f)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .background(
                    color = backgroundColor,
                    shape = MaterialTheme.shapes.small
                )
        ) {
            Column(modifier = Modifier)
             {
                Text(
                    text = logbook,
                    style = MaterialTheme.typography.subtitle1,
                    overflow = TextOverflow.Visible,
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState(), enabled = true)
                        .height(450.dp)
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                )
            }

        }

    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Servoinput(
    modifier: Modifier = Modifier,
    Name: String,
    angle: String,
    Onchange: (String) -> Unit,
){
    Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    modifier = modifier.fillMaxWidth(),
                    value = angle,
                    onValueChange =  Onchange,
                    placeholder = { Text(Name)},

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }}



@Composable
private fun AppBar(

    modifier: Modifier = Modifier
) {   Column(modifier = modifier){
    Box(modifier = Modifier.fillMaxWidth()) {

    val indexStyle = MaterialTheme.typography.caption.toSpanStyle().copy(
        fontWeight = FontWeight.Bold
    )
    val text = buildAnnotatedString {
        withStyle(style = indexStyle) {
            append("debug screen")
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
private fun Dccontrol_button(
    modifier: Modifier = Modifier,
    Dcmotor_name: String,
    Onclick: ()->Unit,
){
    OutlinedButton(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 3.dp, horizontal = 16.dp),onClick=Onclick){   Text(
        text = Dcmotor_name,

    ) }

}
