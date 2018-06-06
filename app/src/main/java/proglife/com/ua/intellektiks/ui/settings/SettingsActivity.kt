package proglife.com.ua.intellektiks.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.li_card.view.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.Card
import proglife.com.ua.intellektiks.data.models.UserData
import proglife.com.ua.intellektiks.ui.base.BaseActivity

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class SettingsActivity: BaseActivity(), SettingsView {

    @InjectPresenter
    lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_settings)

        supportActionBar?.setTitle(R.string.nav_settings)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        btnLogout.setOnClickListener { commonPresenter.logout() }
        btnClear.setOnClickListener { presenter.clearCache() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        withBackAnimation()
    }

    override fun showData(userData: Pair<String?, UserData?>) {
        userData.first?.let { tvEmail.text = it }
        userData.second?.let {
            tvName.text = getString(R.string.profile_hello, it.fullName())
            Glide.with(this)
                    .load(it.image())
                    .into(ivPhoto)
        }
    }

    override fun showCacheSize(size: Long) {
        val textSize = if (size >= 1024 * 1024 * 1024)
            getString(R.string.cache_size_gb, size / 1024f / 1024f / 1024f)
        else getString(R.string.cache_size_mb, size / 1024 / 1024)
        tvCacheInfo.text = textSize
    }

    override fun showClearCacheLoading() {
        btnClear.visibility = View.GONE
        pbLoading.visibility = View.VISIBLE
    }

    override fun dismissClearCacheLoading() {
        btnClear.visibility = View.VISIBLE
        pbLoading.visibility = View.GONE
    }

    override fun showCardsLoading() {
        pbCardsLoading.visibility = View.VISIBLE
    }

    override fun dismissCardsLoading() {
        pbCardsLoading.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    override fun showCards(cards: List<Card>) {
        cardsContainer.removeAllViews()
        if (cards.isEmpty()) {
            tvCardsMessage.text = getString(R.string.cards_empty)
        }
        cards.forEach { card ->
            val view = LayoutInflater.from(this).inflate(R.layout.li_card, cardsContainer, false)
            view.tvCardType.text = card.type
            view.tvCardNumber.text = "**** **** **** ${card.mask}"
            view.btnRemoveCard.setOnClickListener { presenter.removeCard(card) }
            cardsContainer.addView(view)
        }
    }

    override fun showCardsError(message: String?) {
        tvCardsMessage.text = getString(R.string.cards_error)
    }

    override fun showError(res: Int) {
        Snackbar.make(coordinator, res, Snackbar.LENGTH_LONG).show()
    }

    override fun showRemoveCardLoading() {
        progressDialog.show()
    }

    override fun dismissRemoveCardLoading() {
        progressDialog.dismiss()
    }

}