# 🎌 Seekho Anime App — Offline-First Anime Browser

Seekho Anime is a modern, offline-first Android anime browser built using the **Jikan API**.  
The app is designed to demonstrate production-grade Android development with a focus on:

- Offline caching
- Scalable architecture
- Smooth Jetpack Compose UI
- Pagination
- Testability

This project goes beyond a basic API app to reflect real-world engineering practices used in product-based companies.

---

## ✨ Key Features

### 📶 Offline-First Architecture
- All browsed anime is cached locally using Room.
- App continues to work fully without internet.
- In-app offline banner indicates network status.

### 🧭 Modern UI (Jetpack Compose + Material 3)
- 100% Jetpack Compose UI
- Material 3 theming
- Bottom navigation with:
    - **Explore** — Browse anime feed
    - **Favorites** — Saved anime (offline)
    - **About** — App information

### 🔄 Infinite Scrolling (Paging 3)
- Efficient pagination using Paging 3
- Smooth loading for large anime datasets
- Optimized scrolling performance

### ❤️ Favorites (Offline Available)
- Save anime locally
- Stored in Room database
- Accessible without internet

### 🎨 Smooth UX
- Shimmer loading for initial content
- Debounced search input
- Graceful loading, empty, and error states

### 🧪 Testing
- Unit tests for data and repository layer
- Instrumented tests for:
    - Room database
    - Navigation
    - Compose UI

---

## 🛠 Tech Stack

- **UI:** Jetpack Compose, Material 3
- **Architecture:** MVVM
- **Dependency Injection:** Dagger-Hilt
- **Concurrency:** Kotlin Coroutines & Flow
- **Networking:** Retrofit
- **Database:** Room (Offline cache)
- **Paging:** Paging 3 + RemoteMediator
- **Testing:**
    - JUnit
    - Robolectric
    - Hilt Testing
    - Compose Test Rules

---

## 🏗 Architecture Overview

- Offline-first design
- Room as single source of truth
- RemoteMediator for syncing network + local cache
- Repository pattern for data abstraction
- Reactive UI using Flow + Compose

---

## 📸 App Screenshots

<p float="left">
  <img src="https://raw.githubusercontent.com/kumarpatole/SeekhoAnimeApp/refs/heads/main/screenshots/home.png" width="220" />
  <img src="https://raw.githubusercontent.com/kumarpatole/SeekhoAnimeApp/refs/heads/main/screenshots/detail.png" width="220" />
  <img src="https://raw.githubusercontent.com/kumarpatole/SeekhoAnimeApp/refs/heads/main/screenshots/favorite.png" width="220" />
</p>

<p float="left">
  <img src="https://raw.githubusercontent.com/kumarpatole/SeekhoAnimeApp/refs/heads/main/screenshots/emptyfav.png" width="220" />
  <img src="https://raw.githubusercontent.com/kumarpatole/SeekhoAnimeApp/refs/heads/main/screenshots/about.png" width="220" />
</p>

