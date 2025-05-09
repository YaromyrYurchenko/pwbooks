package com.example.viewmodel  // Определяем пакет, в котором находится данный код

import android.os.Bundle  // Импортируем класс для работы с данными активности
import androidx.activity.ComponentActivity  // Импортируем базовый класс для активности
import androidx.activity.compose.setContent  // Импортируем функцию для установки контента в Compose
import androidx.activity.viewModels  // Импортируем функцию для получения ViewModel
import androidx.compose.foundation.layout.Column  // Импортируем компонент для вертикального расположения элементов
import androidx.compose.foundation.layout.padding  // Импортируем модификатор для добавления отступов
import androidx.compose.material3.Button  // Импортируем кнопку из библиотеки Material3
import androidx.compose.material3.Text  // Импортируем текстовый компонент из библиотеки Material3
import androidx.compose.runtime.Composable  // Импортируем аннотацию для функции-компонента
import androidx.compose.ui.Modifier  // Импортируем модификаторы для управления расположением элементов
import androidx.compose.ui.unit.dp  // Импортируем единицы измерения (dp) для отступов
import com.example.viewmodel.ui.theme.ViewModelTheme  // Импортируем тему для UI

class MainActivity : ComponentActivity() {  // Основная активность приложения

    private val clickViewModel: ClickViewModel by viewModels()  // Получаем ViewModel для отслеживания состояния

    override fun onCreate(savedInstanceState: Bundle?) {  // Переопределяем метод onCreate
        super.onCreate(savedInstanceState)  // Вызываем метод родительского класса
        setContent {  // Устанавливаем контент для активности с помощью Jetpack Compose
            ViewModelTheme {  // Применяем тему для приложения
                MainScreen(clickViewModel)  // Отображаем экран и передаем ViewModel
            }
        }
    }
}

@Composable  // Аннотация, которая указывает, что функция является компонентом Compose
fun MainScreen(clickViewModel: ClickViewModel) {  // Экран, который принимает ViewModel в качестве параметра
    Column(modifier = Modifier.padding(16.dp)) {  // Используем Column для вертикального расположения элементов и добавляем отступы
        Text(text = "Press: ${clickViewModel.clickCount.value}")  // Отображаем количество нажатий, получаем его из ViewModel
        Button(onClick = { clickViewModel.incrementClickCount() }) {  // Кнопка, которая увеличивает счетчик при нажатии
            Text(text = "Press me")  // Текст на кнопке
        }
    }
}

package com.example.viewmodel  // Определяем пакет для ViewModel

import androidx.compose.runtime.mutableStateOf  // Импортируем функцию для создания изменяемого состояния
import androidx.compose.runtime.State  // Импортируем интерфейс состояния
import androidx.lifecycle.ViewModel  // Импортируем класс ViewModel для работы с состоянием

class ClickViewModel : ViewModel() {  // Определяем ViewModel для хранения состояния

    private val _clickCount = mutableStateOf(0)  // Создаем изменяемое состояние для счетчика кликов (по умолчанию 0)
    val clickCount: State<Int> = _clickCount  // Публикуем только неизменяемую версию состояния

    fun incrementClickCount() {  // Функция для увеличения счетчика
        _clickCount.value++  // Увеличиваем значение счетчика
    }
}

