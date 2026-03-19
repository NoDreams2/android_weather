## 1. Клонирование репозитория
```bash
git clone https://github.com/NoDreams2/android_weather.git
cd android_weather

## 2. Настройка local.properties
Создайте файл local.properties в корне проекта и укажите путь к Android SDK:
echo "sdk.dir=C\:\\Users\\%USERNAME%\\AppData\\Local\\Android\\Sdk" > local.properties

## 3. Сборка APK
./gradlew clean
./gradlew assembleDebug

## 4. Где найти APK
app/build/outputs/apk/debug/app-debug.apk
