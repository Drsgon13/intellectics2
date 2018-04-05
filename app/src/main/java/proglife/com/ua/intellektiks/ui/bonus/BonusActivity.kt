package proglife.com.ua.intellektiks.ui.bonus

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import kotlinx.android.synthetic.main.activity_bonus.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.ui.base.NavBaseActivity
import proglife.com.ua.intellektiks.utils.ExoUtils

class BonusActivity: NavBaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_bonus)
        supportActionBar?.setTitle(R.string.nav_bonus)

        val spanable = SpannableString(getString(R.string.text_bonus))
        spanable.setSpan(StyleSpan(Typeface.BOLD), 0, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.text =  spanable
        /*val player = ExoUtils.initExoPlayerFactory(this)

        playController*/
    }
}