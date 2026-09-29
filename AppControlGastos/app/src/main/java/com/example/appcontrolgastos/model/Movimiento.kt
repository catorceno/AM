package com.example.appcontrolgastos.model

data class Movimiento(
    val descripcion: String,
    val monto: Double,
    val categoria: String,
    val tipo: TipoMovimiento
)