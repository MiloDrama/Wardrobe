package com.google.milodrama13.wardrobe

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import java.time.Instant

@Dao
interface MyDao {
    @Query("SELECT * FROM cloths WHERE type = :type")
    fun getCloths(type:ClothType) : LiveData<List<Cloth>>

    @Query("SELECT * FROM cloths WHERE id = :id")
    fun getCloth(id:Int) : Cloth

    @Query("SELECT MIN(last_washed) FROM cloths WHERE type = :type AND use_count >= :wear")
    fun getOldestLaundry(type:ClothType, wear:Int) : Instant?

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insertCloth(cloth:Cloth)

    @Update
    fun updateCloth(cloth:Cloth)

    @Delete
    fun deleteCloth(cloth:Cloth)

    @Query("DELETE FROM cloths")
    fun clear()
}