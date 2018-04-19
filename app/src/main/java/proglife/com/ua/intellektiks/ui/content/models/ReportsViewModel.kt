package proglife.com.ua.intellektiks.ui.content.models

import proglife.com.ua.intellektiks.data.models.ReportMessage

/**
 * Created by Evhenyi Shcherbyna on 17.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class ReportsViewModel(
        var show: Boolean = false,
        var messages: List<ReportMessage> = emptyList(),
        var draft: String = ""
) {
    fun getStatus(): ReportMessage.Status? {
        return when {
            messages.isEmpty() -> null
            else -> messages.last().status
        }
    }
}