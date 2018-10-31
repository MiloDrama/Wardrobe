package com.example.milo.wardrobe

import android.arch.persistence.room.TypeConverter
import java.time.Instant
import java.util.*

class InstantConverter {

    @TypeConverter
    fun toInstant(value:Long?):Instant?{
        if (value==null)
            return null;
        return Instant.ofEpochMilli(value)
    }

    @TypeConverter
    fun fromInstant(value: Instant?):Long?{
        if (value == null)
            return null;
        return value.toEpochMilli();
    }

}