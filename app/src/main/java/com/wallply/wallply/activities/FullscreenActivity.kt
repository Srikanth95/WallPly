package com.wallply.wallply.activities

import android.Manifest
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.ads.AdRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.wallply.wallply.R
import com.wallply.wallply.WallplyApplication
import kotlinx.android.synthetic.main.activity_fullscreen.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

class FullscreenActivity: BaseActivity() {

    lateinit var progressDialog1: ProgressDialog
    lateinit var progressDialog: ProgressDialog
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var imgURL: String; lateinit var name: String; lateinit var publicId: String
    lateinit var oImgUrl: String
    var isFavourite = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        var policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        var request = AdRequest.Builder().build()
        adView.loadAd(request)

        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference




        progressDialog1 = ProgressDialog(this)
        progressDialog1.setMessage("Please wait...")
        progressDialog1.isIndeterminate = true
        progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER)

        val bundle = intent.extras!!

        name = bundle.getString("name")!!
        imgURL = bundle.getString("image_url")!!
        oImgUrl = bundle.getString("o_image_url")!!
        publicId = bundle.getString("public_id")!!
        isFavourite = bundle.getBoolean("is_favourite")


        val wallpaper = Companion.getWallpaper(name)
        try {
            if (!wallpaper!!.title.isNullOrEmpty()){
                photo_by.setText("Image by: ${wallpaper.title}")
                external_link.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(wallpaper.externalLink)))
                }
            } else{
                photo_by.visibility = View.GONE
                external_link.visibility = View.GONE
            }
        } catch (ne: java.lang.NullPointerException){
            photo_by.visibility = View.GONE
            external_link.visibility = View.GONE
        }


        if (isFavourite)
            favourite_button.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart_fill))
        else
            favourite_button.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart))

        favourite_button.setOnClickListener {
            if (isFavourite){
                favourite_button.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart))
                isFavourite = false
                removeFromFavourites(Companion.getWallpaper(name)?.name)
                favouriteWallpapers.remove(Companion.getWallpaper(name))
            } else {
                favourite_button.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart_fill))
                isFavourite = true
                addToFavourites(Companion.getWallpaper(name))
            }

        }

        //publicId = getUniqueID(publicId)

        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)

        Glide.with(applicationContext)
                .load(getPortraitUrl(oImgUrl))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(fullimage)

        setWallpaper.setOnClickListener(setWallpaperClickListener)

//        download.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(p0: View?) {
//                if (hasStoragePermission())
//                    downloadData(oImgUrl, name)
//                else
//                    requestStoragePermission()
//            }
//
//        })



    }

    private var setWallpaperClickListener = object: View.OnClickListener {
        override fun onClick(p0: View?) {
//            progressDialog = ProgressDialog(this@FullscreenActivity)
//            progressDialog.setMessage("Applying wallpaper...")
//            progressDialog.isIndeterminate = true
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
//            progressDialog.show()

            SetWallpaper(context, contentResolver).execute(URL(oImgUrl))

//            try {
//
//
//                //var url = URL(oImgUrl)
//                //var image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//
//                val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//                val path = "content://${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}" +  "/WallPly/Temp/${getFileNameFromUrl(URL(oImgUrl))}"
//                scanIntent.setData(Uri.parse(path))
//                sendBroadcast(scanIntent)
//
//                Log.i(TAG, "onClick: setwallpaper path $path")
//
//                val path2 = File(filesDir, getFileNameFromUrl(URL(oImgUrl)))
//
//                var image2 = BitmapFactory.decodeFile(path2.absolutePath)
//
//                //var intent = Intent(Intent.ACTION_SET_WALLPAPER)
//                //startActivity(Intent.createChooser(intent, "Select Wallpaper"))
//
//                var wallpaperManager = WallpaperManager.getInstance(this@FullscreenActivity)
//                wallpaperManager.setBitmap(image2)
//
//                Toast.makeText(this@FullscreenActivity, "Successfully applied as your wallpaper", Toast.LENGTH_SHORT).show()
//            } catch (e: IOException){
//
//            }
        }
    }


    class SetWallpaper(val context: BaseActivity, val contentResolver: ContentResolver): AsyncTask<URL, Void, Bitmap>() {

        lateinit var progressDialog: AlertDialog

        override fun onPreExecute() {

            val builder = AlertDialog.Builder(context)

            progressDialog = builder.create()

            var progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
            progressBar.isIndeterminate = true
            progressBar.setPadding(50, 10, 50, 10)


            progressDialog.setView(progressBar)
            progressDialog.setMessage("Applying wallpaper...")
            progressDialog.show()
        }



        override fun doInBackground(vararg p0: URL?): Bitmap? {
            try {
                var image = BitmapFactory.decodeStream(p0[0]!!.openConnection().getInputStream())

                return image

            } catch (e: NullPointerException){

            }

            return null

        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null){


                val uri = context.saveImage(result)


                var intent = Intent(Intent.ACTION_ATTACH_DATA)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.setDataAndType(uri, "image/*")
                intent.putExtra("mimeType", "image/*")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                progressDialog.dismiss()
                context.startActivityForResult(intent,  1001)

            }
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: kotlin.Array<out String>, grantResults: IntArray) {
//        if (!hasStoragePermission())
//            alertShow()
//        else {
//            downloadData(oImgUrl, name)
//        }
//    }
    fun getPortraitUrl(url: String): String{
        val prefix = url.substring(0, url.indexOf("upload") + 6)
        val postfix = url.substring(url.indexOf("upload") + 6)
        val middle = cloudinaryCredentials.cloudinaryPortraitTr
       // Log.i(TAG, "getPortraitUrl: after adding transformation $prefix$middle$postfix")

        return prefix+middle+postfix
    }

//    fun downloadLocally(url: String, fileName: String){
//
//        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show()
//
//        val count = WallplyApplication.wallpapersSnapshot.child(publicId).child("download_count").getValue(Int::class.java)
//
//        wallpapersDatabaseReference.child(publicId).child("download_count").setValue(count!! + 1)
//
//
//
//        var downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
//        var request = DownloadManager.Request(Uri.parse(url))
//
//        request.setTitle(fileName)
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//        request.setVisibleInDownloadsUi(true)
//        request.allowScanningByMediaScanner()
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "/WallPly/$fileName")
//
//        downloadManager.enqueue(request);
//
//    }
//    fun downloadData(url: String, fileName: String){
//
//        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show()
//
//        val count = WallplyApplication.wallpapersSnapshot.child(publicId).child("download_count").getValue(Int::class.java)
//
//        wallpapersDatabaseReference.child(publicId).child("download_count").setValue(count!! + 1)
//
//
//
//        var downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
//        var request = DownloadManager.Request(Uri.parse(url))
//
//        request.setTitle(fileName)
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//        request.setVisibleInDownloadsUi(true)
//        //request.allowScanningByMediaScanner()
//
//        //request.setDestinationUri(Uri.fromFile(File(filesDir, fileName)))
//        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PICTURES, "/WallPly/Temp/$fileName")
//        //val filename = getFileNameFromUrl(imgURL)
//        //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "/WallPly/Temp/${getFileNameFromUrl(URL(oImgUrl))}")
//
//        downloadManager.enqueue(request);
//
//    }




    private fun alertShow(){

            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Permission Required")
            alertDialog.setMessage("This app requires Storage permission to download wallpapers")
            alertDialog.setPositiveButton("Grant", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    requestStoragePermission()
                }
            })
            alertDialog.setNegativeButton("Close", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    p0?.dismiss()
                    onBackPressed()
                }
            })
            alertDialog.show()

    }

    private fun hasStoragePermission(): Boolean{
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    fun requestStoragePermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100);
    }




}

