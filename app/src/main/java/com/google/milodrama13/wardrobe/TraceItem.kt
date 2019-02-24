package com.google.milodrama13.wardrobe

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.time.Instant

@Entity(tableName = "tracing")
class TraceItem(
        @ColumnInfo(name = "timestamp")
        @NotNull
        val timestamp: Instant,
        @ColumnInfo(name="level")
        @NotNull
        val level:TracingLevel,
        @ColumnInfo(name="message")
        @NotNull
        val message:String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id:Int = 0

}