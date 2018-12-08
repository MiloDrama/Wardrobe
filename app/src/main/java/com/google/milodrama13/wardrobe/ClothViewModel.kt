package com.google.milodrama13.wardrobe

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

class ClothViewModel : ViewModel() {
    private var _cloths:LiveData<List<Cloth>>? = null

    fun getCloths(dao:MyDao, type:ClothType) : LiveData<List<Cloth>>{
        if (_cloths == null)
            _cloths = dao.getCloths(type)
        return _cloths!!
    }
}