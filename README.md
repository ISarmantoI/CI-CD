# Консольное приложение Todo App с системой CI/CD

Проект выполнен в рамках практического занятия №2: **«Организация проекта с системой контроля версий и CI/CD»**.

[![CI / Build & Release](https://github.com/ISarmantoI/CI-CD/actions/workflows/ci.yml/badge.svg)](https://github.com/ISarmantoI/CI-CD/actions/workflows/ci.yml)
[![Publish to GitHub Packages (Maven)](https://github.com/ISarmantoI/CI-CD/actions/workflows/publish-package.yml/badge.svg)](https://github.com/ISarmantoI/CI-CD/actions/workflows/publish-package.yml)
[![Nightly Build & Test](https://github.com/ISarmantoI/CI-CD/actions/workflows/nightly.yml/badge.svg)](https://github.com/ISarmantoI/CI-CD/actions/workflows/nightly.yml)

---

## Ссылки на результаты

* **Репозиторий проекта:** [https://github.com/ISarmantoI/CI-CD](https://github.com/ISarmantoI/CI-CD)
* **GitHub Actions (Сборки и пайплайны):** [https://github.com/ISarmantoI/CI-CD/actions](https://github.com/ISarmantoI/CI-CD/actions)
* **GitHub Releases (Готовые исполняемые JAR):** [https://github.com/ISarmantoI/CI-CD/releases](https://github.com/ISarmantoI/CI-CD/releases)
* **GitHub Packages (Maven Registry):** [https://github.com/ISarmantoI/CI-CD/packages](https://github.com/ISarmantoI/CI-CD/packages)

---

## Описание проекта

Проект представляет собой консольное Java-приложение «Список задач» (Todo App), управляемое системой сборки **Gradle**, с настроенным процессом версионирования **GitHub Flow**, автоматизированным тестированием **JUnit 5**, а также настроенными пайплайнами **CI/CD** в **GitHub Actions**.

### Возможности приложения:
1. `add <task>` — добавление новой задачи в список (пробелы по краям нормализуются, пустые задачи игнорируются).
2. `remove <index>` — удаление задачи по порядковому номеру (с валидацией диапазона индексов).
3. `list` — вывод всех текущих задач с их статусом (`[ ]` — не выполнена, `[X]` — выполнена).
4. `clear` — полная очистка списка задач.
5. `done <index>` — отметка задачи как выполненной.
6. `search <query>` — регистронезависимый поиск задач по подстроке.
7. `exit` — завершение работы программы.

---

## Архитектура CI/CD

В проекте настроены три автоматизированных workflow:

1. **`CI / Build & Release` (`.github/workflows/ci.yml`)**:
   * **Триггер:** Каждый `push` в основную ветку `main`.
   * **Шаги:**
     1. Клонирование репозитория (`actions/checkout@v4`).
     2. Настройка прав на исполнение скрипта `gradlew`.
     3. Развертывание JDK 21 Temurin (`actions/setup-java@v4`).
     4. Кэширование зависимостей Gradle (`actions/cache@v4`).
     5. Чтение текущей версии проекта из `gradle.properties`.
     6. Чистая сборка проекта, запуск всех модульных тестов JUnit 5 и сборка исполняемого fat-JAR с помощью плагина Shadow (`./gradlew clean build`).
     7. При успешном завершении тестов — автоматическое создание GitHub Release с тегом вида `v<version>`.
     8. Прикрепление собранного fat-JAR (`todo-app-<version>.jar`) к релизу как ассета. При падении тестов релиз не создается.

2. **`Publish to GitHub Packages (Maven)` (`.github/workflows/publish-package.yml`)**:
   * **Триггер:** Успешное завершение первого workflow `CI / Build & Release` (через механизм `workflow_run`).
   * **Шаги:**
     1. Выгрузка исходного кода и настройка JDK.
     2. Выполнение задачи `./gradlew publish`.
     3. Публикация fat-JAR как Maven-артефакта в реестр пакетов **GitHub Packages** (`maven.pkg.github.com/ISarmantoI/CI-CD`).

3. **`Nightly Build & Test` (`.github/workflows/nightly.yml`)**:
   * **Триггер:** По расписанию каждый день в 02:00 UTC (cron: `0 2 * * *`), а также возможность ручного запуска через `workflow_dispatch`.
   * **Назначение:** Регулярная проверка стабильности кодовой базы и успешности прохождения юнит-тестов без создания новых релизов.

---

## Структура репозитория

```text
├── .github/
│   └── workflows/
│       ├── ci.yml                 # CI-сборка, тесты, сборка fat-JAR и GitHub Release
│       ├── nightly.yml            # Ночная периодическая сборка и проверка стабильности
│       └── publish-package.yml    # Автоматическая доставка артефакта в GitHub Packages
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── src/
│   ├── main/
│   │   └── java/
│   │       └── org/
│   │           └── example/
│   │               ├── TodoApp.java    # Консольный интерфейс CLI
│   │               └── TodoList.java   # Бизнес-логика управления задачами
│   └── test/
│       └── java/
│           └── org/
│               └── example/
│                   └── TodoListTest.java # Модульные тесты JUnit 5
├── .gitignore
├── build.gradle               # Конфигурация сборки Gradle, Shadow plugin, Maven Publish
├── gradle.properties          # Файл хранения текущей версии проекта
├── gradlew                    # Unix shell-скрипт Gradle Wrapper
├── gradlew.bat                # Windows bat-скрипт Gradle Wrapper
├── settings.gradle            # Настройки корневого проекта
└── README.md
```

---

## Модель ветвления GitHub Flow

Разработка велась строго в соответствии с методологией **GitHub Flow**:
* Ветка `main` содержит исключительно стабильный, протестированный код, готовый к развертыванию.
* Под каждую задачу создавалась тематическая ветка от `main`:
  1. `feat/todo-app-implementation` — реализация базовой логики, CLI и первых модульных тестов (коммиты `add TodoList`, `add TodoApp`, `add tests for the todo app`). Слияние через Pull Request #1.
  2. `feat/actions` — добавление `gradle.properties`, плагина Shadow fat-JAR и workflow автоматической сборки с релизами (коммиты `change build.gradle for jar build`, `add ci`). Слияние через Pull Request #2.
  3. `feat/github-packages` — интеграция плагина `maven-publish`, добавление workflow публикации пакетов (коммит `add cd`). Слияние через Pull Request #3.
  4. `feat/extended-features` — реализация расширенного функционала (`clear`, `done`, `search`), тестов для них и ночного workflow. Слияние через Pull Request #4.

---

## Инструкция по сборке и запуску локально

### 1. Запуск тестов
```bash
./gradlew test
```

### 2. Сборка исполняемого fat-JAR
```bash
./gradlew build
```
Собранный JAR-файл появится по пути: `build/libs/todo-app-<version>.jar`.

### 3. Запуск приложения
```bash
java -jar build/libs/todo-app-0.3.0.jar
```
Или напрямую через Gradle:
```bash
./gradlew run
```

---

## Как подключить артефакт из GitHub Packages

Для подключения опубликованного пакета в сторонний Gradle-проект:

```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/ISarmantoI/CI-CD")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
        }
    }
    mavenCentral()
}

dependencies {
    implementation 'org.example:todo-app:0.3.0'
}
```
