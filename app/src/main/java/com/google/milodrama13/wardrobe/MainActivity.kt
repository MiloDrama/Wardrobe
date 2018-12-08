package com.google.milodrama13.wardrobe

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("WASH", getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = getString(R.string.channel_description)
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_add_cloth -> addCloth()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addCloth(): Boolean {
        val intent = Intent(this, AddClothActivity::class.java)
        startActivityForResult(intent, AddClothActivity.RequestCode)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AddClothActivity.RequestCode && resultCode == RESULT_OK) {
            Snackbar.make(findViewById<CoordinatorLayout>(R.id.myCoordLayout), getString(R.string.msg_new_cloth), Snackbar.LENGTH_SHORT).show()
        }
        else
            super.onActivityResult(requestCode, resultCode, data)
    }

    fun showPants(view:View){
        val intent = Intent(this, ClothsActivity::class.java).apply {
            putExtra("CLOTH_TYPE", ClothType.PANTS)
        }
        startActivity(intent)
    }

    fun showSummerTops(view:View){
        val intent = Intent(this, ClothsActivity::class.java).apply {
            putExtra("CLOTH_TYPE", ClothType.SUMMER_TOP)
        }
        startActivity(intent)
    }

    fun showWinterTops(view:View){
        val intent = Intent(this, ClothsActivity::class.java).apply {
            putExtra("CLOTH_TYPE", ClothType.WINTER_TOP)
        }
        startActivity(intent)
    }

}
