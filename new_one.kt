package com.example.dices

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dices.ui.theme.DicesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DicesTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DiceApp { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
fun DiceApp(showToast: (String) -> Unit) {
    var diceCount by remember { mutableStateOf(1) }
    var sameValues by remember { mutableStateOf(true) }
    var oneMaxValue by remember { mutableStateOf("6") }
    var maxValues by remember { mutableStateOf(listOf("6")) }
    var diceResults by remember { mutableStateOf(listOf(1)) }
    var previousResults by remember { mutableStateOf(listOf<Int>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Symulator Kości", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = diceCount.toString(),
            onValueChange = { input ->
                val count = input.toIntOrNull()
                if (count != null && count in 1..5) {
                    diceCount = count
                    maxValues = List(count) { oneMaxValue }
                    diceResults = List(count) { 1 }
                    previousResults = List(count) { 1 }
                } else {
                    showToast("Podaj liczbę od 1 do 5")
                }
            },
            label = { Text("Ilość kości") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(200.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = sameValues, onCheckedChange = { sameValues = it })
            Text("Wszystkie takie same", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (sameValues) {
            OutlinedTextField(
                value = oneMaxValue,
                onValueChange = { value ->
                    oneMaxValue = value
                    maxValues = List(diceCount) { oneMaxValue }
                },
                label = { Text("Maksymalna wartość") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(200.dp)
            )
        } else {
            Column {
                maxValues.forEachIndexed { index, value ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = { newValue ->
                            maxValues = maxValues.toMutableList().also { list -> list[index] = newValue }
                        },
                        label = { Text("Kość ${index + 1}") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .width(200.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val parsedValues = maxValues.map { it.toIntOrNull() }
            if (parsedValues.all { it != null && it in 1..6 }) {
                previousResults = diceResults
                diceResults = parsedValues.map { (1..it!!).random() }
            } else {
                showToast("Wprowadź liczby od 1 do 6")
            }
        }) {
            Text("Rzuć kośćmi")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Wyniki:")
        Row(horizontalArrangement = Arrangement.Center) {
            diceResults.forEach { DiceImage(it) }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Poprzedni rzut:")
        Row(horizontalArrangement = Arrangement.Center) {
            previousResults.forEach { DiceImage(it) }
        }
    }
}

@Composable
fun DiceImage(value: Int) {
    val image: Painter = painterResource(id = when (value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    })

    Image(
        painter = image,
        contentDescription = "Kość $value",
        modifier = Modifier
            .size(64.dp)
            .padding(4.dp)
    )
}
