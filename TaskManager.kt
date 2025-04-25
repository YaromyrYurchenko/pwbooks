// Определяем главный пакет нашего приложения
package com.example.task2

// Импортируем необходимые библиотеки для работы с Android и Compose
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task2.ui.theme.Task2Theme

// Определяем класс MainActivity, который является точкой входа в наше приложение
class MainActivity : ComponentActivity() {
    // Переопределяем метод onCreate, который вызывается при запуске активности
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Устанавливаем содержимое экрана с помощью Jetpack Compose
        setContent {
            // Оборачиваем приложение в тему, чтобы применить стили
            Task2Theme {
                // Отображаем наш компонент ActivityTracker
                ActivityTracker()
            }
        }
    }
}

// Основной составной элемент, который отображает экран с активностью
@Composable
fun ActivityTracker() {
    // Состояние для ввода имени активности
    var activityName by remember { mutableStateOf(TextFieldValue("")) }
    // Состояние для ввода длительности активности
    var durationInput by remember { mutableStateOf(TextFieldValue("")) }
    // Список для хранения пар "имя активности" и "длительность"
    val activityList = remember { mutableStateListOf<Pair<String, Int>>() }

    // Считаем общее количество минут из списка активностей
    val totalMinutes = activityList.sumOf { it.second }
    // Переводим общее количество минут в часы и минуты
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    // Основной контейнер с вертикальным расположением элементов
    Column(
        modifier = Modifier
            .fillMaxSize() // Заполняем весь экран
            .padding(24.dp) // Добавляем отступы
    ) {
        // Заголовок экрана
        Text(
            text = "Activity Tracker", // Текст заголовка
            style = MaterialTheme.typography.headlineSmall, // Стиль текста
            modifier = Modifier.padding(bottom = 16.dp) // Отступ снизу
        )

        // Поле ввода для имени активности
        OutlinedTextField(
            value = activityName, // Состояние поля
            onValueChange = { activityName = it }, // Обработчик изменения текста
            label = { Text("Activity Name") }, // Надпись на поле
            modifier = Modifier.fillMaxWidth() // Поле занимает всю ширину
        )

        Spacer(modifier = Modifier.height(8.dp)) // Пробел между полями

        // Поле ввода для длительности активности
        OutlinedTextField(
            value = durationInput, // Состояние поля
            onValueChange = { durationInput = it }, // Обработчик изменения текста
            label = { Text("Duration (min, hh:mm, or hh.mm)") }, // Надпись на поле
            modifier = Modifier.fillMaxWidth() // Поле занимает всю ширину
        )

        Spacer(modifier = Modifier.height(12.dp)) // Пробел между полями

        // Контейнер с двумя кнопками
        Row(
            modifier = Modifier.fillMaxWidth(), // Контейнер занимает всю ширину
            horizontalArrangement = Arrangement.SpaceEvenly // Равномерное распределение кнопок
        ) {
            // Кнопка для добавления активности
            Button(
                onClick = {
                    // Получаем имя и длительность активности
                    val name = activityName.text.trim()
                    val duration = parseDuration(durationInput.text.trim())
                    // Если введены корректные данные, добавляем активность в список
                    if (name.isNotEmpty() && duration != null && duration > 0) {
                        activityList.add(name to duration)
                        // Очищаем поля ввода после добавления
                        activityName = TextFieldValue("")
                        durationInput = TextFieldValue("")
                    }
                },
                modifier = Modifier.weight(1f) // Кнопка занимает 50% ширины
            ) {
                Text("Add") // Текст на кнопке
            }

            Spacer(modifier = Modifier.width(12.dp)) // Пробел между кнопками

            // Кнопка для удаления всех активностей
            Button(
                onClick = { activityList.clear() }, // Очищаем список
                modifier = Modifier.weight(1f), // Кнопка занимает 50% ширины
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error) // Цвет кнопки
            ) {
                Text("Rem.") // Текст на кнопке
            }
        }

        Spacer(modifier = Modifier.height(24.dp)) // Пробел перед следующей секцией

        // Заголовок для статистики
        Text(
            text = "Summary", // Текст заголовка
            style = MaterialTheme.typography.titleMedium, // Стиль текста
            modifier = Modifier.padding(bottom = 4.dp) // Отступ снизу
        )

        // Текст с общей статистикой
        Text(
            text = "${activityList.size} activities, $hours h $minutes min ($totalMinutes min)", // Сводка по активности
            style = MaterialTheme.typography.bodyLarge // Стиль текста
        )

        Spacer(modifier = Modifier.height(16.dp)) // Пробел между статистикой и списком

        // Список всех добавленных активностей
        LazyColumn(
            modifier = Modifier.fillMaxSize() // Список занимает весь экран
        ) {
            items(activityList) { item ->
                // Карточка для каждой активности
                Card(
                    modifier = Modifier
                        .fillMaxWidth() // Карточка занимает всю ширину
                        .padding(vertical = 4.dp), // Отступы по вертикали
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Цвет фона карточки
                ) {
                    // Горизонтальное расположение элементов внутри карточки
                    Row(
                        modifier = Modifier
                            .fillMaxWidth() // Строка занимает всю ширину
                            .padding(12.dp), // Отступы внутри строки
                        horizontalArrangement = Arrangement.SpaceBetween, // Равномерное распределение
                        verticalAlignment = Alignment.CenterVertically // Выравнивание по вертикали
                    ) {
                        // Название активности
                        Text(text = item.first, fontSize = 16.sp)
                        // Длительность активности
                        Text(text = "${item.second} min", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

// Функция для парсинга введенной длительности (минуты или время в формате hh:mm или hh.mm)
fun parseDuration(input: String): Int? {
    return try {
        when {
            // Если ввод содержит двоеточие (например, 01:30)
            input.contains(":") -> {
                val parts = input.split(":") // Разделяем на части
                if (parts.size == 2) {
                    val hours = parts[0].toInt() // Часы
                    val minutes = parts[1].toInt() // Минуты
                    if (hours >= 0 && minutes in 0..59) { // Проверяем, что значения валидны
                        hours * 60 + minutes // Возвращаем длительность в минутах
                    } else null
                } else null
            }

            // Если ввод содержит точку (например, 1.30)
            input.contains(".") -> {
                val parts = input.split(".") // Разделяем на части
                if (parts.size == 2) {
                    val hours = parts[0].toInt() // Часы
                    val minutes = parts[1].toInt() // Минуты
                    if (hours >= 0 && minutes in 0..59) { // Проверяем, что значения валидны
                        hours * 60 + minutes // Возвращаем длительность в минутах
                    } else null
                } else null
            }

            // Если ввод состоит только из чисел (например, 45)
            else -> {
                val mins = input.toInt() // Преобразуем в целое число
                if (mins > 0) mins else null // Если число больше 0, возвращаем его
            }
        }
    } catch (e: Exception) {
        null // Если произошла ошибка, возвращаем null
    }
}
