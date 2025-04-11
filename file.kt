// Основной класс активности, наследующийся от ComponentActivity
class MainActivity : ComponentActivity() {
    
    // Переопределение метода onCreate, который вызывается при создании активности
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)  // Вызов родительского метода для инициализации

        // Включаем режим использования всего экрана
        enableEdgeToEdge()

        // Устанавливаем UI с помощью Jetpack Compose
        setContent {
            // Применение темы для приложения
            DicesTheme {

                // Контейнер для отображения UI
                Surface(modifier = Modifier.fillMaxSize()) {
                    
                    // Основное приложение для работы с игрой в кости
                    DiceApp { message ->
                        // Показать Toast-сообщение при вызове
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

// Компонент игры, который показывает UI для симуляции игры в кости
@Composable
fun DiceApp(showToast: (String) -> Unit) {
    
    // Состояние для текста количества костей
    var diceCountText by remember { mutableStateOf("1") }
    
    // Состояние для количества костей
    var diceCount by remember { mutableStateOf(1) }
    
    // Состояние для выбора, должны ли все кости быть одинаковыми
    var sameValues by remember { mutableStateOf(true) }
    
    // Состояние для максимальной значения каждой кости
    var oneMaxValue by remember { mutableStateOf("6") }
    
    // Список с максимальными значениями для каждой кости
    var maxValues by remember { mutableStateOf(listOf("6")) }
    
    // Список для хранения результатов бросков костей
    var diceResults by remember { mutableStateOf(listOf(1)) }
    
    // Список для хранения результатов предыдущего броска
    var previousResults by remember { mutableStateOf(listOf<Int>()) }

    // Верстка интерфейса
    Column(
        modifier = Modifier
            .fillMaxSize()  // Заполнение всего доступного пространства
            .padding(24.dp),  // Отступы по краям
        horizontalAlignment = Alignment.CenterHorizontally  // Выравнивание по центру по горизонтали
    ) {

        // Заголовок приложения
        Text("Symulator Kości", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Поле ввода для количества костей
        OutlinedTextField(
            value = diceCountText,  // Текущее значение текста в поле
            onValueChange = {
                // Обработчик изменения текста в поле
                diceCountText = it
                val count = it.toIntOrNull()  // Преобразование текста в целое число
                if (count != null && count in 1..5) {  // Проверка, что число в пределах от 1 до 5
                    diceCount = count
                    diceResults = List(count) { 1 }  // Инициализация результатов броска
                    previousResults = List(count) { 1 }  // Инициализация предыдущих результатов
                    maxValues = List(count) { oneMaxValue }  // Инициализация значений для каждой кости
                } else {
                    showToast("Podaj liczbę od 1 do 5")  // Показать Toast-сообщение, если введено неправильное значение
                }
            },
            label = { Text("Ilość kości") },  // Надпись на поле ввода
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),  // Тип клавиатуры - числа
            modifier = Modifier.width(200.dp)  // Ширина поля
        )

        Spacer(modifier = Modifier.height(12.dp))  // Пробел между элементами

        // Переключатель для выбора одинаковых значений для всех костей
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = sameValues, onCheckedChange = {
                sameValues = it  // Обновление состояния переключателя
            })
            Text("Wszystkie takie same", modifier = Modifier.padding(start = 8.dp))  // Надпись рядом с переключателем
        }

        Spacer(modifier = Modifier.height(12.dp))  // Пробел между элементами

        // Если выбрано, что все кости одинаковые, показывается поле для ввода максимального значения
        if (sameValues) {
            OutlinedTextField(
                value = oneMaxValue,  // Текущее значение максимальной величины
                onValueChange = {
                    oneMaxValue = it  // Обновление максимальной величины
                    maxValues = List(diceCount) { oneMaxValue }  // Обновление значений для всех костей
                },
                label = { Text("Maksymalna wartość") },  // Надпись на поле
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),  // Тип клавиатуры - числа
                modifier = Modifier.width(200.dp)  // Ширина поля
            )
        } else {
            // Если не все кости одинаковые, показываются отдельные поля для каждой кости
            Column {
                maxValues.forEachIndexed { index, value ->
                    OutlinedTextField(
                        value = value,  // Текущее значение для каждой кости
                        onValueChange = {
                            // Обновление максимальной величины для конкретной кости
                            maxValues = maxValues.toMutableList().also { list -> list[index] = it }
                        },
                        label = { Text("Kość ${index + 1}") },  // Подпись с номером кости
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),  // Тип клавиатуры - числа
                        modifier = Modifier
                            .padding(vertical = 4.dp)  // Отступы
                            .width(200.dp)  // Ширина поля
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))  // Пробел между элементами

        // Кнопка для выполнения броска костей
        Button(onClick = {
            val parsedValues = maxValues.map { it.toIntOrNull() }  // Преобразование значений в числа
            if (parsedValues.all { it != null && it in 1..6 }) {  // Проверка, что все значения в диапазоне от 1 до 6
                previousResults = diceResults  // Сохраняем предыдущие результаты
                diceResults = parsedValues.map { (1..it!!).random() }  // Генерация случайных результатов броска
            } else {
                showToast("Wprowadź liczby od 1 do 6")  // Показать Toast, если введены неправильные значения
            }
        }) {
            Text("Rzuć kośćmi")  // Текст на кнопке
        }

        Spacer(modifier = Modifier.height(16.dp))  // Пробел между элементами

        // Заголовок для результатов броска
        Text("Wyniki:")
        Row(horizontalArrangement = Arrangement.Center) {
            // Отображение изображений костей для текущих результатов
            diceResults.forEach { DiceImage(it) }
        }

        Spacer(modifier = Modifier.height(24.dp))  // Пробел между элементами

        // Заголовок для предыдущих результатов
        Text("Poprzedni rzut:")
        Row(horizontalArrangement = Arrangement.Center) {
            // Отображение изображений костей для предыдущих результатов
            previousResults.forEach { DiceImage(it) }
        }
    }
}

// Функция для отображения изображения кости по ее значению
@Composable
fun DiceImage(value: Int) {
    val image: Painter = painterResource(id = when (value) {
        1 -> R.drawable.dice_1  // Изображение для кости с значением 1
        2 -> R.drawable.dice_2  // Изображение для кости с значением 2
        3 -> R.drawable.dice_3  // Изображение для кости с значением 3
        4 -> R.drawable.dice_4  // Изображение для кости с значением 4
        5 -> R.drawable.dice_5  // Изображение для кости с значением 5
        else -> R.drawable.dice_6  // Изображение для кости с значением 6
    })

    // Отображение изображения кости
    Image(
        painter = image,  // Источник изображения
        contentDescription = "Kość $value",  // Описание изображения для доступности
        modifier = Modifier
            .size(64.dp)  // Размер изображения
            .padding(4.dp)  // Отступы вокруг изображения
    )
}
