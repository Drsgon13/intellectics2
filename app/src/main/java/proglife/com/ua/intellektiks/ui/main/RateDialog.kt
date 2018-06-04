package proglife.com.ua.intellektiks.ui.main

import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionManager
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_rate.*
import proglife.com.ua.intellektiks.R
import android.content.Intent
import android.net.Uri
import proglife.com.ua.intellektiks.BuildConfig


/**
 * Created by Evhenyi Shcherbyna on 04.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class RateDialog : DialogFragment() {

    private val constraintSet2 = ConstraintSet()
    private val constraintSet3 = ConstraintSet()

    var action: Action? = null

    init {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.RateDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_rate, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnRateNow.setOnClickListener { switchToRatingBar() }
        btnRateLater.setOnClickListener { dismiss() }
        btnRateDenied.setOnClickListener {
            dismiss()
            action?.onRatingDenied()
        }
        rbRating.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                if (rating >= 4) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
                    startActivity(intent)
                }
                switchToThankYou()
                action?.onRatingAccepted()
            }
        }
        action?.onRatingDeferred()
    }

    private fun switchToRatingBar() {
        constraintSet2.clone(constraintLayout)
        constraintSet2.centerHorizontally(R.id.tvTitle, 0)
        constraintSet2.setVisibility(R.id.tvDescription, View.INVISIBLE)
        constraintSet2.setVisibility(R.id.rbRating, View.VISIBLE)
        constraintSet2.setVisibility(R.id.btnRateDenied, View.GONE)
        constraintSet2.setVisibility(R.id.btnRateLater, View.GONE)
        constraintSet2.setVisibility(R.id.btnRateNow, View.GONE)
        btnRateDenied.visibility = View.GONE
        btnRateLater.visibility = View.GONE
        btnRateNow.visibility = View.GONE

        TransitionManager.beginDelayedTransition(constraintLayout)
        constraintSet2.applyTo(constraintLayout)
    }

    private fun switchToThankYou() {
        rbRating.setOnTouchListener { _, _ -> true }

        constraintSet3.clone(constraintLayout)
        constraintSet3.setVisibility(R.id.tvDone, View.VISIBLE)

        TransitionManager.beginDelayedTransition(constraintLayout)
        constraintSet3.applyTo(constraintLayout)

        constraintLayout.setOnClickListener { dismiss() }
    }

    interface Action {
        fun onRatingDenied()
        fun onRatingAccepted()
        fun onRatingDeferred()
    }

}