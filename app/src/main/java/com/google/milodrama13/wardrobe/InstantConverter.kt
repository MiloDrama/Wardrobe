package com.google.milodrama13.wardrobe

import android.arch.persistence.room.TypeConverter
import java.time.Instant

class InstantConverter {

    @TypeConverter
    fun toInstant(value:Long?):Instant?{
        if (value==null)
            return null
        return Instant.ofEpochMilli(value)
    }

    @TypeConverter
    fun fromInstant(value: Instant?):Long?{
        if (value == null)
            return null
        if (value < Instant.EPOCH)
            return 0
        return value.toEpochMilli()
    }

}