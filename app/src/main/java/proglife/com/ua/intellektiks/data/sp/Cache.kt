package proglife.com.ua.intellektiks.data.sp

import proglife.com.ua.intellektiks.data.models.*

/**
 * Created by Evhenyi Shcherbyna on 03.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class Cache(
        var userGoodsPreview: List<GoodsPreview>? = null,
        var goodsList: MutableMap<Long, Goods?> = mutableMapOf(),
        var lessonPreviews: MutableMap<Long, List<LessonPreview>?> = mutableMapOf(),
        var lessons: MutableMap<Long, Lesson?> = mutableMapOf(),
        var help: Help? = null
)