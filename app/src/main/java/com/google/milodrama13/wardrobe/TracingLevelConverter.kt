package com.google.milodrama13.wardrobe

import android.arch.persistence.room.TypeConverter

class TracingLevelConverter {

    @TypeConverter
    fun toTracingLevel(value:String):TracingLevel{
        return TracingLevel.valueOf(value)
    }

    @TypeConverter
    fun fromTracingLevel(value:TracingLevel):String{
        return value.name
    }

}