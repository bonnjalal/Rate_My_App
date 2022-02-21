package com.bonnjalal.rate_my_app

import android.widget.TextView
import android.widget.RatingBar
import android.view.LayoutInflater
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.Toast
import android.graphics.drawable.LayerDrawable
import android.graphics.PorterDuffColorFilter
import android.graphics.PorterDuff
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * Created by angtrim on 12/09/15.
 * Forked and upgraded by bonnjalal on 05/11/21
 */
class RateMyAppDialog(private val context: Context, supportEmail: String) {
    private var isForceMode = false
    private val sharedPrefs: SharedPreferences
    private var supportEmail: String
    private val contentTextView: TextView? = null
    private lateinit var ratingBar: RatingBar
    private var title: String? = null
    private var rateText: String? = null
    private var alertDialog: AlertDialog? = null
    private lateinit var dialogView: View
    private var upperBound = 4
    private var negativeReviewListener: NegativeReviewListener? = null
    private var reviewListener: ReviewListener? = null
    private var starColor = 0
    private var okBtnColor = 0
    private var notNowBtnColor = 0
    private var neverBtnColor = 0
    private var topBarColor = 0
    private var dialogBoxColor = 0
    private var titleTxtColor = 0
    private var messageTxtColor = 0
    private var buttonsTxtColor = 0
    private var positiveButtonText = "Ok"
    private var negativeButtonText = "Not Now"
    private var neutralButtonText = "Never"
    private lateinit var okBtn: Button
    private lateinit var notNowBtn: Button
    private lateinit var neverBtn: Button
    private lateinit var titleTxt: TextView
    private lateinit var messageTxt: TextView
    private lateinit var dialogBox: ConstraintLayout

    private fun build() {

        val builder = AlertDialog.Builder(
            context, R.style.CustomAlertDialog
        )
        val inflater = LayoutInflater.from(context)
        dialogView = inflater.inflate(R.layout.layout_custom_dialog, null)

        builder.setView(dialogView)
        val titleToAdd = if (title == null) DEFAULT_TITLE else title!!
        val textToAdd = if (rateText == null) DEFAULT_TEXT else rateText!!

        titleTxt = dialogView.findViewById(R.id.textTitle)
        messageTxt = dialogView.findViewById(R.id.textMessage)
        okBtn = dialogView.findViewById(R.id.buttonYes)
        notNowBtn = dialogView.findViewById(R.id.buttonNotNow)
        neverBtn = dialogView.findViewById(R.id.buttonNever)
        dialogBox = dialogView.findViewById(R.id.layoutDialog)
        titleTxt.setText(titleToAdd)
        messageTxt.setText(textToAdd)
        okBtn.setText(positiveButtonText)
        notNowBtn.setText(negativeButtonText)
        neverBtn.setText(neutralButtonText)
        ratingBar = dialogView.findViewById(R.id.ratingBar)

        ratingBar.onRatingBarChangeListener =
            OnRatingBarChangeListener { ratingBar: RatingBar, v: Float, b: Boolean ->
                Log.d(TAG, "Rating changed : $v")
                if (isForceMode && v >= upperBound) {
                    openMarket()
                    if (reviewListener != null) reviewListener!!.onReview(ratingBar.rating.toInt())
                }
            }

        // Setup Custimized user UI
        setUpDialogUI()

        okBtn.setOnClickListener(View.OnClickListener setOnClickListener@{
            if (ratingBar.getRating() <= 0) {
                Toast.makeText(
                    context,
                    context.getString(R.string.select_stars),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (ratingBar.getRating() < upperBound) {
                if (negativeReviewListener == null) {
                    sendEmail()
                } else {
                    negativeReviewListener!!.onNegativeReview(ratingBar.getRating().toInt())
                }
            } else if (!isForceMode) {
                openMarket()
            }
            disable()
            if (reviewListener != null) reviewListener!!.onReview(ratingBar.getRating().toInt())
            alertDialog!!.hide()
        })
        neverBtn.setOnClickListener {
            disable()
            alertDialog!!.hide()
        }
        notNowBtn.setOnClickListener {
            val editor = sharedPrefs.edit()
            editor.putInt(SP_NUM_OF_ACCESS, 0)
            editor.apply()
            alertDialog!!.hide()
        }

        alertDialog = builder.create()
    }

    private fun setUpDialogUI() {
        ratingBar.setBackgroundColor(Color.WHITE)
        val stars = ratingBar.progressDrawable as LayerDrawable
        val starCF: PorterDuffColorFilter
        starCF = if (starColor != 0) {
            PorterDuffColorFilter(starColor, PorterDuff.Mode.SRC_ATOP)
        } else {
            PorterDuffColorFilter(
                Color.parseColor("#DBEF12"),
                PorterDuff.Mode.SRC_ATOP
            )
        }

        //ratingBar.backgroundTintMode = PorterDuff.Mode.DARKEN
                //stars.getDrawable(1).colorFilter = starCF
        stars.getDrawable(2).colorFilter = starCF
        val starCF0 = PorterDuffColorFilter(Color.parseColor("#EFEDED"), PorterDuff.Mode.SRC_ATOP)
        stars.getDrawable(0).colorFilter = starCF0

        if (okBtnColor != 0) {
            val btnFilter = PorterDuffColorFilter(okBtnColor, PorterDuff.Mode.SRC_ATOP)
            //LayerDrawable okBtnLayer = (LayerDrawable) okBtn.getBackground();
            //okBtnLayer.setColorFilter(btnFilter);
            okBtn.background.colorFilter = btnFilter
        }
        if (neverBtnColor != 0) {
            val btnFilter = PorterDuffColorFilter(neverBtnColor, PorterDuff.Mode.SRC_ATOP)
            neverBtn.background.colorFilter = btnFilter
        }
        if (notNowBtnColor != 0) {
            val btnFilter = PorterDuffColorFilter(notNowBtnColor, PorterDuff.Mode.SRC_ATOP)
            notNowBtn.background.colorFilter = btnFilter
        }
        if (topBarColor != 0) {
            val btnFilter = PorterDuffColorFilter(topBarColor, PorterDuff.Mode.SRC_ATOP)
            titleTxt.background.colorFilter = btnFilter
        }
        if (dialogBoxColor != 0) {
            val btnFilter = PorterDuffColorFilter(dialogBoxColor, PorterDuff.Mode.SRC_ATOP)
            dialogBox.background.colorFilter = btnFilter
        }
        if (titleTxtColor != 0) {
            titleTxt.setTextColor(titleTxtColor)
        }
        if (messageTxtColor != 0) {
            messageTxt.setTextColor(messageTxtColor)
        }
        if (buttonsTxtColor != 0) {
            okBtn.setTextColor(buttonsTxtColor)
            neverBtn.setTextColor(buttonsTxtColor)
            notNowBtn.setTextColor(buttonsTxtColor)
        }
    }

    private fun disable() {
        val shared = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val editor = shared.edit()
        editor.putBoolean(SP_DISABLED, true)
        editor.apply()
    }

    private fun openMarket() {
        val appPackageName = context.packageName
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
            .setData(Uri.Builder().scheme("mailto").build())
            .putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmail))
            .putExtra(Intent.EXTRA_SUBJECT, "App Report (" + context.packageName + ")")
            .putExtra(Intent.EXTRA_TEXT, "Report: ")
        @SuppressLint("QueryPermissionsNeeded") val emailApp = intent.resolveActivity(
            context.packageManager
        )
        val unsupportedAction = ComponentName.unflattenFromString("com.android.fallback/.Fallback")
        if (emailApp != null && emailApp != unsupportedAction) try {
            // Needed to customise the chooser dialog title since it might default to "Share with"
            // Note that the chooser will still be skipped if only one app is matched
            val chooser = Intent.createChooser(intent, context.getString(R.string.send_message_via))
            context.startActivity(chooser)
            return
        } catch (ignored: ActivityNotFoundException) {
        }
        Toast.makeText(context, context.getString(R.string.no_email_app), Toast.LENGTH_LONG).show()
    }

    private fun show() {
        val disabled = sharedPrefs.getBoolean(SP_DISABLED, false)
        if (!disabled) {
            build()
            alertDialog!!.show()
        }
    }

    fun showAfter(numberOfAccess: Int) {
        build()
        val editor = sharedPrefs.edit()
        val numOfAccess = sharedPrefs.getInt(SP_NUM_OF_ACCESS, 0)
        editor.putInt(SP_NUM_OF_ACCESS, numOfAccess + 1)
        editor.apply()
        if (numOfAccess + 1 >= numberOfAccess) {
            show()
        }
    }

    fun setTitle(title: String?): RateMyAppDialog {
        this.title = title
        return this
    }

    fun setSupportEmail(supportEmail: String): RateMyAppDialog {
        this.supportEmail = supportEmail
        return this
    }

    fun setRateText(rateText: String?): RateMyAppDialog {
        this.rateText = rateText
        return this
    }

    fun setStarColor(color: Int): RateMyAppDialog {
        starColor = color
        return this
    }

    fun setOkButtonColor(color: Int): RateMyAppDialog {
        okBtnColor = color
        return this
    }

    fun setNotNowButtonColor(color: Int): RateMyAppDialog {
        notNowBtnColor = color
        return this
    }

    fun setNeverButtonColor(color: Int): RateMyAppDialog {
        neverBtnColor = color
        return this
    }

    fun setTitleColor(color: Int): RateMyAppDialog {
        titleTxtColor = color
        return this
    }

    fun setMessageColor(color: Int): RateMyAppDialog {
        messageTxtColor = color
        return this
    }

    fun setTopBarColor(color: Int): RateMyAppDialog {
        topBarColor = color
        return this
    }

    fun setDialogBoxColor(color: Int): RateMyAppDialog {
        dialogBoxColor = color
        return this
    }

    fun setButtonsTextColor(color: Int): RateMyAppDialog {
        buttonsTxtColor = color
        return this
    }

    fun setPositiveButtonText(OK_Button: String): RateMyAppDialog {
        positiveButtonText = OK_Button
        return this
    }

    fun setNegativeButtonText(Later_Button: String): RateMyAppDialog {
        negativeButtonText = Later_Button
        return this
    }

    fun setNeutralButton(Never_Button: String): RateMyAppDialog {
        neutralButtonText = Never_Button
        return this
    }

    /**
     * Set to true if you want to send the user directly to the market
     *
     * @param isForceMode
     * @return
     */
    fun setForceMode(isForceMode: Boolean): RateMyAppDialog {
        this.isForceMode = isForceMode
        return this
    }

    /**
     * Set the upper bound for the rating.
     * If the rating is >= of the bound, the market is opened.
     *
     * @param bound the upper bound
     * @return the dialog
     */
    fun setUpperBound(bound: Int): RateMyAppDialog {
        upperBound = bound
        return this
    }

    /**
     * Set a custom listener if you want to OVERRIDE the default "send email" action when the user gives a negative review
     *
     * @param listener
     * @return
     */
    fun setNegativeReviewListener(listener: NegativeReviewListener?): RateMyAppDialog {
        negativeReviewListener = listener
        return this
    }

    /**
     * Set a listener to get notified when a review (positive or negative) is issued, for example for tracking purposes
     *
     * @param listener
     * @return
     */
    fun setReviewListener(listener: ReviewListener?): RateMyAppDialog {
        reviewListener = listener
        return this
    }

    companion object {
        private var DEFAULT_TITLE = "Rate this app"
        private var DEFAULT_TEXT = "How much do you love our app?"
        private const val SP_NUM_OF_ACCESS = "numOfAccess"
        private const val SP_DISABLED = "disabled"
        private val TAG = RateMyAppDialog::class.java.simpleName
    }

    init {
        sharedPrefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        this.supportEmail = supportEmail
        DEFAULT_TITLE = context.getString(R.string.rate_this_app)
        DEFAULT_TEXT = context.getString(R.string.how_much_stars)
    }
}