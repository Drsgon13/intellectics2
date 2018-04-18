package proglife.com.ua.intellektiks.ui.content.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.li_player.view.*
import proglife.com.ua.intellektiks.ui.content.models.PlayerViewModel

class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mediaContainer: FrameLayout = itemView.mediaContainer
    private val btnDownloadAll: Button = itemView.btnDownloadAll

    fun bind(playerViewModel: PlayerViewModel) {
        if (mediaContainer.childCount == 0 && playerViewModel.playerView != null) {
            mediaContainer.visibility = View.VISIBLE
            mediaContainer.addView(playerViewModel.playerView)
        } else if (mediaContainer.childCount > 0 && playerViewModel.playerView == null) {
            mediaContainer.removeAllViews()
        }
    }

}