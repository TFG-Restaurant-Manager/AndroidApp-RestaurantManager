# Restaurant Manager Mobile App

### English en / [Español es](README_ES.md)

Mobile application for restaurant staff that connects to the **Restaurant Manager API**.
The app is designed mainly for **waiters**, allowing them to manage tables, orders, and their personal work information.

---

## Features

- View and manage **restaurant tables**
- Create and manage **orders**
- Browse available **food and products**
- View **personal profile**
- Check **work schedules**

---

## Tech Stack

- **Kotlin**
- **Android / Jetpack Compose**
- **REST API integration**
- **MVVM architecture**

---

## Screens

### Profile

Shows the personal information of the employee and their work schedule.

![Profile Screen](images/profile.png)

---

### Orders

Allows waiters to create and manage customer orders.

![Orders Screen](images/orders.png)

---

### Tables

Displays the restaurant tables and their current state.

![Tables Screen](images/tables.png)

---

### Food

List of dishes and products available to add to an order.

![Food Screen](images/food.png)

---

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/your-repository/restaurant-manager-mobile.git
cd restaurant-manager-mobile
```

---

### 2. Open the project

Open the project with **Android Studio**.

---

### 3. Configure API connection

Edit the base URL in the API configuration file:

```kotlin
BASE_URL = "http://localhost:8080"
```

If running on an emulator, you may need to use:

```kotlin
BASE_URL = "http://10.0.2.2:8080"
```

---

### 4. Run the application

Connect a device or start an emulator and run the project from **Android Studio**.

---

## Project Structure

```
AndroidApp_RestaurantManager
    └── app/src
        ├── androidTest
        ├── test
        └── java/com/tfg_rm/androidapp_restaurantmanager
            ├── data
            │   ├── remote
            │   └── repository
            ├── ui
            │   ├── screens
            │   ├── theme
            │   └── navigation
            ├── domain
            |   ├── models
            |   ├── services
            |   └── viewmodels
            └── MainActivity.kt
```

---

## Backend Dependency

This application depends on the **Restaurant Manager API** backend.

Make sure the backend is running before starting the mobile app.

---
