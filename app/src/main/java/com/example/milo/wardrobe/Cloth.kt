package com.example.milo.wardrobe

import android.arch.persistence.room.*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Picture
import android.support.annotation.NonNull
import android.support.v4.content.FileProvider
import org.jetbrains.annotations.NotNull
import java.io.File
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.*

@Entity(tableName = "cloths")
class Cloth(
        @ColumnInfo(name="type")
        @NotNull
        val type:ClothType) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id:Int = 0

    @ColumnInfo(name="picture_file_path")
    var pictureFilePath: String? = null
    @ColumnInfo(name="use_count")
    var useCount:Int = 0
    @ColumnInfo(name="last_washed")
    var lastWashed: Instant? = null

}