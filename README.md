# Food Journal
A personal diary for Android.
This app is a food diary that lets the users to take photos of their meals and upload the pictures to the platform, and share the records with their family and friends.
This app also take notes on the food's location so it can be share to their friends quickly.
Entries can be search by tags, edited and deleted by the user.

## App description

### 1) Users
**Primary users:**
A person who dine in their own home or eating out and want to remember what they eat, where and how the food taste.
For example, university students who find new cafes and hawker stalls.

**Secondary users:**
Friends and family who receive shared meal records.

### 2) Core task
- Record a meal with a photo, the type of food, location where it was eaten, and additional notes.
- Share personal food experience easily with friends and family.

### 3) Data
Used GitHub to commit and push our works.
The app stores three Room tables and a small set of preferences in Settings.

| Data | Fields | Notes |
|---|---|---|
| 'Place' (table 'places') | id, name, latitude, longitude | A restaurant location. Created from 'Add map' |
| 'MyFoodRecord' (table 'food_data') | id, photo, description, timeDate, tag1, tag2, tag3, placeId | A meal record. The photo is stored in a file path in the app's own storage on the phone. Tags are stored without the '#', at most three. |
| 'ShareRecord' (table 'shares_data') | id, foodId, shareAt | A share record. Add one row each time the user share an entry. |
| 'Settings' | theme (Purple, Peach, Blue), cameraOn, mapOn | Small user preferences. The theme recolors the app. The toggles on and off the camera and map with buttons. |

**Relationship:**
A Meal has many Shares (one-to-many).

- User cannot create a Share without a Meal.
- Sharing is optional - a Meal can exist with zero Shares


### 4) Out-of-scope
1. User accounts and authentication(no username, password and login)
2. Add friends

## Build steps
- **Android Studio version**: - Android Studio Rabbit 1 | 2026.2.1 (Build #AI-262.9437.185.2621.16467767)
- **Kotlin**: 2.2.10
- **JDK version**: 21
- **Jetpack Compose**: Compose BOM 2026.02.01 (Material 3)
- **Minimum SDK**: 24
- **Target SDK**: 37
- **Gradle**: 9.5.0 (via the Gradle wrapper)

## Run steps
1. Connect a physical device or start an emulator.
2. Select the 'app' configuration.
3. Click 'Run' (green triangle)
4. Grant camera and location permission when prompted.

Permissions the app may ask for while running:
- Camera, when the user taps the camera button. Declining is handled.
- Location, when the user taps the add map. Declining and deleting is handled.

## Device features
### Camera
1. Log in to the app
2. Tap the '+' icon.
3. Take a photo. It appears in the form.
4. Cancel the camera will returns to the app without crashing.
5. Deny camera permission will offers gallery as an alternative.

### Location
1. Tap 'Add Map' in the editing screen
2. The app fetches GPS coordinates and reverse-geocodes them.
3. If denies permission, the app shows a message and allows manual place entry.

### Settings
- Theme: Purple, Peach, Blue
- Camera: On or Off
- Map: On or Off

### Sharing

