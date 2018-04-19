package proglife.com.ua.intellektiks.ui.content.models

import android.widget.Button
import com.google.android.exoplayer2.ui.PlayerView

/**
 * Created by Evhenyi Shcherbyna on 18.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class PlayerViewModel(
        var show: Boolean = false,
        var playerView: PlayerView? = null,
        var downloadSize: Float = 0f
)