package proglife.com.ua.intellektiks.ui.viewer.media

import proglife.com.ua.intellektiks.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 16.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface ViewerMediaView: BaseView {
    fun initPlayer(authData: String?)
}