package com.example.NPI

import NPI.R
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.NPI.ui.theme.ComposeAppTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material.Icon
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Book
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.math.sqrt

class OtraActividad : ComponentActivity() {

    var mSensorManager: SensorManager? = null
    var mAccel // acceleration apart from gravity
            = 0f
    var mAccelCurrent // current acceleration including gravity
            = 0f
     var mAccelLast // last acceleration including gravity
            = 0f

    private val mSensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(se: SensorEvent) {
            val x = se.values[0]
            val y = se.values[1]
            val z = se.values[2]
            mAccelLast = mAccelCurrent
            mAccelCurrent = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta = mAccelCurrent - mAccelLast
            mAccel = mAccel * 0.9f + delta // perform low-cut filter
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(
            mSensorListener,
            mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        mSensorManager!!.unregisterListener(mSensorListener)
        super.onPause()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        /* do this in onCreate */
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorManager!!.registerListener(mSensorListener, mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAppTheme {
                MainScreen()
            }
        }
    }


    sealed class BottomNavigationScreens(val route: String, @StringRes val resourceId: Int, val icon: ImageVector){
        object Home : BottomNavigationScreens("Inicio", R.string.home_route, Icons.Filled.Home)
        object Schedule : BottomNavigationScreens("Horario", R.string.schedule_route,Icons.Filled.CalendarToday)
        object Biblioteca : BottomNavigationScreens("Biblioteca", R.string.biblioteca_route, Icons.Filled.Book)
        object Asistencia : BottomNavigationScreens("Asistencia", R.string.asistencia_route, Icons.Filled.AssignmentInd)
    }

    // En Main Screen vamos a tener la barra de navegación anterior y también iremos incorporando cada uno de los
// submenús.
    @Preview
    @Composable
    fun MainScreen() {
        // Guardamos el estado en una variable navController
        val navController = rememberNavController()

        // Aquí añadimos todos los objetos que aparecerán en el submenú inferior.
        val bottomNavigationItems = listOf(
            BottomNavigationScreens.Home,
            BottomNavigationScreens.Schedule,
            BottomNavigationScreens.Biblioteca,
            BottomNavigationScreens.Asistencia
        )

        // Creamos un "Scaffold" o "Armazón" con forma de barra inferior.
        Scaffold(
            bottomBar = { AppBottomNavigation(navController = navController, items = bottomNavigationItems)}
        ){
            MainScreenNavigationConfigurations(navController)
        }
    }

    @Composable
    private fun AppBottomNavigation(
        navController: NavHostController,
        items: List<BottomNavigationScreens>
    ) {
        BottomNavigation {
            val currentRoute = currentRoute(navController = navController)
            items.forEach { screen ->
                BottomNavigationItem(
                    icon = { Icon(imageVector = screen.icon, contentDescription = "") },
                    label = { Text(stringResource(id = screen.resourceId)) },
                    selected = currentRoute == screen.route,
                    // Esta es la interacción a HACER para moverse entre pantallas.
                    onClick = {
                        if(currentRoute != screen.route){
                            navController.navigate(screen.route)
                        }
                    },
                    alwaysShowLabel = true
                )
            }
        }
    }

    @Composable
// Coger la ruta actual. Usamos un método de navBackStackEntry
    private fun currentRoute(navController: NavHostController): String? {
        //  Conseguir el estado actual y guardarlo en una variable.
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        // Devolver la ruta.
        return navBackStackEntry?.destination?.route
    }


    @Composable
// Aquí le damos al sistema la información de a donde debería de ir dada una ruta.
    private fun MainScreenNavigationConfigurations(
        navController: NavHostController
    ) {
        NavHost(navController, startDestination = BottomNavigationScreens.Home.route) {

            composable(BottomNavigationScreens.Home.route) {
                HomeScreen()
            }
            composable(BottomNavigationScreens.Schedule.route) {
                //CalendarioScreen()
            }
            composable(BottomNavigationScreens.Biblioteca.route) {
                //CalendarioScreen()
            }
            composable(BottomNavigationScreens.Asistencia.route) {
                //CalendarioScreen()
            }
        }
    }

    //A partir de aquí, diseñaremos en la misma actividad todas las sub-vistas.
    @Composable
    fun HomeScreen(
    ) {
        if (mAccel > 12) {
            val toast = Toast.makeText(applicationContext, "Device has shaken.", Toast.LENGTH_LONG)
            toast.show()
        }
        Text(text = "Hola")
    }

}
