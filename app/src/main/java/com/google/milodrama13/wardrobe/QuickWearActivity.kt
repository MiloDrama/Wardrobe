package com.google.milodrama13.wardrobe

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.GridView
import android.widget.ImageButton
import java.time.Instant

class QuickWearActivity : AppCompatActivity() {

    private var _gridView: GridView? = null
    private var _adapter : GridAdapter? = null
    private var _viewModel: ClothViewModel? = null

    private var _button_winter_top: ImageButton? = null
    private var _button_summer_top: ImageButton? = null
    private var _button_pants: ImageButton? = null

    private var _dao:MyDao? = null
    private var _observer:Observer<List<Cloth>>? = null

    private var _type:ClothType = ClothType.WINTER_TOP


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_wear)

        _dao = MyDatabase.getInstance(this).getDao()
        _viewModel = ViewModelProviders.of(this).get(ClothViewModel::class.java) as ClothViewModel

        _gridView = findViewById(R.id.gridview)
        _gridView!!.setOnItemClickListener {
            parent, view, position, id ->  kotlin.run {
            val cloth = _viewModel!!.getCloth(position)
            cloth.useCount++
            cloth.lastWear = Instant.now()
            _dao!!.updateCloth(cloth)
            setResult(UpdateClothActivity.ResultUpdated)
            finish()
        }}

        _observer = android.arch.lifecycle.Observer<List<Cloth>> { list ->
            if (_gridView!!.adapter == null) {
                _adapter = GridAdapter(list!!)
                _gridView!!.adapter = _adapter
            } else {
                _adapter!!.update(list!!)
                //_gridView!!.invalidateViews()
            }
        }

        _button_winter_top = findViewById(R.id.button_winter_top)
        _button_summer_top = findViewById(R.id.button_summer_top)
        _button_pants = findViewById(R.id.button_pants)

        when(_type){
            ClothType.SUMMER_TOP -> displaySummerTop(_button_summer_top!!)
            ClothType.WINTER_TOP -> displayWinterTop(_button_winter_top!!)
            ClothType.PANTS -> displayPants(_button_pants!!)
        }
    }

    fun displayWinterTop(view: View){
        _type = ClothType.WINTER_TOP
        _button_winter_top!!.setBackgroundResource(R.color.colorSelected)
        _button_summer_top!!.setBackgroundResource(R.color.colorWhite)
        _button_pants!!.setBackgroundResource(R.color.colorWhite)
        _viewModel!!.getCloths(_dao!!, _type).observe(this, _observer!!)
    }

    fun displaySummerTop(view: View){
        _type = ClothType.SUMMER_TOP
        _button_winter_top!!.setBackgroundResource(R.color.colorWhite)
        _button_summer_top!!.setBackgroundResource(R.color.colorSelected)
        _button_pants!!.setBackgroundResource(R.color.colorWhite)
        _viewModel!!.getCloths(_dao!!, _type).observe(this, _observer!!)
    }

    fun displayPants(view: View){
        _type = ClothType.PANTS
        _button_winter_top!!.setBackgroundResource(R.color.colorWhite)
        _button_summer_top!!.setBackgroundResource(R.color.colorWhite)
        _button_pants!!.setBackgroundResource(R.color.colorSelected)
        _viewModel!!.getCloths(_dao!!, _type).observe(this, _observer!!)
    }


}