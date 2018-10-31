package com.example.milo.wardrobe

import android.app.Activity
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.io.File
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.*

class UpdateClothActivity : AppCompatActivity() {

    companion object {
        val RequestCode = 2
        val ResultUpdated = -1
        val ResultDeleted = -2
    }

    private var _cloth:Cloth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_cloth)

        val dao = MyDatabase.getInstance(this).getDao()
        _cloth = dao.getCloth(intent.getStringExtra("CLOTH_ID").toInt())
        if (_cloth == null){
            Snackbar.make(findViewById<CoordinatorLayout>(R.id.myCoordLayout), getString(R.string.msg_invalid_cloth), Snackbar.LENGTH_SHORT).show()
            finish()
            return
        }

        val cloth_type = findViewById<ImageView>(R.id.cloth_type)
        val cloth_picture = findViewById<ImageView>(R.id.cloth_picture)
        val cloth_use_count = findViewById<TextView>(R.id.cloth_use_count)
        val cloth_last_washed = findViewById<TextView>(R.id.cloth_last_washed)

        cloth_use_count.text = _cloth!!.useCount.toString()

        if (_cloth!!.lastWashed == null)
            cloth_last_washed.text = "Never"
        else
            cloth_last_washed.text =  SimpleDateFormat("EEE dd/MM/yyyy").format(Date.from(_cloth!!.lastWashed))

        val picture = BitmapFactory.decodeFile(_cloth!!.pictureFilePath)
        cloth_picture.setImageBitmap(picture)

        when(_cloth!!.type) {
            ClothType.SUMMER_TOP -> cloth_type.setImageResource(R.drawable.ic_short_sleeved_shirt)
            ClothType.WINTER_TOP -> cloth_type.setImageResource(R.drawable.ic_shirt)
            ClothType.PANTS -> cloth_type.setImageResource(R.drawable.ic_pants)
        }
    }

    fun wear(view: View)
    {
        _cloth!!.useCount++
        updateCloth()
        finish()
    }

    fun wash(view:View)
    {
        _cloth!!.useCount = 0
        _cloth!!.lastWashed = Instant.now()
        updateCloth()
        finish()
    }

    fun delete(view: View)
    {
        deleteCloth()
        finish()
    }

    private fun deleteCloth() {
        val dao = MyDatabase.getInstance(this).getDao()
        dao.deleteCloth(_cloth!!)
        val pictureFile = File(_cloth!!.pictureFilePath)
        pictureFile.delete()

        setResult(ResultDeleted)
    }

    private fun updateCloth() {
        val dao = MyDatabase.getInstance(this).getDao()
        dao.updateCloth(_cloth!!)

        setResult(ResultUpdated)
    }
}
