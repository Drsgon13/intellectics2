package proglife.com.ua.intellektiks.data.sp

import proglife.com.ua.intellektiks.data.models.Goods
import proglife.com.ua.intellektiks.data.models.GoodsPreview
import proglife.com.ua.intellektiks.data.models.Lesson
import proglife.com.ua.intellektiks.data.models.LessonPreview

/**
 * Created by Evhenyi Shcherbyna on 03.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class Cache(
        var userGoodsPreview: List<GoodsPreview>? = null,
        var goodsList: MutableMap<Long, Goods?> = mutableMapOf(),
        var lessonPreviews: MutableMap<Long, List<LessonPreview>?> = mutableMapOf(),
        var lessons: MutableMap<Long, Lesson?> = mutableMapOf()
)