# TikTok for Quotes

An elegant, modern Android application to explore, save, and like inspirational quotes using Jetpack Compose. 
It supports offline persistence, search, categories, and synchronization with a local JSON backend.

## Tech Stack
| Layer                | Technology                          |
|----------------------|--------------------------------------|
| Language             | Kotlin                               |
| UI                   | Jetpack Compose, Material 3          |
| State                | ViewModel, StateFlow, Coroutines     |
| Persistence          | Room (SQLite)                        |
| Dependency Injection | Manual                               |
| Network/Backend      | Retrofit (JSON-based local fetch)    |
| Gradle Version       | Kotlin DSL                           |
| Min SDK              | 24                                   |

## Project Structure

📂 app/
├── data/              # Data models (Quote, Category, etc.), Room Database instance, Retrofit API interfaces, Repository to bridge ViewModel and data layers
│
├── ui/
│   ├── screen/        # Screen composables (SearchScreen, LikedQuotes, etc.)
│   └── theme/         # Centralized theme for the application
│
├── viewmodel/         # QuoteViewModel with all app logic
├── MainActivity.kt    # App entry point
└── SplashActivity/             

## Features

- View Quotes by Category
- Like and Save Quotes (stored locally)
- Powerful Keyword Search
- On-Demand Sync with JSON Data
- Clean & Adaptive Jetpack Compose UI

## Backend (Quotes Server)

For simplicity and offline-first approach:

- Quotes are fetched from a **static JSON file** stored locally or hosted on GitHub.
- On first app launch or manual sync, existing DB records are compared, and only new quotes are added (preserving likes/saves).
- JSON is parsed via Retrofit with a simple `@GET` endpoint for fetching the quote list.

```json
[
  {
    "id": 1,
    "text": "Your heart is the size of an ocean...",
    "author": "Rumi",
    "category": "Love"
  }
]
````


## Setup & Run

1. Clone the repository
2. Open in Android Studio
3. Build the project
4. Run on an emulator or device (API 24+)


## Author

Developed with ❤️ by an experienced Full Stack Developer with 8+ years of experience in backend and modern Android UI.
I specialize in delivering end-to-end product solutions clients truly love.
