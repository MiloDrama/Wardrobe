package com.google.milodrama13.wardrobe

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddClothActivity : AppCompatActivity() {

    companion object {
        val RequestCode = 1
    }

    val CameraRequestCode: Int = 10

    private var _cameraIntent: Intent? = null
    private var _thumbnail: ImageView? = null
    private var _pictureFilePath: String? = null
    private var _cameraFilePath:String? = null

    private var _button_winter_top:ImageButton? = null
    private var _button_summer_top:ImageButton? = null
    private var _button_pants:ImageButton? = null

    private var _typeSelected = ClothType.WINTER_TOP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cloth)
        setSupportActionBar(toolbar)

        _cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        _thumbnail = findViewById(R.id.picture)
        _cameraFilePath = null
        _pictureFilePath = null

        _button_winter_top = findViewById(R.id.button_winter_top)
        _button_summer_top = findViewById(R.id.button_summer_top)
        _button_pants = findViewById(R.id.button_pants)

        if (intent.hasExtra("CLOTH_TYPE"))
        {
            when(intent.getSerializableExtra("CLOTH_TYPE") as ClothType){
                ClothType.WINTER_TOP -> selectWinterTop(_button_winter_top!!)
                ClothType.SUMMER_TOP -> selectSummerTop(_button_summer_top!!)
                ClothType.PANTS -> selectPants(_button_pants!!)
            }
        }
        else
            selectWinterTop(_button_winter_top!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_cloth, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId)
        {
            R.id.action_add_done -> addShirt()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        ClearPictureFile()
        super.onBackPressed()
    }

    private fun addShirt(): Boolean {
        val layout = findViewById<CoordinatorLayout>(R.id.myCoordLayout)
        if (_pictureFilePath == null){
            Snackbar.make(layout, getString(R.string.msg_picture_mandatory), Snackbar.LENGTH_SHORT).show()
            return false
        }

        val cloth = Cloth(_typeSelected)
        cloth.pictureFilePath = _pictureFilePath

        val dao = MyDatabase.getInstance(this).getDao()
        dao.insertCloth(cloth)

        setResult(Activity.RESULT_OK)
        finish()
        return true
    }

    fun createImageFile(): File
    {
        val timestamp:String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val filename:String = "IMG_" + timestamp +"_"
        val storageDir:File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile:File = File.createTempFile(filename, ".png", storageDir)

        _cameraFilePath = imageFile.absolutePath
        return imageFile
    }

    fun takePicture(view:View) {
        if (_cameraIntent!!.resolveActivity(packageManager) != null) {
            val imageFile:File = createImageFile()
            if (imageFile != null)
            {
                val imageUri: Uri = FileProvider.getUriForFile(this, "com.example.milo", imageFile)
                _cameraIntent!!.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            }
            startActivityForResult(_cameraIntent, CameraRequestCode)
        }
    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CameraRequestCode) {
            if (resultCode == RESULT_OK) {
                ClearPictureFile()
                if (data != null) {
                    val bitmap = data.extras["data"] as Bitmap
                    val timestamp:String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    val filename:String = "IMG_" + timestamp +"_"
                    val storageDir:File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    val pictureFile = File(storageDir, filename)
                    _pictureFilePath = pictureFile.absolutePath
                    bitmap.compress(Bitmap.CompressFormat.PNG,0,pictureFile.outputStream())
                    _thumbnail!!.setImageBitmap(bitmap)
                } else{
                    val imageFile = File(_cameraFilePath)
                    val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(imageFile))
                    _pictureFilePath = imageFile.absolutePath
                    _thumbnail!!.setImageBitmap(imageBitmap)
                }
            } else {
                val imageFile = File(_cameraFilePath)
                imageFile.delete()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun ClearPictureFile() {
        if (_pictureFilePath != null) {
            var pictureFile = File(_pictureFilePath)
            pictureFile.delete()
        }
    }

    fun selectWinterTop(view:View){
        _typeSelected = ClothType.WINTER_TOP
        _button_winter_top!!.setBackgroundResource(R.color.colorSelected)
        _button_summer_top!!.setBackgroundResource(R.color.colorWhite)
        _button_pants!!.setBackgroundResource(R.color.colorWhite)
    }

    fun selectSummerTop(view:View){
        _typeSelected = ClothType.SUMMER_TOP
        _button_winter_top!!.setBackgroundResource(R.color.colorWhite)
        _button_summer_top!!.setBackgroundResource(R.color.colorSelected)
        _button_pants!!.setBackgroundResource(R.color.colorWhite)
    }

    fun selectPants(view:View){
        _typeSelected = ClothType.PANTS
        _button_winter_top!!.setBackgroundResource(R.color.colorWhite)
        _button_summer_top!!.setBackgroundResource(R.color.colorWhite)
        _button_pants!!.setBackgroundResource(R.color.colorSelected)
    }
}
