# AutoParts – Android App

![Android CI](https://img.shields.io/badge/Android%20Build-Passing-brightgreen) ![Kotlin](https://img.shields.io/badge/Kotlin-1.7.10-blue) ![MVVM](https://img.shields.io/badge/Architecture-MVVM-yellow) ![License](https://img.shields.io/badge/License-MIT-orange)

> **AutoParts** is a native Android application for browsing, searching, and purchasing automotive parts. It follows a modern MVVM architecture, communicates with a RESTful back-end, and provides a user-friendly shopping experience on mobile devices.


---

## Features

- **Browse Catalog**: View a paginated list of auto parts with images, names, prices, and stock status.  
- **Search & Filter**: Search by part name or SKU, filter by category, brand, or price range.  
- **Part Details**: View detailed information on each part, including description, specifications, and images (swipe through gallery).  
- **Shopping Cart**: Add/remove items to a persistent cart; update quantities before checkout.  
- **User Authentication**: Sign up / log in with email and password; keep user session with JWT tokens.  
- **Order Placement**: Submit orders directly from the cart; view order confirmation and history.  
- **Profile & Settings**: View and edit user profile, shipping address, and payment methods.  
- **Offline Support**: Cache last-fetched catalog pages with Room (SQLite) so users can browse recently viewed items offline.  
- **Push Notifications**: Receive order updates, promotional offers, and restock alerts via Firebase Cloud Messaging.  

---

## Tech Stack

- **Language**: Kotlin (1.7.x)  
- **Minimum SDK**: API Level 21 (Android 5.0 Lollipop)  
- **Target SDK**: API Level 33 (Android 13)  
- **Architecture**: MVVM (ViewModel + LiveData + Repository + Room)  
- **Networking**: Retrofit2 + OkHttp + Moshi (JSON parsing)  
- **Dependency Injection**: Hilt  
- **Image Loading**: Glide  
- **Persistence**: Room (SQLite)  
- **Reactive Streams**: Kotlin Coroutines + Flow  
- **UI**: AndroidX (AppCompat, Material Components, ConstraintLayout)  
- **Navigation**: AndroidX Navigation Component  
- **Authentication & Messaging**: Firebase Authentication & Firebase Cloud Messaging (FCM)  
- **Build System**: Gradle (Kotlin DSL)  
- **CI/CD**: GitHub Actions (unit tests + lint checks)  

---


## Getting Started

Follow these steps to set up the project locally and run it on your device or emulator.

### Prerequisites

- **Android Studio** (2022.3.1 or later) with Android SDK tools installed.  
- **Java Development Kit**: Java 11 or higher.  
- **Gradle**: Uses the wrapper bundled in the repository (`./gradlew`).  
- **Emulator/Device**: Any Android device running Lollipop (API 21) or higher.  

> **Optional:**  
> - **Firebase Project**: If you want to test push notifications, set up a Firebase project and add the `google-services.json` file to `app/`.  
> - **REST Back-End**: The app expects a running RESTful API (e.g., at `https://api.example.com/`) for parts data, authentication, and order processing. See [API Endpoints](#api-endpoints) below for details.

---

### Clone & Open

```bash

git clone https://github.com/tuananh2005889/AutoParts_Android.git
cd BackEnd
./gradlew BootRun

cd FrontEnd
run MainActivity

cd web_admin
npm install
npm run dev
