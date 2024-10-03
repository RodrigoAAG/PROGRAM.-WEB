package com.example.avance_proyecto


import android.widget.CalendarView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.material3.*


// Para colores
import androidx.compose.ui.graphics.Color

// Para tamaños (dp, sp)
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Para los campos de texto
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.TextStyle

// Para el fondo de los elementos
import androidx.compose.foundation.background
import androidx.compose.foundation.border

// Para los íconos y menús
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.avance.ui.theme.AvanceTheme





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AvanceTheme {
                MyApp()
            }
        }
    }
}

// Configura la navegación
@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { MyAppContent(navController) }
        composable("secondScreen") {  segunda_pantalla(navController) }
        composable("notesTasksScreen/{item}") { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item") ?: "Notas/Tareas"
            tercera_pantalla(item, listOf(item), navController)
        }
    }
}





// Pantalla principal
@Composable
fun MyAppContent(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        //icono de búsqueda
        var searchQuery by remember { mutableStateOf("") }
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Buscar")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Seleccionar
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            StatusCard("Todos", 0, Modifier.weight(1f).padding(end = 8.dp), Icons.Default.Home)
            StatusCard("Pendientes", 0, Modifier.weight(1f).padding(start = 8.dp), Icons.Default.Notifications)
        }

        Spacer(modifier = Modifier.height(16.dp))
        StatusCard("Terminados", 0, Modifier.fillMaxWidth(), Icons.Default.Check)
        Spacer(modifier = Modifier.height(16.dp))

        // lista de notas y tareas
        Text("MIS LISTAS", style = MaterialTheme.typography.titleLarge)
        // Lista de Notas
        ListCardWithIcon(
            title = "Notas",
            icon = Icons.Default.Menu,
            items = listOf("Nota 1", "Nota 2", "Nota 3"),
            onItemClick = { item -> navController.navigate("notesTasksScreen/$item") }
        )
        // Lista de Tareas
        ListCardWithIcon(
            title = "Tareas",
            icon = Icons.Default.Menu,
            items = listOf("Tarea 1", "Tarea 2", "Tarea 3"),
            onItemClick = { item -> navController.navigate("notesTasksScreen/$item") }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Botón Agregar nueva
        ListCardWithIcon(
            title = "Agregar nueva",
            icon = Icons.Default.Add, // Ícono de + para el botón
            items = listOf(),
            onClick = { navController.navigate("secondScreen") }  // Navegar a la segunda pantalla
        )
    }
}


@Composable
fun ListCardWithIcon(
    title: String,
    icon: ImageVector,
    items: List<String>,
    onClick: (() -> Unit)? = null,  // Opción de callback para el botón principal
    onItemClick: ((String) -> Unit)? = null // Callback para los ítems
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                if (onClick != null) onClick() else expanded = !expanded
            },
        // Hacer clic en "Agregar nueva" o expandir la lista
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = icon, contentDescription = title)  // Icono de la lista
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(title)
                }
                if (items.isNotEmpty()) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Desplegar")  // Icono de despliegue
                }
            }
            if (expanded && items.isNotEmpty()) {
                Column {
                    items.forEach { item ->
                        Text(
                            text = item,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { onItemClick?.invoke(item) }  // Hacer el ítem clicleable
                        )
                    }
                }
            }
        }
    }
}




@Composable
fun StatusCard(title: String, count: Int, modifier: Modifier = Modifier, icon: ImageVector) {
    Card(
        modifier = modifier.height(120.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ícono a la izquierda
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(32.dp)
                )
                // Número a la derecha
                Text(text = "$count", style = MaterialTheme.typography.headlineMedium)
            }
            // Título centrado en la parte inferior
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}

@Composable
fun ListCard(title: String, items: List<String>) {
    var expanded by remember { mutableStateOf(false) }

    // Hacer que todo el Row sea clickable
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), elevation = CardDefaults.cardElevation(4.dp)) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { expanded = !expanded }, // Click en el título
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(title)
            }
            if (expanded) {
                Column {
                    items.forEach { item ->
                        Text(text = item, modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }
}






//segunda pantalla
@Composable
fun segunda_pantalla(navController: NavController) {
    var selectedListOption by remember { mutableStateOf("Selecciona una lista") }
    var expanded by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("Seleccione una fecha") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Cancelar, Nueva nota, Agregar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Cancelar", color = Color.Black)
            }
            Text(text = "Nueva nota", style = MaterialTheme.typography.titleLarge)
            TextButton(onClick = { }) {
                Text("Agregar", color = Color.Black)
            }
        }

        // Título y Descripción con borde
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))  // Borde alrededor de los campos
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "TITULO", fontSize = 14.sp, color = Color.Black)
                BasicTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color(0xFFE8EAF6)),
                    textStyle = TextStyle(fontSize = 18.sp, color = Color.Black)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Descripción", fontSize = 14.sp, color = Color.Black)
                BasicTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Color(0xFFE8EAF6)),
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selecciona una lista
        ListCardWithIcon(
            title = "   lista",
            icon = Icons.Default.Menu,  // Aquí se usa un ícono para la lista
            items = listOf("Tarea", "Recordatorio"),
            onClick = { }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de íconos con borde alrededor
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))  // Borde alrededor del Box
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.DateRange, contentDescription = "Calendario", tint = Color.Black)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.AttachFile, contentDescription = "Clip", tint = Color.Black)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Cámara", tint = Color.Black)
                }
            }
        }

        // Borde alrededor del calendario y los botones
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp)) // Borde
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Fecha y hora",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                // Calendario
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(16.dp)  //tamañp
                ) {
                    AndroidView(
                        factory = { context ->
                            CalendarView(context).apply {
                                setOnDateChangeListener { _, year, month, dayOfMonth ->
                                    selectedDate = "$dayOfMonth/${month + 1}/$year"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botones Hora y Recordar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .clickable {},
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Schedule, contentDescription = "Hora", tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Hora")
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .clickable {},
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = "Recordar", tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Recordar")
                        }
                    }
                }
            }
        }
    }
}









//tercera pantalla
@Composable
fun tercera_pantalla(title: String, items: List<String>, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título de la lista en la parte superior
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar lista de tareas
        items.forEachIndexed { index, item ->
            TaskItem(title = "Título de la tarea $index", description = "Descripción $index", date = "Fecha $index")
        }

        Spacer(modifier = Modifier.weight(1f))

        // Sección de botones con borde en la parte inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))  // Borde alrededor del Box
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.DateRange, contentDescription = "Calendario")
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.AttachFile, contentDescription = "Editar")
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Cámara")
                }
            }
        }

        // Reducir el espacio extra entre los iconos y el botón de Agregar nueva
        Spacer(modifier = Modifier.height(12.dp))

        // Botón Agregar nueva en la parte inferior
        StyledAddButton(onClick = {})
    }
}

@Composable
fun StyledAddButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)  // Reducir el padding vertical para acercar el botón
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6)) // Fondo
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar nueva", tint = Color.Black) // Ícono +
            Spacer(modifier = Modifier.width(8.dp))
            Text("Agregar nueva", color = Color.Black) // Texto en color negro
        }
    }
}



@Composable
fun TaskItem(title: String, description: String, date: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = false, onClick = {})
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge)
                Text(text = description, style = MaterialTheme.typography.bodySmall)
                Text(text = date, style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sección de imágenes
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Img")
            }
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Img")
            }
        }

        // Añadir línea divisoria debajo de cada tarea
        Divider(
            color = Color.LightGray, // Color de la línea
            thickness = 1.dp,        // Grosor de la línea
            modifier = Modifier.padding(vertical = 8.dp) // Margen alrededor de la línea
        )
    }
}










@Preview(showBackground = true)
@Composable
fun PreviewMyAppContent() {
    AvanceTheme {
        MyApp()
    }
}
