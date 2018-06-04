package proglife.com.ua.intellektiks.ui.remember

import proglife.com.ua.intellektiks.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 04.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface ForgotView: BaseView {
    fun showLoading()
    fun dismissLoading()
    fun showError(message: String?)
    fun showSuccess(email: String)
}