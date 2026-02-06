# Outfit Manager 👔

A sleek Material Design 3 Android app for tracking outfit states.

**One question answered:** *"Which outfits are wearable right now?"*

## Features

- 📸 **Add outfits** with photos
- 🔄 **Track 5 states**: Available → Worn → Needs Laundry → In Laundry → Washed
- 🎯 **Filter** by state instantly
- ⚡ **Quick actions** via long press
- 🎨 **Material Design 3** with dynamic colors
- 📴 **Offline-first** - No internet required

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Database:** Room (SQLite)
- **Architecture:** MVVM + Repository Pattern
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)

## Getting Started

### Requirements
- Android Studio Hedgehog (2023.1.1) or newer
- Android device or emulator (API 24+)

### Installation
1. Clone or open this project in Android Studio
2. Sync Gradle (automatic)
3. Run on device/emulator
4. Grant photo permissions

### First Use
1. Tap the **+** button to add your first outfit
2. Select a photo from your gallery
3. Optionally add name, type, and notes
4. Save and start tracking!

## How to Use

### Add Outfit
- Tap the floating **+** button
- Select a photo (required)
- Fill optional details (name, type, notes)
- Save (default state: Available)

### Change State
- **Tap** an outfit → Full detail sheet with all state options
- **Long press** an outfit → Quick menu with 3 suggested states

### Filter
- Use the chips at the top to filter by state
- Tap **"All"** to see everything

## Project Structure

```
app/src/main/java/com/outfitmanager/
├── domain/           # Business logic (OutfitState enum)
├── data/             # Database layer (Room)
├── ui/
│   ├── theme/        # Material 3 theming
│   ├── components/   # Reusable UI components
│   ├── home/         # Home screen + ViewModel
│   ├── addedit/      # Add/Edit screen + ViewModel
│   └── detail/       # Detail bottom sheet
└── MainActivity.kt   # Navigation setup
```

## Architecture

```
UI (Compose) → ViewModel → Repository → Room DAO → SQLite
```

- **Reactive**: Flow-based data streams for instant UI updates
- **Offline**: All data stored locally
- **Clean**: Separation of concerns with repository pattern

## Design Highlights

✨ **Material Design 3** throughout  
🎨 **Dynamic colors** on Android 12+ (adapts to wallpaper)  
🌓 **Dark mode** supported  
📐 **8dp grid system** for consistent spacing  
🎭 **Smooth animations** with Material motion  
🖼️ **Gradient overlays** for better text readability on images  

## State Flow

```
Available → Worn → Needs Laundry → In Laundry → Washed → Available
```

Manual override supported - real life isn't always linear!

## Dependencies

- Compose BOM 2024.02.00
- Room 2.6.1
- Navigation Compose 2.7.6
- Coil 2.5.0 (image loading)
- Lifecycle ViewModel Compose 2.7.0

## License

This is a learning project created as part of an Android development exercise.

## Screenshots

*Add screenshots after building the app*

---

**Built with ❤️ using Jetpack Compose**
