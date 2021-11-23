package com.bonnjalal.ratemyappsample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bonnjalal.rate_my_app.RateMyAppDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appRatingDialog = RateMyAppDialog(this, "bonnjalalbus@gmail.com")
        appRatingDialog.setRateText("Your custom text")
            .setTitle("Your custom title")
            .setForceMode(false)
            .setUpperBound(4) // here 4 and more to send user to play store
                // and less than 4 send user to e-mail app
            //.setNegativeReviewListener(this) // if you want to handle negative button manually
            //.setReviewListener(this) // if you want to handle positive button manually
            .setStarColor(Color.BLUE) // set stars color
            .setTopBarColor(Color.BLUE)
            .setDialogBoxColor(Color.YELLOW)
            .setTitleColor(Color.WHITE)
            .setMessageColor(Color.WHITE)
            .setButtonsTextColor(Color.BLACK)
            .setOkButtonColor(Color.WHITE)
            .setNeverButtonColor(Color.WHITE)
            .setNotNowButtonColor(Color.WHITE)
            .showAfter(0) // how much time the user enter your app for dialog to appear
    }
}