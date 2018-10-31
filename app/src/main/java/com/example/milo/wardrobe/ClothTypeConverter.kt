package com.example.milo.wardrobe

import android.arch.persistence.room.TypeConverter

class ClothTypeConverter {

    @TypeConverter
    fun toClothType(value:String):ClothType{
        return ClothType.valueOf(value)
    }

    @TypeConverter
    fun fromClothType(value:ClothType):String{
        return value.name
    }

}