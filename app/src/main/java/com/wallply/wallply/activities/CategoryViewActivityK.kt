package com.wallply.wallply.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.wallply.wallply.R
import com.wallply.wallply.adapters.WallpaperAdapter
import com.wallply.wallply.models.WallpaperModel
import kotlinx.android.synthetic.main.activity_view.*

class CategoryViewActivityK: BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        checktext.visibility = View.INVISIBLE
        tryagain.visibility = View.INVISIBLE
        tryagain.setOnClickListener { updateData() }

        val b = intent.extras
        val catName = b!!.getString("name")

        title_text.setText(catName)
        var adapter = WallpaperAdapter(this, getWallsOf(catName!!), false)

        var layoutManager = GridLayoutManager(this, 2)
        recv1.setHasFixedSize(true)
        recv1.layoutManager = layoutManager
        recv1.adapter = adapter



        frame.visibility = View.INVISIBLE

    }

    private fun getWallsOf(categoryName: String): ArrayList<WallpaperModel>{
        var arrayList = ArrayList<WallpaperModel>()
        val wallpaperModelArrayList = wallpapers
        for (i in 0 until wallpaperModelArrayList.size){
            if(wallpaperModelArrayList.get(i).getPublic_id().contains(categoryName)){
                arrayList.add(wallpaperModelArrayList.get(i))
            }
        }
        return arrayList
    }

    private fun updateData(){
        val intent: Intent = Intent(applicationContext, CategoryViewActivityK::class.java)
        startActivity(intent)
    }

}