package com.example.appcontrolgastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appcontrolgastos.ui.theme.AppControlGastosTheme
import com.example.appcontrolgastos.navegation.AppNavigation

//FUNCIONES
fun calcularIngresos(movimientos: List<Movimiento>): Double {
    return movimientos.filter{
        it.tipo == TipoMovimiento.INGRESO
    }.sumOf { it.monto }
}
fun calcularEgresos(movimientos: List<Movimiento>): Double {
    return movimientos.filter{
        it.tipo == TipoMovimiento.EGRESO
    }.sumOf { it.monto }
}
fun calcularSaldo(movimientos: List<Movimiento>): Double {
    return calcularIngresos(movimientos) - calcularEgresos(movimientos)
}

class MainActivity : ComponentActivity() {
    // método del ciclo de vida de un Activity, se ejecuar una sola vez cuando la aplicación se crea por primera vez en la memoria
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // permite utilizar las funciones Composable por sobre archivos XML nativo
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}


@Composable
fun PantallaInicio(
    irRegistro: () -> Unit,
    irMovimientos: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Control de gasto",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )

        TarjetaResumen(
            titulo = "INGRESOS",
            monto = 0.0,
            color = Color(0xFFFFBE16)
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        TarjetaResumen(
            titulo = "EGRESOS",
            monto = 0.0,
            color = Color(0xFFFF4665)
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = irRegistro,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar movimiento")
        }
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Button(
            onClick = irMovimientos,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver movimientos")
        }
    }
}

@Composable
fun PantallaRegistro(
    registrarMovimiento: (Movimiento) -> Unit,
    volver: () -> Unit,
    irMovimientos: () -> Unit
) {
    var descripcion by remember {
        mutableStateOf("")
    }
    var monto by remember {
        mutableStateOf("")
    }
    var categoria by remember {
        mutableStateOf("")
    }
    var tipo by remember {
        mutableStateOf(TipoMovimiento.INGRESO)
    }
    var mensajeError by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(20.dp)
    ) {
        Text(
            text = "Registrar movimiento",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                descripcion = it
            },
            label = {
                Text("Descripción")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            singleLine = true
        )
        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = monto,
            onValueChange = {
                monto = it
            },
            label = {
                Text("Monto")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            singleLine = true
        )
        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = categoria,
            onValueChange = {
                categoria = it
            },
            label = {
                Text("Categoría")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            singleLine = true
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Tipo de movimiento",
            fontWeight = FontWeight.Medium
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected =
                    tipo == TipoMovimiento.INGRESO,
                onClick = {
                    tipo = TipoMovimiento.INGRESO
                }
            )
            Text("Ingreso")

            Spacer(
                modifier = Modifier.width(20.dp)
            )

            RadioButton(
                selected =
                    tipo == TipoMovimiento.EGRESO,
                onClick = {
                    tipo = TipoMovimiento.EGRESO
                }
            )
            Text("Egreso")
        }

        if (mensajeError.isNotEmpty()) {
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = {
                val montoNumero =
                    monto
                        .replace(",", ".")
                        .toDoubleOrNull()

                if (descripcion.isBlank()) {
                    mensajeError =
                        "Ingrese una descripción."
                    return@Button
                }
                if (categoria.isBlank()) {
                    mensajeError =
                        "Ingrese una categoría."
                    return@Button
                }
                if (
                    montoNumero == null ||
                    montoNumero <= 0
                ) {
                    mensajeError =
                        "Ingrese un monto válido."
                    return@Button
                }

                val nuevoMovimiento =
                    Movimiento(
                        descripcion = descripcion,
                        monto = montoNumero,
                        categoria = categoria,
                        tipo = tipo
                    )
                registrarMovimiento(
                    nuevoMovimiento
                )

                irMovimientos()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape =
                RoundedCornerShape(20.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        Color(0xFF008F72)
                )
        ) {
            Text(
                text = "AGREGAR",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedButton(
            onClick = volver,
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(20.dp)
        ) {

            Text("CANCELAR")
        }
    }
}

@Composable
fun PantallaMovimientos(
    listMovimientos: List<Movimiento>,
    volver: () -> Unit,
    irRegistro: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Movimientos",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = irRegistro,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Nuevo registro"
            )
        }
        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Button(
            onClick = volver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Volver a inicio"
            )
        }

        if (
            listMovimientos.isEmpty()
        ) {
            Text(
                text =
                    "No hay movimientos registrados"
            )
        } else {
            listMovimientos.forEach {
                    movimiento ->
                Text(
                    text =
                        "${movimiento.descripcion} - ${movimiento.monto} Bs"
                )
            }
        }
    }
}


@Composable
fun TarjetaResumen(
    titulo: String,
    monto: Double,
    color: Color,
    modifier: Modifier = Modifier
    // es un objeto que indica a un componente visual cómo debe comportarte, posicionarse o lucir en la pantalla, permite varias configuraciones como fillMaxWidth()
    // = Modifier está pasando la instancia base/vacía de Modifier que no aplica ningún estilo extra
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            //MONTO CENTRADO
            Text(
                text = "${"%.2f".format(monto)} Bs",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.50f),
                        offset = Offset(4f, 3f),
                        blurRadius = 8f
                    )
                ),
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
            //TÍTULO ABAJO
            Text(
                text = titulo,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(
                    Alignment.BottomEnd
                )
            )
        }
    }
}

@Composable
fun TarjetaSaldo(
    saldo: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF5A58CE)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            //MONTO CENTRADO
            Text(
                text = "${"%.2f".format(saldo)} Bs",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.50f),
                        offset = Offset(4f, 3f),
                        blurRadius = 8f
                    )
                ),
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
            //TÍTULO ABAJO
            Text(
                text = "SALDO TOTAL",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(
                    Alignment.BottomEnd
                )
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppControlGastosTheme {
        Greeting("Android")
    }
}