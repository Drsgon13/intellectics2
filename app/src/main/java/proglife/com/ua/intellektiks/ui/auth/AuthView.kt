package proglife.com.ua.intellektiks.ui.auth

import proglife.com.ua.intellektiks.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface AuthView: BaseView {
    fun showError(message: String?)
    fun showLoading()
    fun dismissLoading()
    fun showNext()
}