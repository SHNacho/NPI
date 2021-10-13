package com.example.NPI

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.NPI.ui.theme.ComposeAppTheme

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                        login()
                }
            }
        }
    }

    // Login de la aplicación
    @ExperimentalAnimationApi
    @Composable
    fun login(){
        // Las variables tipo remember se usan para que se recargue la
        // vista cuando se modifican (o algo así)
        var usuario by remember { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("")}
        var LogErrorVisivility by remember { mutableStateOf(false)}

        // Creamos una columan que ocupa todo el espacio disponible
        // Su conternido está centrado vertical y horizontalmente
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center ,horizontalAlignment = Alignment.CenterHorizontally) {

            // El primer elemento de la columna es un cuadro de texto para el usuario
            // Si lo quereos sin el borde de fuera podemos usar solo TextField
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it; LogErrorVisivility = false   },
                label = { Text(text = "User") },
                placeholder = { Text(text = "User")},
            )
            // El segundo elemento es un cuadro de texto para la contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; LogErrorVisivility = false },
                label = { Text(text = "Password") },
                placeholder = { Text(text = "Password")},
                visualTransformation = PasswordVisualTransformation()
            )

            // El tercer elemento es un botón
            // Si el usuario y contraseña son correctos nos lleva a otra actividad
            // mediante la función sendMessage
            Button(
                onClick = {
                          if (usuario == "user" && password == "1234"){
                              sendMessage()
                          }
                          else{ // Si fallamos en la contraseña nos muestra un mensaje de error
                            LogErrorVisivility = true
                          }
                },
                modifier = Modifier
                    .padding(20.dp)
                    .width(100.dp)
            ) {
                Text(text = "Log In", fontSize = 18.sp)
            }

            // El último elemento es un mensaje que solo se muestra si fallamos en el
            // login
            AnimatedVisibility(
                visible = LogErrorVisivility,
                enter = fadeIn()
            ) {
                Text(text = "Usuario o cantraseña incorrectos")
            }
        }
    }

    // Esta función simplemente permite que podamos ver una vista previa
    // del diseño
    @ExperimentalAnimationApi
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ComposeAppTheme {
            login()
        }
    }

    // Se llama cuando el usuario pulsa el botón de LogIn
    fun sendMessage() {
        val intent = Intent(this, OtraActividad::class.java).apply {  }
        startActivity(intent)
    }
}

//@Composable
//fun Greeting(name: String){
//    Text(text = "Hello $name")
//    Button(onClick = {sendMessage()}) {
//        Text(text = "Pulsa aquí")
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    ComposeAppTheme {
//        Greeting("Android")
//    }
//}