package com.google.milodrama13.wardrobe

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class ClothsActivity : AppCompatActivity() {

    private var _button: Button?=null

    private var _recycler: RecyclerView? = null
    private var _adapter: RecyclerAdapter? = null
    private var _viewModel: ClothViewModel? = null

    private var _type:ClothType = ClothType.WINTER_TOP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloths)
        setSupportActionBar(toolbar)

        _type = intent.getSerializableExtra("CLOTH_TYPE") as ClothType

        _button = findViewById(R.id.button_cloth_type)
        var text:Int
        var drawable:Int
        when(_type){
            ClothType.WINTER_TOP ->{
                text = R.string.winter_tops
                drawable = R.drawable.ic_shirt
            }
            ClothType.SUMMER_TOP ->{
                text = R.string.summer_tops
                drawable = R.drawable.ic_short_sleeved_shirt
            }
            ClothType.PANTS ->{
                text = R.string.pants
                drawable = R.drawable.ic_pants
            }
        }
        _button!!.setText(text)
        _button!!.setCompoundDrawablesWithIntrinsicBounds(drawable,0,0,0)

        val dao = MyDatabase.getInstance(this).getDao()
        _viewModel = ViewModelProviders.of(this).get(ClothViewModel::class.java) as ClothViewModel

        _recycler = findViewById(R.id.recycler)
        val _layoutMgr = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        _recycler!!.layoutManager = _layoutMgr

        val itemTouchHelper = ItemTouchHelper(SwipeController())
        itemTouchHelper.attachToRecyclerView(_recycler)

        val observer = android.arch.lifecycle.Observer<List<Cloth>> { list ->
                if (_recycler!!.adapter == null) {
                    _adapter = RecyclerAdapter(list!!)
                    _recycler!!.adapter = _adapter
                } else {
                    _adapter!!.update(list!!)
                }
        }
        _viewModel!!.getCloths(dao, _type).observe(this, observer)
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
        val intent = Intent(this, AddClothActivity::class.java).apply {
            putExtra("CLOTH_TYPE", _type)
        }
        startActivityForResult(intent, AddClothActivity.RequestCode)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            AddClothActivity.RequestCode -> kotlin.run {
                if (resultCode == Activity.RESULT_OK)
                    Snackbar.make(findViewById<CoordinatorLayout>(R.id.myCoordLayout), getString(R.string.msg_new_cloth), Snackbar.LENGTH_SHORT).show()
            }
            UpdateClothActivity.RequestCode -> kotlin.run {
                when (resultCode){
                    UpdateClothActivity.ResultUpdated -> Snackbar.make(findViewById(R.id.myCoordLayout), getString(R.string.msg_cloth_updated), Snackbar.LENGTH_SHORT).show()
                    UpdateClothActivity.ResultDeleted -> Snackbar.make(findViewById(R.id.myCoordLayout), getString(R.string.msg_cloth_deleted), Snackbar.LENGTH_SHORT).show()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun updateCloth(view: View)
    {
        val cloth_id = view.findViewById<TextView>(R.id.cloth_id);

        val intent = Intent(this, UpdateClothActivity::class.java).apply {
            putExtra("CLOTH_ID", cloth_id.text)
        }
        startActivityForResult(intent, UpdateClothActivity.RequestCode)
    }
}
