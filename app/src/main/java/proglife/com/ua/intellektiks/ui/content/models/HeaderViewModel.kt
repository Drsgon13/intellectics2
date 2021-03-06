package proglife.com.ua.intellektiks.ui.content.models

import proglife.com.ua.intellektiks.data.models.Marker

/**
 * Created by Evhenyi Shcherbyna on 18.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class HeaderViewModel(
        var title: String = "",
        var description: String? = null,
        var markers: List<Marker>? = null
)