package proglife.com.ua.intellektiks.ui.bonus

import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_bonus.*
import kotlinx.android.synthetic.main.li_media_object_player.*
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.models.FileType
import proglife.com.ua.intellektiks.ui.base.NavBaseActivity
import proglife.com.ua.intellektiks.utils.ExoUtils

class BonusActivity: NavBaseActivity(), BonusView{

    @InjectPresenter lateinit var presenter: BonusPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomView(R.layout.activity_bonus)
        supportActionBar?.setTitle(R.string.nav_bonus)

        val spanable = SpannableString(getString(R.string.text_bonus))
        spanable.setSpan(StyleSpan(Typeface.BOLD), 0, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.text =  spanable

        tvName.text = getString(R.string.name_bonus)
        tvInfo.text = getString(R.string.file_info, "9")
        presenter.init()
    }

    override fun initPlayer(url: String){
        val player = ExoUtils.initExoPlayerFactory(this)
        player.prepare(ExoUtils.buildMediaSource(ExoUtils.buildDataSourceFactory(this), Uri.parse(url), FileType.MP3))
        playController.player = player
        playController.showTimeoutMs = Int.MAX_VALUE
        player.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        if(playController.player!=null)
            playController.player.playWhenReady = false
    }
}