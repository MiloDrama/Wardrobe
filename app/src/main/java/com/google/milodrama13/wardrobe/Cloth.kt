package com.google.milodrama13.wardrobe

import android.arch.persistence.room.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.jetbrains.annotations.NotNull
import java.time.Instant

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
    @ColumnInfo(name="last_wear")
    var lastWear: Instant? = null

    fun geBitmap(height:Int): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(pictureFilePath, options)
        //val scale = options.outHeight/96
        //val scale = Math.min(options.outWidth/cloth_picture.measuredWidth, options.outHeight/cloth_picture.measuredHeight)
        //val scale = Math.min(4*options.outWidth/(cloth_picture.parent.parent as View).width, options.outHeight/(cloth_picture.parent.parent as View).height)
        var scale = 1
        val halfHeight = options.outHeight / 2
        while (halfHeight / scale >= height)
            scale *= 2
        options.inJustDecodeBounds = false
        options.inSampleSize = scale
        val picture = BitmapFactory.decodeFile(pictureFilePath, options)
        return picture
    }


}