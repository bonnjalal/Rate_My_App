# Rate_My_App Library


Rate My App Library is a small library that helps developers add a **"Rate My App"** dialog to their applications.

If the user gives **4 or 5 stars out of 5**, the user is sent to the *Google Play Store* page to give an actual rating.

If the user gives **3 or less stars out of 5**, the user is asked to *send a bug report* to the developer.

If "Force Mode" is activated, when the user selects 4/5 stars, he is immediately redirected to the Play Store, without asking for a confirm. :D

## Preview

<img src="https://user-images.githubusercontent.com/42393256/155017534-f9ddafe5-b5aa-4116-be42-852374a83963.png" alt="preview" width="300" height="533">


## Installation

To use the library, first include it your project using Gradle


    allprojects {
        repositories {
            jcenter()
            maven { url "https://jitpack.io" }
        }
    }

	dependencies {
	        implementation 'com.github.bonnjalal:Rate_My_App:v1.2'
	}

[![](https://jitpack.io/v/bonnjalal/Rate_My_App.svg)](https://jitpack.io/#bonnjalal/Rate_My_App)

## How to use
To use this library just add this snippet in the `onCreate` of your activity.

The `showAfter(int numbersOfAccess)` method tells the library after how many access the dialog has to be shown.

Example:

```kotlin
        val appRatingDialog = RateMyAppDialog(this, "yourSupportEmail@gmail.com")
        appRatingDialog.setRateText("Your custom text")
            .setTitle("Your custom title")
            .setForceMode(false) 
            .setUpperBound(4) // here 4 and more to send user to play store
                // and less than 4 send user to e-mail app
            //.setNegativeReviewListener(this) // if you want to handle negative button manually
            //.setReviewListener(this) // if you want to handle positive button manually
            /* use this to change the default dialog appearence
            .setStarColor(Color.BLUE) // set stars color
            .setTopBarColor(Color.BLUE) 
            .setDialogBoxColor(Color.YELLOW)
            .setTitleColor(Color.WHITE)
            .setMessageColor(Color.WHITE)
            .setButtonsTextColor(Color.BLACK)
            .setOkButtonColor(Color.WHITE)
            .setNeverButtonColor(Color.WHITE)
            .setNotNowButtonColor(Color.WHITE)
            .setPositiveButtonText("Ok")
            .setNegativeButtonText("Not Now")
            .setNeutralButton("never")
            */
            .showAfter(3) // how much time the user enter your app for dialog to appear
```
## Features

The library is very simple, just note that :
* When the user tap OK or NEVER the dialog will not show again
* When the user tap NOT NOW the access counter will be reset and the dialog will be shown again after the selected times.

## 
   This library based on Five Stars Library with new UI and some new features to let the developer let the Dialog match his app UI.

   The original Library: https://github.com/Angtrim/Android-Five-Stars-Library
