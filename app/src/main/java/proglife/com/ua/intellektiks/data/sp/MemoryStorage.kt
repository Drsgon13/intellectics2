package proglife.com.ua.intellektiks.data.sp

import proglife.com.ua.intellektiks.data.models.UserData

/**
 * Created by Evhenyi Shcherbyna on 03.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class MemoryStorage(
        var credentials: Pair<String?, String?> = Pair(null, null),
        var userData: UserData? = null
)