package proglife.com.ua.intellektiks.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.util.*

/**
 * Created by Evhenyi Shcherbyna on 27.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class DateTypeAdapter: TypeAdapter<Date>() {

    override fun write(out: JsonWriter?, value: Date?) {
        out?.let {
            it.nullValue()
            return
        }
        out?.value(value?.time)
    }

    override fun read(reader: JsonReader?): Date? {
        if (reader?.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }
        return null
    }

}