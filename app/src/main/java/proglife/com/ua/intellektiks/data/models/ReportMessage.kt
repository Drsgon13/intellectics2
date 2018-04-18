package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Evhenyi Shcherbyna on 09.04.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class ReportMessage(
        @SerializedName("id_training_lessons_message") val id: Long,
        @SerializedName("training_lessons_message") val message: String,
        @SerializedName("id_training_lessons_message_type") val type: Type,
        @SerializedName("id_training_lessons_message_status") val status: Status,
        @SerializedName("creation_date") val creationDate: Date
) {
    // 1 - написал контакт, 2 - ответил сотрудник, 3 - черновик
    enum class Type {
        @SerializedName("1") CONTACT,
        @SerializedName("2") EMPLOYEE,
        @SerializedName("3") DRAFT
    }

    // 1 - Отчет ожидает проверки, 5 - Отчёт одобрен, 2 - Отчёт отклонен
    enum class Status {
        @SerializedName("1") AWAIT_CHECK,
        @SerializedName("2") REJECTED,
        @SerializedName("5") APPROVED
    }
}