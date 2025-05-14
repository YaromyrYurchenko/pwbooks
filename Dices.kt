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

// Основная активность приложения
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Включаем режим на весь экран

        // Содержимое экрана
        setContent {
            DicesTheme {  // Применяем тему для всего приложения
                Surface(modifier = Modifier.fillMaxSize()) {  // Задаем основной контейнер
                    DiceApp { message ->  // Передаем функцию для отображения Toast-сообщений
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

// Основной Composable-функция, которая отображает UI
@Composable
fun DiceApp(showToast: (String) -> Unit) {
    // Состояния для всех изменяемых значений (используем remember для сохранения состояния)
    var diceCountText by remember { mutableStateOf("1") }  // Количество кубиков в виде строки
    var diceCount by remember { mutableStateOf(1) }  // Количество кубиков в виде числа

    var sameValues by remember { mutableStateOf(true) }  // Нужно ли, чтобы все кубики имели одинаковые значения
    var oneMaxValue by remember { mutableStateOf("6") }  // Максимальное значение для всех кубиков
    var maxValues by remember { mutableStateOf(listOf("6")) }  // Список максимальных значений для каждого кубика

    var diceResults by remember { mutableStateOf(listOf(1)) }  // Список текущих результатов подброса кубиков
    var previousResults by remember { mutableStateOf(listOf<Int>()) }  // Список предыдущих результатов

    // Основной вертикальный контейнер, в который помещаются все элементы интерфейса
    Column(
        modifier = Modifier
            .fillMaxSize()  // Заполняем весь экран
            .padding(24.dp),  // Добавляем отступы
        horizontalAlignment = Alignment.CenterHorizontally  // Все элементы внутри будут выровнены по центру
    ) {
        // Заголовок
        Text("Dice Simulator", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Поле ввода для количества кубиков
        OutlinedTextField(
            value = diceCountText,  // Текущее значение в поле
            onValueChange = {
                diceCountText = it  // Обновляем значение в поле
                val count = it.toIntOrNull()  // Преобразуем строку в число
                if (count != null && count in 1..5) {  // Если введено число от 1 до 5
                    diceCount = count  // Обновляем количество кубиков
                    diceResults = List(count) { 1 }  // Обновляем список результатов
                    previousResults = List(count) { 1 }  // Обновляем список предыдущих результатов
                    maxValues = List(count) { oneMaxValue }  // Обновляем список максимальных значений
                } else {
                    showToast("Please enter a number between 1 and 5")  // Показываем сообщение, если число не в пределах 1-5
                }
            },
            label = { Text("Number of Dice") },  // Подсказка для пользователя
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),  // Устанавливаем тип клавиатуры для ввода чисел
            modifier = Modifier.width(200.dp)  // Ограничиваем ширину поля ввода
        )

        Spacer(modifier = Modifier.height(12.dp))  // Добавляем отступ

        // Переключатель для включения/выключения одинаковых значений для всех кубиков
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = sameValues, onCheckedChange = {
                sameValues = it  // Обновляем состояние переключателя
            })
            Text("All dice have the same value", modifier = Modifier.padding(start = 8.dp))  // Текст для переключателя
        }

        Spacer(modifier = Modifier.height(12.dp))  // Добавляем отступ

        // Если переключатель включен, то показываем поле для ввода максимального значения для всех кубиков
        if (sameValues) {
            OutlinedTextField(
                value = oneMaxValue,
                onValueChange = {
                    oneMaxValue = it
                    maxValues = List(diceCount) { oneMaxValue }  // Обновляем список значений для всех кубиков
                },
                label = { Text("Maximum Value") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(200.dp)
            )
        } else {
            // Если переключатель выключен, то показываем поле для каждого кубика отдельно
            Column {
                maxValues.forEachIndexed { index, value ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = {
                            maxValues = maxValues.toMutableList().also { list -> list[index] = it }  // Обновляем список значений для каждого кубика
                        },
                        label = { Text("Die ${index + 1}") },  // Метка для каждого кубика
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .width(200.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))  // Добавляем отступ

        // Кнопка для подбрасывания кубиков
        Button(onClick = {
            // Проверяем, что все значения являются числами от 1 до 6
            val parsedValues = maxValues.map { it.toIntOrNull() }
            if (parsedValues.all { it != null && it in 1..6 }) {
                previousResults = diceResults  // Сохраняем текущие результаты как предыдущие
                // Подбрасываем каждый кубик, используя его максимальное значение
                diceResults = parsedValues.map { (1..it!!).random() }
            } else {
                showToast("Enter numbers between 1 and 6")  // Если одно из значений некорректно, показываем сообщение
            }
        }) {
            Text("Roll the Dice")  // Текст на кнопке
        }

        Spacer(modifier = Modifier.height(16.dp))  // Добавляем отступ

        // Отображаем текущие результаты подброса кубиков
        Text("Results:")
        Row(horizontalArrangement = Arrangement.Center) {
            diceResults.forEach { DiceImage(it) }  // Для каждого кубика показываем картинку с результатом
        }

        Spacer(modifier = Modifier.height(24.dp))  // Добавляем отступ

        // Отображаем предыдущие результаты
        Text("Previous Roll:")
        Row(horizontalArrangement = Arrangement.Center) {
            previousResults.forEach { DiceImage(it) }
        }
    }
}

// Composable-функция для отображения картинки кубика
@Composable
fun DiceImage(value: Int) {
    // В зависимости от значения кубика выбираем соответствующую картинку
    val image: Painter = painterResource(
        id = when (value) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
    )

    // Отображаем картинку с размером 64dp и отступами
    Image(
        painter = image,
        contentDescription = "Die $value",  // Описание для экранных читалок
        modifier = Modifier
            .size(64.dp)  // Размер картинки
            .padding(4.dp)  // Отступы
    )
}
