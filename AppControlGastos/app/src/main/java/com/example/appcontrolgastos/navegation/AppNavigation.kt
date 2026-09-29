package com.example.appcontrolgastos.navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appcontrolgastos.PantallaInicio
import com.example.appcontrolgastos.PantallaMovimientos
import com.example.appcontrolgastos.PantallaRegistro
import com.example.appcontrolgastos.model.Movimiento

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val movimientos = remember { mutableStateListOf<Movimiento>() }
    val movimientoSeleccionado by remember { mutableStateOf<Movimiento?>(null) }

    NavHost (
        navController = navController,
        startDestination = "inicio"
    ){
        composable ("inicio") {
            PantallaInicio(
                irRegistro = {
                    navController.navigate("registro")
                },
                irMovimientos = {
                    navController.navigate("movimientos")
                }
            )
        }

        composable ("registro") {
            PantallaRegistro(
                registrarMovimiento = {
                        movimiento ->
                    movimientos.add(movimiento)
                },
                volver = {
                    navController.popBackStack()
                },
                irMovimientos = {
                    navController.navigate("movimientos") {
                        popUpTo("inicio")
                    }
                }
            )
        }

        composable ("movimientos") {
            PantallaMovimientos(
                listMovimientos = movimientos,
                volver = {
                    navController.popBackStack()
                },
                irRegistro = {
                    navController.navigate("registro")
                }
            )
        }

        composable ("detalle"){}
    }
}