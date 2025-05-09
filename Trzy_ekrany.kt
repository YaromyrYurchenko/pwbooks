package com.example.trzyekrany

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.TextFieldValue
import com.example.trzyekrany.ui.theme.TrzyEkranyTheme
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrzyEkranyTheme {
                var screen by remember { mutableStateOf(1) }
                var input1 by remember { mutableStateOf(TextFieldValue("")) }
                var input2 by remember { mutableStateOf(TextFieldValue("")) }

                when (screen) {
                    1 -> Screen1(input1, { input1 = it }, { screen = 2 })
                    2 -> Screen2(input1.text, input2, { input2 = it }, { screen = 3 })
                    3 -> Screen3(input1.text, input2.text)
                }
            }
        }
    }
}

@Composable
fun Screen1(input: TextFieldValue, onInputChanged: (TextFieldValue) -> Unit, onNext: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = input,
            onValueChange = onInputChanged,
            label = { Text("Screen 1") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNext) { Text("Move to screen 2") }
    }
}

@Composable
fun Screen2(input1: String, input2: TextFieldValue, onInputChanged: (TextFieldValue) -> Unit, onNext: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Value from screen 1: $input1")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = input2,
            onValueChange = onInputChanged,
            label = { Text("Screen 2") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNext) { Text("Move to screen 3") }
    }
}

@Composable
fun Screen3(input1: String, input2: String) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Value from screen 1: $input1")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Value from screen 2: $input2")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    TrzyEkranyTheme {
        Screen1(TextFieldValue(""), {}, {})
    }
}
