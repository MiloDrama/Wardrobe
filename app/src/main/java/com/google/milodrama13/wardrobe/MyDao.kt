package com.google.milodrama13.wardrobe

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface MyDao {
    @Query("SELECT * FROM cloths WHERE type = :type")
    fun getCloths(type:ClothType) : LiveData<List<Cloth>>

    @Query("SELECT * FROM cloths WHERE id = :id")
    fun getCloth(id:Int) : Cloth

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insertCloth(cloth:Cloth)

    @Update
    fun updateCloth(cloth:Cloth)

    @Delete
    fun deleteCloth(cloth:Cloth)

    @Query("DELETE FROM cloths")
    fun clear()
}