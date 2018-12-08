package com.google.milodrama13.wardrobe

import android.arch.persistence.room.*
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

}