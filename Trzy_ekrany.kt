package com.example.trzyekrany

// Импортируем необходимые компоненты для использования в нашем приложении.
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.* // Для использования layout-компонентов (например, Column)
import androidx.compose.material3.* // Для использования компонентов Material (например, Button, TextField)
import androidx.compose.runtime.* // Для использования состояния в Compose
import androidx.compose.ui.Modifier // Для применения модификаторов, например, для изменения размера
import androidx.compose.ui.tooling.preview.Preview // Для использования превью в Android Studio
import androidx.compose.ui.text.input.TextFieldValue // Для работы с текстовыми полями в Compose
import com.example.trzyekrany.ui.theme.TrzyEkranyTheme // Тема для оформления приложения
import androidx.compose.ui.unit.dp // Для использования размеров в dp (density-independent pixels)

class MainActivity : ComponentActivity() {
    // Переопределяем метод onCreate, который вызывается при создании активности
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Устанавливаем содержимое экрана, используя Jetpack Compose
        setContent {
            // Применяем тему к нашему приложению
            TrzyEkranyTheme {
                // Состояния для управления текущим экраном и ввода данных
                var screen by remember { mutableStateOf(1) } // Номер текущего экрана (1, 2 или 3)
                var input1 by remember { mutableStateOf(TextFieldValue("")) } // Ввод на первом экране
                var input2 by remember { mutableStateOf(TextFieldValue("")) } // Ввод на втором экране

                // В зависимости от значения переменной screen отображаем соответствующий экран
                when (screen) {
                    1 -> Screen1(input1, { input1 = it }, { screen = 2 }) // Экран 1, передаем состояние и действия
                    2 -> Screen2(input1.text, input2, { input2 = it }, { screen = 3 }) // Экран 2
                    3 -> Screen3(input1.text, input2.text) // Экран 3
                }
            }
        }
    }
}

// Экран 1, с полем ввода и кнопкой для перехода на экран 2
@Composable
fun Screen1(input: TextFieldValue, onInputChanged: (TextFieldValue) -> Unit, onNext: () -> Unit) {
    // Разметка экрана 1
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Текстовое поле для ввода данных на экране 1
        TextField(
            value = input, // Значение текстового поля
            onValueChange = onInputChanged, // Обработчик изменения текста
            label = { Text("Screen 1") } // Метка для поля ввода
        )
        Spacer(modifier = Modifier.height(16.dp)) // Добавляем отступ между элементами
        // Кнопка для перехода на экран 2
        Button(onClick = onNext) { Text("Move to screen 2") }
    }
}

// Экран 2, где отображается ввод с экрана 1 и новое поле ввода для второго значения
@Composable
fun Screen2(input1: String, input2: TextFieldValue, onInputChanged: (TextFieldValue) -> Unit, onNext: () -> Unit) {
    // Разметка экрана 2
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Показываем значение, введенное на экране 1
        Text("Value from screen 1: $input1")
        Spacer(modifier = Modifier.height(16.dp)) // Отступ
        // Текстовое поле для ввода на экране 2
        TextField(
            value = input2, // Значение текстового поля
            onValueChange = onInputChanged, // Обработчик изменения текста
            label = { Text("Screen 2") } // Метка для поля ввода
        )
        Spacer(modifier = Modifier.height(16.dp)) // Отступ
        // Кнопка для перехода на экран 3
        Button(onClick = onNext) { Text("Move to screen 3") }
    }
}

// Экран 3, который отображает данные с экранов 1 и 2
@Composable
fun Screen3(input1: String, input2: String) {
    // Разметка экрана 3
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Показываем введенные данные с экрана 1
        Text("Value from screen 1: $input1")
        Spacer(modifier = Modifier.height(16.dp)) // Отступ
        // Показываем введенные данные с экрана 2
        Text("Value from screen 2: $input2")
    }
}

// Превью для отображения экрана в редакторе Android Studio
@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    TrzyEkranyTheme {
        // Отображаем только первый экран в превью
        Screen1(TextFieldValue(""), {}, {})
    }
}
