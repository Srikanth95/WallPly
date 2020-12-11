package com.wallply.wallply.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.util.Strings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.wallply.wallply.WallplyApplication
import com.wallply.wallply.models.CategoryModel
import com.wallply.wallply.models.WallpaperDatabase
import com.wallply.wallply.models.WallpaperModel
import com.wallply.wallply.utilities.CloudinaryCredentials
import com.wallply.wallply.utilities.CloudinaryJsonObjectRequest
import com.wallply.wallply.utilities.VolleySingleton
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.URL

abstract class BaseActivity: AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var auth: FirebaseAuth
    val RC_SIGN_IN = 1001
    lateinit var user: FirebaseUser



    companion object{
        val TAG = BaseActivity.javaClass.simpleName
        lateinit var sharedPreferences: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
        private val DATABASE_NAME = "wallply_db"
        lateinit var wallpaperDatabase: WallpaperDatabase

        lateinit var wallpapers: ArrayList<WallpaperModel>
        lateinit var favouriteWallpapers: ArrayList<WallpaperModel>
        lateinit var collections: ArrayList<CategoryModel>
        var firebaseFavourites = ArrayList<String>()
        var roomFavourites = ArrayList<String>()

        var adCount = 1
        lateinit var cloudinaryCredentials: CloudinaryCredentials
        lateinit var databaseReference: DatabaseReference
        lateinit var cloudinaryDatabaseReference: DatabaseReference
        lateinit var usersDatabaseReference: DatabaseReference
        lateinit var favouritesDatabaseReference: DatabaseReference
        lateinit var wallpapersDatabaseReference: DatabaseReference

        var mainActivityStarted = false

//        fun initializeFavouriteArrayList(){
//            Log.i(TAG, "initializeFavouriteArrayList: Initialiizinf afvourite array list")
//            //wallData.allImagesFavouriteArrayList = ArrayList()
//            var strings = sharedPreferences.getStringSet("FAVOURITES", null)
//
//
//            if(wallpaperDatabase.dbAccess().favouriteArrayList != null) {
//
//                if (strings != null) {
//                    Log.i(TAG, "initializeFavouriteArrayList: favourites list is not null")
//                    var arrayList = ArrayList<WallpaperModel>(wallData.getAllImagesArrayList())
//                    var favouriteArrayList = ArrayList<WallpaperModel>()
//                    var itr = strings.iterator()
//                    lateinit var temp: String
//
//                    while (itr.hasNext()) {
//                        temp = itr.next()
//                        //Log.i(TAG, "initializeFavouriteArrayList: string $temp")
//                        for (j in 0 until arrayList.size) {
//                            //Log.i(TAG, "initializeFavouriteArrayList: j - $j string - ${arrayList.get(j).getPublic_id()}")
//                            if (arrayList.get(j).getPublic_id().contains(temp)) {
//                                favouriteArrayList.add(arrayList.get(j))
//                                break
//                            }
//                            //else Log.i(TAG, "initializeFavouriteArrayList: ${arrayList.get(j).getPublic_id()} $temp ")
//                        }
//                    }
//
//                    wallData.setAllImagesFavouriteArrayList(favouriteArrayList)
//
//                } else {
//                    //Log.i(TAG, "initializeFavouriteArrayList: favourite list is null")
//                    //wallData.setAllImagesFavouriteArrayList(ArrayList<WallpaperModel>())
//                    initializeFirebaseFavouriteArrayList()
//                }
//            }
//
//        }



//        fun initializeFirebaseFavouriteArrayList(){
//            Log.i(TAG, "initializeFavouriteArrayList: Initialiizinf afvourite array list")
//            //wallData.allImagesFavouriteArrayList = ArrayList()
//
//            var fStrings = ArrayList<String>()
//
//            for (child in WallplyApplication.favouritesSnapshot.children){
//                val key = child.key.toString()
//                if (!key.equals("sid"))
//                    fStrings.add(key)
//            }
//
//            if(fStrings.isNotEmpty()){
//                Log.i(TAG, "initializeFavouriteArrayList: favourites list is not null")
//                var set: MutableSet<String?> = HashSet()
//                var arrayList = ArrayList<WallpaperModel>(wallData.getAllImagesArrayList())
//                var favouriteArrayList = ArrayList<WallpaperModel>()
//
//                for (item in fStrings){
//                    set.add(item)
//                    for (wallpaper in arrayList){
//                        if (wallpaper.getPublic_id().contains(item)){
//                            favouriteArrayList.add(wallpaper)
//                            break
//                        }
//                    }
//                }
//                wallData.setAllImagesFavouriteArrayList(favouriteArrayList)
//                editor.putStringSet("FAVOURITES", set)
//                editor.commit()
//
//            }
//
//            else {
//                wallData.setAllImagesFavouriteArrayList(ArrayList<WallpaperModel>())
//            }
//
//        }


        fun saveCurrentTheme(themeCode: Int){
            if (sharedPreferences != null) {
                editor.clear()
                editor = sharedPreferences.edit()
                editor.putInt("current_theme", themeCode)
                editor.commit()
            }



        }

        fun setCurrentTheme(themeCode: Int){
            when(themeCode){
                0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        fun getCurrentTheme(): Int{
            return if (sharedPreferences != null)
                sharedPreferences.getInt("current_theme",0)
            else 0
        }

        fun initializeFirebaseFavouriteArrayList(){
          //  Log.i(TAG, "initializeFavouriteArrayList: Initialiizinf afvourite array list")
            var fList = ArrayList<WallpaperModel>()
            for (wallpaper in wallpapers){
                if(firebaseFavourites.contains(wallpaper.name))
                    fList.add(wallpaper)
            }
            favouriteWallpapers = fList
            //wallData.allImagesFavouriteArrayList = ArrayList()



        }

        fun removeFromFavourites(name: String?) {
            if(name != null){
                for (wallpaper in favouriteWallpapers){
                    if(wallpaper.name.equals(name)){
                        //favouriteWallpapers.remove(wallpaper)
                        wallpaperDatabase.dbAccess().setFavourite(wallpaper.name, false)
                        try {
                            if (FirebaseAuth.getInstance().currentUser != null)
                                favouritesDatabaseReference?.child(wallpaper.name).removeValue()
                        } catch (e: Exception){

                        }
                        break
                    }

                }
            }

           // val currentCount = wallpaperDatabase.dbAccess().getFavouriteCount(publicId)
          //  wallpapersDatabaseReference.child(publicId).child("favourite_count").setValue(currentCount!! - 1)
        }

        fun addToFavourites(wallpaperModel: WallpaperModel?) {

            if (wallpaperModel != null) {
                favouriteWallpapers.add(wallpaperModel)
                wallpaperDatabase.dbAccess().setFavourite(wallpaperModel.name, true)

                if (FirebaseAuth.getInstance().currentUser != null)
                favouritesDatabaseReference?.child(wallpaperModel.name!!).setValue("true")

            }

         //   val currentCount = wallpaperDatabase.dbAccess().getFavouriteCount(publicId)
        //    wallpapersDatabaseReference.child(publicId).child("favourite_count").setValue(currentCount!! + 1)

        }

        fun getUniqueID(publicId: String): String{

            if(publicId.contains("/"))
                return publicId.substring(publicId.indexOf("/") + 1)
            return publicId
        }

        fun getFileNameFromUrl(url: URL): String{
            val urlString = url.file
            return urlString.substring(urlString.lastIndexOf("/")+1).split("\\?")[0].split("#")[0]
        }

        fun getWallpaper(name: String): WallpaperModel?{
            for (wallpaper in wallpapers)
                if(wallpaper.name == name)
                    return wallpaper
            return null
        }


    }

    fun updateFavourites(publicIdList: ArrayList<String>){
        for (public_id in publicIdList){
            wallpaperDatabase.dbAccess().setFavourite(public_id, true)
        }
    }


    fun setShownSignIn(boolean: Boolean){
        editor = sharedPreferences.edit()
        editor.clear()
        editor.putBoolean("shown_sign_in",boolean)
        editor.commit()
    }


    lateinit var cloudinaryJsonObjectRequest: CloudinaryJsonObjectRequest
    lateinit var allImagesJsonObjectRequest: CloudinaryJsonObjectRequest
    lateinit var allFoldersJsonObjectRequest: CloudinaryJsonObjectRequest

    val context = this@BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        wallpaperDatabase = Room.databaseBuilder(applicationContext, WallpaperDatabase::class.java, DATABASE_NAME).
                fallbackToDestructiveMigration().
                allowMainThreadQueries().
                build()



        // Initialize Firebase objects
        auth = Firebase.auth
        databaseReference = FirebaseDatabase.getInstance().getReference()
        cloudinaryDatabaseReference = databaseReference.child("cloudinary_credentials")
        usersDatabaseReference = databaseReference.child("users")
        wallpapersDatabaseReference = databaseReference.child("wallpapers")



        // Initialize Cloudinary object
        cloudinaryCredentials = CloudinaryCredentials.getInstance(context)

        // Initialize Shared preferences and editor objects
        sharedPreferences = getSharedPreferences("WALL", MODE_PRIVATE)
        editor = sharedPreferences.edit()


    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1001){
            //if (resultCode == Activity.RESULT_OK)
               // Toast.makeText(context, "Successfully applied as your wallpaper", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveImage(image: Bitmap): Uri? {
        //TODO - Should be processed in another thread
        val imagesFolder = File(cacheDir, "images")
        var uri: Uri? = null
        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "shared_image.png")
            val stream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(this, "com.wallply.wallply.fileprovider", file)
        } catch (e: IOException) {
          //  Log.d(TAG, "IOException while trying to write file for sharing: " + e.message)
        }
        return uri
    }

    var cloudinaryValueEventListener = object: ValueEventListener{
        override fun onDataChange(p0: DataSnapshot) {
            cloudinaryCredentials.setCloudName(p0.child("name").getValue(String::class.java))
            cloudinaryCredentials.setCloudinaryUsername(p0.child("username").getValue(Long::class.java).toString())
            cloudinaryCredentials.setCloudinaryPassword(p0.child("password").getValue(String::class.java))
            cloudinaryCredentials.setCloudinaryUrlPrefix(p0.child("url_prefix").getValue(String::class.java))
            cloudinaryCredentials.setCloudinaryAllImagesURL(p0.child("all_images_url").getValue(String::class.java))
            cloudinaryCredentials.setCloudinaryAllFoldersURL(p0.child("all_folders_url").getValue(String::class.java))
            cloudinaryCredentials.setCloudinarySquareTr(p0.child("square_tr").getValue(String::class.java))
            cloudinaryCredentials.setCloudinaryLandscapeTr(p0.child("landscape_tr").getValue(String::class.java))
            cloudinaryCredentials.setCloudinaryPortraitTr(p0.child("portrait_tr").getValue(String::class.java))
            //wallpaperDatabase.clearAllTables()
            fetchWallData()
        }

        override fun onCancelled(p0: DatabaseError) {
          //  Log.e(TAG, "cloudinary node onCancelled: Firebase Database Error $p0")
        }

    }

    var usersValueEvenListener = object: ValueEventListener{
        override fun onDataChange(p0: DataSnapshot) {

            WallplyApplication.Companion.usersSnapshot = p0!!

            if(p0.child(user.uid).exists()){
                val userData = p0.child(user.uid).child("favourites")
                var fStrings = ArrayList<String>()

                for (child in userData.children){
                    val key = child.key.toString()
                    if (!key.equals("sid"))
                        fStrings.add(key)
                }
                if(fStrings.isNotEmpty()){
                    firebaseFavourites = fStrings
                }
                favouritesDatabaseReference = usersDatabaseReference.child(user.uid).child("favourites")
            } else {
                usersDatabaseReference.child(user!!.uid).child("favourites").child("pid").setValue("true")
                return
            }

            cloudinaryDatabaseReference.addValueEventListener(cloudinaryValueEventListener)

        }

        override fun onCancelled(p0: DatabaseError) {
           // Log.w(TAG, " users node onCancelled: $p0")
        }
    }

//    var wallpapersValueEventListener = object : ValueEventListener{
//        override fun onDataChange(p0: DataSnapshot) {
//           WallplyApplication.wallpapersSnapshot = p0
//
//            for (child in p0.children){
//                val publicId = child.key.toString()
//                var count = child.child("favourite_count").getValue(Int::class.java)
//                if(count == null)
//                    count = 0
//                wallpaperDatabase.dbAccess().setFavouriteCount(publicId, count)
//            }
//        }
//
//        override fun onCancelled(p0: DatabaseError) {
//            Log.d(TAG, "wallpapers node onCancelled: $p0")
//        }
//    }

    var favouritesValueEventListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {

            var fStrings = ArrayList<String>()

            for (child in p0.children){
                val key = child.key.toString()
                if (!key.equals("sid"))
                    fStrings.add(key)
            }

            if(fStrings.isNotEmpty()){
                updateFavourites(fStrings)
            }

            //WallplyApplication.Companion.favouritesSnapshot = p0!!
        }

        override fun onCancelled(p0: DatabaseError) {
           // Log.w(TAG, " favourites node onCancelled: $p0")
        }
    }

    fun fetchCategories(){
        allFoldersJsonObjectRequest = CloudinaryJsonObjectRequest(Request.Method.GET,
                cloudinaryCredentials.cloudinaryAllFoldersURL,
                null,
                allFoldersJsonObjectListener,
                {})

        VolleySingleton.getInstance(this).addToRequestQueue(allFoldersJsonObjectRequest)
    }

    fun fetchWallData(){
        allImagesJsonObjectRequest = CloudinaryJsonObjectRequest(Request.Method.GET,
                cloudinaryCredentials.cloudinaryAllImagesURL,
                null,
                allImagesJsonObjectListener,
                { }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(allImagesJsonObjectRequest)
    }

    val allFoldersJsonObjectListener = Response.Listener<JSONObject> { response ->
        var arrayList = ArrayList<CategoryModel>()
        if (response != null){
            try {
                val jsonArray = response.getJSONArray("folders")
                for (i in 0 until jsonArray.length()){
                    val jsonObject = jsonArray.getJSONObject(i)
                    var categoryModel = CategoryModel()
                    categoryModel.setCategoryName(jsonObject.getString("name"))
                    categoryModel.setCategoryPath(jsonObject.getString("path"))
                    arrayList.add(categoryModel)
                }
                //wallData.allCategoriesArrayList = ArrayList()
                //wallData.setAllCategoriesArrayList(arrayList)
                collections = arrayList
                wallpaperDatabase.dbAccess().insertCategories(arrayList)
                //wallpapersDatabaseReference.addValueEventListener(wallpapersValueEventListener)
                if (!mainActivityStarted)
                startMainActivity()
            } catch (e: JSONException){
                e.printStackTrace()
            }
        }
    }

    val allImagesJsonObjectListener = Response.Listener<JSONObject> { response ->
        var arrayList = ArrayList<WallpaperModel>()

        if(response != null){
            try {
                val jsonArray = response.getJSONArray("resources")
                for (i in 0 until jsonArray.length()){

                    val jsonObject = jsonArray.getJSONObject(i)

                    var wallpaperModel = WallpaperModel()

                  //  Log.i(TAG, "jsonobject: $jsonObject")

                    wallpaperModel.setFormat(jsonObject.getString("format"))
                    wallpaperModel.setVersion(jsonObject.getInt("version"))
                    wallpaperModel.setResource_type(jsonObject.getString("resource_type"))
                    wallpaperModel.setType(jsonObject.getString("type"))
                    wallpaperModel.setBytes(jsonObject.getInt("bytes"))
                    wallpaperModel.setWidth(jsonObject.getInt("width"))
                    wallpaperModel.setHeight(jsonObject.getInt("height"))
                    wallpaperModel.setUrl(jsonObject.getString("url"))
                    wallpaperModel.setSecure_url(jsonObject.getString("secure_url"))
                    try {
                        val contextObject = jsonObject.getJSONObject("context").getJSONObject("custom")
                        wallpaperModel.title = contextObject.getString("caption")
                        wallpaperModel.externalLink = contextObject.getString("alt")
                    }catch (je: JSONException){
                        wallpaperModel.title = ""
                        wallpaperModel.externalLink = ""
                    }


                    var publicId = jsonObject.getString("public_id")
                    wallpaperModel.setPublic_id(publicId)
                    if(publicId.contains("/"))
                        wallpaperModel.setName(publicId.substring(publicId.indexOf("/") + 1))
                    else
                        wallpaperModel.setName(publicId)

                    if (auth.currentUser == null)
                    wallpaperModel.isFavourite = roomFavourites.contains(wallpaperModel.name)
                    else
                    wallpaperModel.isFavourite = firebaseFavourites.contains(wallpaperModel.name)

//                    if(!WallplyApplication.wallpapersSnapshot.child(wallpaperModel.name).exists()){
//                        wallpapersDatabaseReference.child(wallpaperModel.name).child("download_count").setValue(0)
//                        wallpapersDatabaseReference.child(wallpaperModel.name).child("favourite_count").setValue(0)
//
//                    }



                    arrayList.add(wallpaperModel)
                }
                //wallData.allImagesArrayList = ArrayList()
                //wallData.setAllImagesArrayList(arrayList)
                wallpapers = arrayList
                if(auth.currentUser != null)
                initializeFirebaseFavouriteArrayList()
                wallpaperDatabase.dbAccess().insertMultipleWallpapers(arrayList)
                fetchCategories()
            }catch (e: JSONException){
                e.printStackTrace()
            }
        }
    }


    fun startMainActivity(){
        usersDatabaseReference.removeEventListener(usersValueEvenListener)
        //Toast.makeText(context, "Starting Main Activity", Toast.LENGTH_SHORT)
        Log.i(TAG, "startMainActivity: Starting Main Activity")
        val intent = Intent(context, NewMainActivity::class.java)
        startActivity(intent)
    }

    fun isNetworkAvailable(): Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }







}