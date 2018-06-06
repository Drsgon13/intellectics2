package proglife.com.ua.intellektiks.data.network.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 *
 * Вот список значений:
 * getUserData - Данные пользователя
 * userGoods - Купленные товары
 * getGood - Конкретный товар (дополнительно передается параметр goodid)
 * getTrainings - Курс
 * getLessons - Уроки (дополнительно передается параметр id_training)
 * getLesson - Урок (дополнительно передается параметр id_training_lessons)
 * createLessonMessage - Создать отчет (дополнительно передается параметры id_training_lessons, training_lessons_message, id_contact)
 * getNotifications - Сообщения в лк updateNotification - Простановка статуса открыто (дополнительно передается параметр id_message)
 * getNotification - Конкретное сообщение (дополнительно передается параметр id_message)
 * getHelp - Инструкция для пользователя приложения  Еще передается параметр platform, необязательный и непонятно в каком формате. Передается в метод сбора статистики, но в бд колонка пустая.
 */
enum class ServerMethod {
    @SerializedName("getUserData") GET_USER_DATA,
    @SerializedName("getUserGoods") GET_USER_GOODS,
    @SerializedName("getGood") GET_GOODS,
    @SerializedName("getLessons") GET_LESSONS,
    @SerializedName("getLesson") GET_LESSON,
    @SerializedName("getNotifications") GET_NOTIFICATIONS,
    @SerializedName("getNotification") GET_NOTIFICATION,
    @SerializedName("createLessonMessage") CREATE_LESSON_MESSAGE,
    @SerializedName("getFavorites") GET_FAVORITES,
    @SerializedName("setFavorites") SET_FAVORITES
}