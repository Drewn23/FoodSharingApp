# Food Journal
This app is a personal food diary that lets the users to take photos of their meals and upload the pictures to the platform, and share the records with their family and friends.
This app also take notes on the food's location so it can be share to their friends quickly.

(What it does)


## App description

### 1) Users
**Primary users:**
A person who dine in their own home or eating out and want to remember what they eat, where and how the food taste.

**Secondary users:**
Friends and family who receive shared meal records.

(types of users)

### 2) Core task
- Record a meal with a photo, the type of food, location where it was eaten, and additional notes.
- Share personal food experience easily with friends and family.

(What the app should do)

### 3) Data
?

(the passing data between functions)

**Relationship:**

A Meal has many Shares (one-to-many).

- User cannot create a Share without a Meal.
- Sharing is optional - a Meal can exist with zero Shares


### 4) Out-of-scope
1. User accounts and authentication
2. Add friends

(What is ot included)

## Build steps
- **Android Studio version**:
- **Kotlin**:
- **JDK version**:
- **Jetpack Compose**:
- **Minimum SDK**:
- **Target SDK**:
- **Gradle**:

(Android studio version adn others)

## Run steps
1. Connect a physical device or start an emulator.
2. Select the 'app' configuration.
3. Click 'Run' (green triangle)
4. Grant camera and location permission when prompted.

(How to run)

## Network features
GPS

?

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
- Theme: Red, Peach, Blue
- Camera: On or Off
- Map: On or Off