package com.wallply.wallply.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.snackbar.Snackbar
import com.wallply.wallply.R
import com.wallply.wallply.fragments.AllFragment
import com.wallply.wallply.fragments.CollectionsFragment
import com.wallply.wallply.fragments.FavouritesFragment
import com.wallply.wallply.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_new_main.*
import kotlinx.android.synthetic.main.fragment_all.recv1

class NewMainActivity : BaseActivity() {

    lateinit var allFragment: AllFragment
    lateinit var collectionsFragment: CollectionsFragment
    lateinit var favouritesFragment: FavouritesFragment
    lateinit var settingsFragment: SettingsFragment
    var currentFragmentId: Int = R.id.all_wallpapers

    companion object{
        var shownAd = false
    }

    lateinit var interstitialAd: InterstitialAd


    private fun selectFragment(fragment: Fragment) =
            supportFragmentManager.beginTransaction().apply {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                replace(R.id.fl_fragment, fragment)

            commit()
        }

    var favouriteAddedReceiver = object: BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            Snackbar.make(homeScreenLayout, "Added to your Favourites!", Snackbar.LENGTH_SHORT)
                    .setAnchorView(bottom_navigation)
                    .show()
        }

    }

    var favouriteRemovedReceiver = object: BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            Snackbar.make(homeScreenLayout, "Removed from your Favourites.", Snackbar.LENGTH_SHORT)
                    .setAnchorView(bottom_navigation)
                    .show()
        }

    }


//    private fun setupViewPager(){
//        var adapter = ViewPagerAdapter(supportFragmentManager)
//        adapter.addFragment(AllFragment(),"All")
//        adapter.addFragment(CollectionsFragment(),"Collections")
//        adapter.addFragment(FavouritesFragment(),"Favourites")
//        viewpager.adapter = adapter
//    }

    override fun onStop() {
        super.onStop()
        mainActivityStarted = false
        try{
            unregisterReceiver(favouriteAddedReceiver)
            unregisterReceiver(favouriteRemovedReceiver)
        } catch (e: Exception){

        }

    }


    override fun onResume() {
        super.onResume()
        mainActivityStarted = true
        registerReceiver(favouriteAddedReceiver, IntentFilter().apply { addAction("FAVOURITE_ADDED") })
        registerReceiver(favouriteRemovedReceiver, IntentFilter().apply { addAction("FAVOURITE_REMOVED") })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_main)

        //registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        supportActionBar?.hide()
        mainActivityStarted = true
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        toolbar.inflateMenu(R.menu.main_menu)
//        toolbar.setOnMenuItemClickListener(menuItemClickListener)

//        var request : AdRequest = AdRequest.Builder().build()
//        adView.loadAd(request)

        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = resources.getString(R.string.interstitial_ad_id)
        interstitialAd.loadAd(AdRequest.Builder().build())

        //initializeFirebaseFavouriteArrayList()

        allFragment = AllFragment()
        collectionsFragment = CollectionsFragment()
        favouritesFragment = FavouritesFragment()
        settingsFragment = SettingsFragment()

        bottom_navigation?.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.all_wallpapers -> selectFragment(allFragment)
                R.id.collections -> selectFragment(collectionsFragment)
                R.id.favourites -> selectFragment(favouritesFragment)
                R.id.settings -> selectFragment(settingsFragment)
            }
            true
        }

        bottom_navigation.selectedItemId = currentFragmentId

        bottom_navigation?.setOnNavigationItemReselectedListener {
            when(it.itemId){
                R.id.all_wallpapers -> {
                    selectFragment(allFragment)
                    if (recv1 != null){
                        recv1.smoothScrollToPosition(0)
                        //allFragment.adapter.notifyDataSetChanged()
                    }

                }
//                R.id.collections -> {
//                    selectFragment(collectionsFragment)
//                    if (recv1 != null){
//                        recv1.smoothScrollToPosition(0)
//                        //allFragment.adapter.notifyDataSetChanged()
//                    }
//
//                }
//                R.id.favourites -> {
//                    //selectFragment(favouritesFragment)
//
//                }

                R.id.settings -> selectFragment(settingsFragment)
            }
        }

        //bottom_navigation?.selectedItemId = R.id.all_wallpapers

        //setupViewPager()
        //tabs.setupWithViewPager(viewpager)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }


    override fun onDestroy() {
        //unregisterReceiver(onComplete)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentFragmentId", bottom_navigation.selectedItemId)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentFragmentId = savedInstanceState.getInt("currentFragmentId",R.id.all_wallpapers)
        bottom_navigation.selectedItemId = currentFragmentId
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