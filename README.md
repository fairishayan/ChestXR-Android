# 📱 ChestXR - Android App

An Android application built with **Jetpack Compose** that detects Pneumonia from Chest X-rays.

## 🚀 Features
- Pick images from Gallery (using Photo Picker API).
- Beautiful UI with Material 3 (Loading, Success, Error states).
- Displays prediction (NORMAL / PNEUMONIA) with confidence score.

## 🔗 Backend
This app connects to a FastAPI Python backend powered by MobileNetV3.
👉 [Link to ML Backend Repo](https://github.com/fairishayan/ChestXR-ML)

## 🛠️ Tech Stack
- Kotlin, Jetpack Compose
- MVVM Architecture
- Retrofit (for API calls)
- Coil (for Image Loading)

## ⚙️ How to run
1. Open in Android Studio.
2. Update the API IP address in `MainViewModel.kt` (currently set to local Wi-Fi).
3. Run the app on your physical device.
