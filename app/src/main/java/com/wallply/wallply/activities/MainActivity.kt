package com.wallply.wallply.activities

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.tabs.TabLayout
import com.wallply.wallply.R
import com.wallply.wallply.fragments.AllFragment
import com.wallply.wallply.fragments.CollectionsFragment
import com.wallply.wallply.fragments.FavouritesFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseActivity() {

    companion object{
        var shownAd = false
    }

    var onComplete = object :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            Toast.makeText(p0, "Download Completed", Toast.LENGTH_SHORT).show()
        }

    }
    lateinit var interstitialAd: InterstitialAd


    private var menuItemClickListener = object : Toolbar.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            if (item != null) {
                if (item.itemId == R.id.privacy_policy)
                    startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.privacy_url))))
                return true

            }

            return false
        }

    }

    private fun setupViewPager(){
      /*  var adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(AllFragment(),"All")
        adapter.addFragment(CollectionsFragment(),"Collections")
        adapter.addFragment(FavouritesFragment(),"Favourites")
        viewpager.adapter = adapter */
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        setSupportActionBar(toolbar)
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener(menuItemClickListener)

        var request : AdRequest = AdRequest.Builder().build()
        adView.loadAd(request)

        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = resources.getString(R.string.interracial_ad_id_test)
        interstitialAd.loadAd(AdRequest.Builder().build())

        //initializeFavouriteArrayList()

        setupViewPager()
        tabs.setupWithViewPager(viewpager)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }


    override fun onDestroy() {
        unregisterReceiver(onComplete)
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (interstitialAd.isLoaded && !shownAd){
            interstitialAd.show()
            shownAd = true
            finishAffinity()
        } else
            finishAffinity()
    }

//    internal class ViewPagerAdapter(manager: FragmentManager?) : FragmentPagerAdapter(manager) {
//        private val mFragmentList: MutableList<Fragment> = ArrayList()
//        private val mFragmentTitleList: MutableList<String> = ArrayList()
//        override fun getItem(position: Int): Fragment {
//            return mFragmentList[position]
//        }
//
//        override fun getCount(): Int {
//            return mFragmentList.size
//        }
//
//        fun addFragment(fragment: Fragment, title: String) {
//            mFragmentList.add(fragment)
//            mFragmentTitleList.add(title)
//        }
//
//        override fun getPageTitle(position: Int): CharSequence? {
//            return mFragmentTitleList[position]
//        }
//    }









}