package com.wallply.wallply

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wallply.wallply.activities.BaseActivity
import com.wallply.wallply.activities.NewMainActivity
import com.wallply.wallply.models.CategoryModel
import com.wallply.wallply.models.WallpaperModel
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.Exception

class SplashK: BaseActivity(), View.OnClickListener {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Set the visibility of the views
        progress_bar.visibility = View.GONE
        retry.visibility = View.GONE
        error_info.visibility = View.GONE
        sign_in_button.visibility = View.GONE
        skip.visibility = View.GONE




        //Thread(Runnable { setCurrentTheme(getCurrentTheme()) }).run()


        // Customize progress bar
        progress_bar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)

        sign_in_button.setOnClickListener(this)
        skip.setOnClickListener(this)
        retry.setOnClickListener(this)







        //Log.i(TAG, "onCreate: spashStarted")

        //checkConnection()



    }

    override fun onStart() {
        super.onStart()
        GetDataFromRoom(this).execute()
    }


    override fun onClick(p0: View?) {
        when(p0){
            sign_in_button -> signIn()
            skip -> {
                showProgressBar()
                cloudinaryDatabaseReference.addValueEventListener(cloudinaryValueEventListener) }
            retry -> checkConnection(false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
               // d(TAG, "firebaseAuthWithGoogle: "+ account.id)
            } catch (e: ApiException){
                Log.w(TAG, "Google Sign In Failed: ", e)
            }

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this){
                    task ->
                    if (task.isSuccessful){
                       // Log.d(TAG, "signInWithCredential: success")
                        user = auth.currentUser!!
                        Snackbar.make(coordinatorLayout, "Authentication Success user: ${user?.displayName}", Snackbar.LENGTH_SHORT).show()
//                        if (!(WallplyApplication.Companion.usersSnapshot.hasChild(user!!.uid)))
//                        usersDatabaseReference.child(user!!.uid).child("favourites").child("pid").setValue("true")

                        showProgressBar()
                        usersDatabaseReference.addValueEventListener(usersValueEvenListener)
                    } else {
                        Log.w(TAG, "signInWithCredential: failure", task.exception)
                        Snackbar.make(coordinatorLayout, "Authentication Failed", Snackbar.LENGTH_SHORT).show()
                        showSignIn()
                    }
                }
    }

    fun shownSignIn(): Boolean{
        return sharedPreferences.getBoolean("shown_sign_in", false)
    }

    fun signIn(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.app_client_id)).requestEmail().build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun checkConnection(proceedToMainActivity: Boolean) {
        if (isNetworkAvailable()) {
            if (auth.currentUser == null) {
                if (!shownSignIn()) {
                    showSignIn()

                    //usersDatabaseReference.addValueEventListener(usersValueEvenListener)



                } else {

                    showProgressBar()


                   Thread(Runnable { cloudinaryDatabaseReference.addValueEventListener(cloudinaryValueEventListener)
//                       if (proceedToMainActivity) {
//                           startActivity(Intent(this, NewMainActivity::class.java))
//                       }
                   }) .start()



                }
            } else {
                    showProgressBar()
                    user = auth.currentUser!!
//                usersDatabaseReference.addValueEventListener(usersValueEvenListener)
//
//                favouritesDatabaseReference = usersDatabaseReference.child(auth.currentUser!!.uid).child("favourites")
//                favouritesDatabaseReference.addValueEventListener(favouritesValueEventListener)
//                checkConnection()
                    usersDatabaseReference.addValueEventListener(usersValueEvenListener)
                }


                //wallpapersDatabaseReference.addValueEventListener(wallpapersValueEventListener)

                //cloudinaryDatabaseReference.addValueEventListener(cloudinaryValueEventListener)
            } else {
                showRetry()
            }
        }


    class GetDataFromRoom(val context: SplashK): AsyncTask<Void, Void, Boolean>(){
        override fun doInBackground(vararg p0: Void?): Boolean {
            try {
                favouriteWallpapers = wallpaperDatabase.dbAccess().favouriteArrayList as ArrayList<WallpaperModel>
                wallpapers =  wallpaperDatabase.dbAccess().wallpapersList as ArrayList<WallpaperModel>
                collections = wallpaperDatabase.dbAccess().categories as ArrayList<CategoryModel>
                return true
            } catch (e: Exception){
               // Log.i(TAG, "doInBackground: Error fetching data from Database $e")
            }

            return false
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            if (result != null){
                if (result){
                  //  Log.i(TAG, "onPostExecute: roomd result is true")
                    if(wallpapers.size > 10){
                     //   Log.i(TAG, "onPostExecute: roomd wallpapers size > 10")
                        for (item in favouriteWallpapers){
                            roomFavourites.add(item.name)
                          //  Log.d(TAG, "onPostExecute: fetchiinigFavourites: ${item.name}")
                        }
                            
                        context.checkConnection(true)
                    } else {
                        //Log.i(TAG, "onPostExecute: roomd wallpapers size < 10, actual size: ${wallpapers.size}")
                        context.checkConnection(false)
                    }
                } else{
                  //  Log.i(TAG, "onPostExecute: roomd result is false")
                    context.checkConnection(false)
                }

            } else{
              //  Log.i(TAG, "onPostExecute: roomd result is null")
                context.checkConnection(false)
            }

        }
    }

    fun showProgressBar(){
        retry.visibility = View.GONE
        sign_in_button.visibility = View.GONE
        skip.visibility = View.GONE
        error_info.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE

    }

    private fun showRetry(){
        retry.visibility = View.VISIBLE
        sign_in_button.visibility = View.GONE
        skip.visibility = View.GONE
        error_info.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
    }



    private fun showSignIn(){
        setShownSignIn(true)
        progress_bar.visibility = View.GONE
        retry.visibility = View.GONE
        error_info.visibility = View.GONE
        skip.visibility = View.VISIBLE
        sign_in_button.visibility = View.VISIBLE
    }


}