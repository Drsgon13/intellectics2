package proglife.com.ua.intellektiks.ui.profile

import proglife.com.ua.intellektiks.data.models.UserData
import proglife.com.ua.intellektiks.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 28.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface ProfileView: BaseView {
    fun showData(userData: Pair<String?, UserData?>)
}