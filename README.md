# xs2a-react-native

Integrate XS2A from Tink Germany into your React Native App.
This React Native SDK is a wrapper of [xs2a-ios](https://github.com/FinTecSystems/xs2a-ios) and [xs2a-android](https://github.com/FinTecSystems/xs2a-android).

## Requirements

- iOS Version >= 13

## Installation

```sh
npm install @fintecsystems/xs2a-react-native --save
```

### iOS
```sh
cd ios/
pod install
```

### Android
Make sure, that your app's `minSdkVersion` is 21 or higher and that your `compileSdkVersion` and `targetSdkVersion` is 33 or higher.

Because of that your `gradle-build-tools`-version has to be `4.2.0` or higher.

Make sure, because we raised the `compileSdkVersion` to 33, to delete the `buildToolsVersion` field.

`android/build.gradle`
```groovy
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    ext {
        buildToolsVersion = "30.0.2" // Delete this line
        minSdkVersion = 21 // Upgrade if needed
        compileSdkVersion = 33 // Upgrade if needed
        targetSdkVersion = 33 // Upgrade if needed
    }
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.2") // Upgrade if needed
...
```

Since Android 12 (API Level 31) you need to add `android:exported` to your `AndroidManifest.xml`.

`android/app/src/main/AndroidManifest.xml`
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
  package="com.rn_testapp">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
      android:name=".MainApplication"
      android:label="@string/app_name"
      android:icon="@mipmap/ic_launcher"
      android:roundIcon="@mipmap/ic_launcher_round"
      android:allowBackup="false"
      android:theme="@style/AppTheme">
      <activity
        android:name=".MainActivity"
        android:label="@string/app_name"
        android:configChanges="keyboard|keyboardHidden|orientation|screenSize|uiMode"
        android:launchMode="singleTask"
        android:exported="true" <!--Add this line-->
        android:windowSoftInputMode="adjustResize">
```

## Usage

```jsx
import Xs2aReactNativeViewManager from "@fintecsystems/xs2a-react-native";

// ...

<Xs2aReactNativeViewManager
  style={{ width: '100%', height: '100%' }}
  wizardSessionKey="YOUR_WIZARD_SESSION_KEY"
  language="de | en | fr | it | es"
  onSuccess={({ nativeEvent: { credentials } }) => {
    // e.g. redirect to success screen

    // 'credentials' is only provided for XS2A.API sessions
    // with sync_mode = shared
    console.log('Success');
  }}
  onAbort={() => {
    // e.g. redirect to abort screen
    console.log('Abort');
  }}
  onNetworkError={() => {
    // e.g. redirect to error screen
    console.log('Network error');
  }}
  onBackButtonTapped={({ nativeEvent: { currentStep } }) => {
    // Called when the back button was tapped. Optional to implement.
    // currentStep can be any of "login", "tan", "account", "bank" or empty string ("")
  }}
  onSessionError={({ nativeEvent: { errorCode, recoverable } }) => {
    /**
    Session errors occur during a session.
    Implementation of the different cases below is optional.
    No action needs to be taken for them, in fact we recommend
    to let the user handle the completion of the session until one of the above .success or .failure cases is called.
    You can however use below cases for measuring purposes.
    NOTE: Should you decide to do navigation to different screens based on below cases, you should only do so
    in case of the recoverable parameter being false, otherwise the user can still finish the session.
    */
    // Detailed error descriptions can be found here: https://github.com/FinTecSystems/xs2a-ios#configure-and-present-the-view
    console.log(errorCode, recoverable);
  }}
  // All styles are optional
  styleProvider={{
    tintColor: '#ff0000',
    logoVariation: 'STANDARD',
    backgroundColor: '#ffffff',
    textColor: '#000000',
    inputBackgroundColor: '#059392',
    inputBorderRadius: 15,
    inputTextColor: '#000000',
    placeholderColor: '#dddddd',
    buttonBorderRadius: 9,
    submitButtonStyle: {
      textColor: '#000000',
      backgroundColor: '#dddddd',
    },
    backButtonStyle: {
      textColor: '#000000',
      backgroundColor: '#dddddd',
    },
    abortButtonStyle: {
      textColor: '#000000',
      backgroundColor: '#dddddd',
    },
    restartButtonStyle: {
      textColor: '#000000',
      backgroundColor: '#dddddd',
    },
    alertBorderRadius: 12,
    errorStyle: {
      textColor: '#000000',
      backgroundColor: '#dddddd',
    },
    warningStyle: {
      textColor: '#000000',
      backgroundColor: '#dddddd',
    },
    infoStyle: {
      textColor: '#000000',
      backgroundColor: '#dddddd',
    },
  }}
/>
```

### Encryption Export Compliance Information

When uploading your app to App Store Connect, Apple typically wants to know some information on whether your app uses encryption and if it qualifies for an exemption 
under Category 5, Part 2 of the U.S. Export Administration Regulations. This SDK *does* qualify for such exemption, namely article `(d)`:

> Specially designed and limited for banking use or "money transactions"

Please note, that this only applies to this SDK and the corresponding `XS2AiOSNetService`, but not to any other parts of your app, which might not qualify 
for such exemptions and you might have to reconsider how to answer that dialog.

### License

Please note that this mobile SDK is subject to the MIT license. MIT license does not apply to the logo of Tink Germany GmbH, the terms of use and the privacy policy of Tink Germany GmbH. The license terms of the logo of Tink Germany GmbH, the terms of use and the privacy policy of Tink Germany GmbH are included in the LICENSE as Tink Germany LICENSE.
