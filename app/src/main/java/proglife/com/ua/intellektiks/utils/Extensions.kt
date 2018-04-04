package proglife.com.ua.intellektiks.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Evhenyi Shcherbyna on 03.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
fun ViewGroup.inflate(layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}