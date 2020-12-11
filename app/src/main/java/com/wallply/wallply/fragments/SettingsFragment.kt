package com.wallply.wallply.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wallply.wallply.R
import com.wallply.wallply.SplashK
import com.wallply.wallply.activities.BaseActivity
import com.wallply.wallply.adapters.SettingsAdapter
import kotlinx.android.synthetic.main.activity_new_main.*
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.fragment_setttings.*

class SettingsFragment: Fragment() {
    //var isSignedIn = isSignedIn
    lateinit var userName: TextView
    lateinit var profileIcon: ImageView
    lateinit var listView: ListView
    lateinit var googleSignInButton: ImageButton
    lateinit var settingsAdapter: SettingsAdapter


    // int[] data = {R.drawable.nature,R.drawable.city,R.drawable.illustration_amoled_p01,R.drawable.patterns,R.drawable.color,R.drawable.amoled,R.drawable.graphics_p03,R.drawable.gradient,R.drawable.material,R.drawable.cars};
    //  String[] names = {"Nature","City","Illustrations","Patterns","Colorful","For Amoled","Graphics","Gradients","Material","Cars"};

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_setttings, container, false)
        userName = view.findViewById(R.id.user_name) as TextView
        profileIcon = view.findViewById(R.id.profile_icon) as ImageView
        listView = view.findViewById(R.id.settings_list_view) as ListView
        googleSignInButton = view.findViewById(R.id.sign_in_button) as ImageButton


        return view
    }

    override fun onStart() {
        super.onStart()
        var itemsList = ArrayList<String>()
        itemsList.add(context!!.applicationContext.getString(R.string.theme))
        itemsList.add(context!!.getString(R.string.privacy_text))

        val user = Firebase.auth.currentUser

        if (user != null){
            itemsList.add(context!!.getString(R.string.sign_out))
            googleSignInButton.visibility = View.GONE
            userName.visibility = View.VISIBLE
            userName.text = user.displayName + "\n" + user.email
            profileIcon.setColorFilter(ContextCompat.getColor(context!!, R.color.transparent))
            profileIcon.setPadding(0, 0, 0, 0)
            Glide.with(context).load(user.photoUrl).into(profileIcon)
        } else {
            profileIcon.setImageDrawable(context!!.getDrawable(R.drawable.baseline_person_24))
            profileIcon.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAccent))
            profileIcon.setPadding(40, 35, 35, 35)
            googleSignInButton.visibility = View.VISIBLE
            userName.visibility = View.GONE
        }

        googleSignInButton.setOnClickListener {
//            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.app_client_id)).requestEmail().build()
//            var mGoogleSignInClient = GoogleSignIn.getClient(context!!, gso)
//            val intent = mGoogleSignInClient.signInIntent
//            startActivityForResult(intent, 1001)

            signIn()



        }


        settingsAdapter = SettingsAdapter(context!!, itemsList)
        listView.adapter = settingsAdapter
        listView.isClickable = true
        listView.setOnItemClickListener{ parent, view, position, id ->
            when (position) {
                0 -> {
                    showThemePreferenceDialog()
                }
                1 -> startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.privacy_url))))
                2 -> {
                    FirebaseAuth.getInstance().signOut();
                    Snackbar.make((context as Activity).findViewById(R.id.homeScreenLayout), "Signed Out.", Snackbar.LENGTH_SHORT)
                            .setAnchorView((context as Activity).findViewById(R.id.bottom_navigation))
                            .show()
                    onStart()
                }
            }
        }

    }

    fun signIn(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.app_client_id)).requestEmail().build()

        var mGoogleSignInClient = GoogleSignIn.getClient(context as Activity, gso)
        val intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent, 1002)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1002){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
               // Log.d(BaseActivity.TAG, "firebaseAuthWithGoogle: "+ account.id)
            } catch (e: ApiException){
                Log.w(BaseActivity.TAG, "Google Sign In Failed: ", e)
            }

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        var auth = Firebase.auth as FirebaseAuth
        auth.signInWithCredential(credential)
                .addOnCompleteListener(context as Activity){
                    task ->
                    if (task.isSuccessful){
                       // Log.d(BaseActivity.TAG, "signInWithCredential: success")
                        BaseActivity.mainActivityStarted = false
                        //Log.i("TAG", "firebaseAuthWithGoogle: starting spash")
                        startActivity(Intent(context, SplashK::class.java))
                        //var user = Firebase.auth.currentUser!!
                        Snackbar.make((context as Activity).findViewById(R.id.homeScreenLayout), "Signed In Successfully", Snackbar.LENGTH_SHORT)
                                .setAnchorView((context as Activity).findViewById(R.id.bottom_navigation))
                                .show()

//                        if (!(WallplyApplication.Companion.usersSnapshot.hasChild(user!!.uid)))
//                        usersDatabaseReference.child(user!!.uid).child("favourites").child("pid").setValue("true")

                        //showProgressBar()
                        //BaseActivity.usersDatabaseReference.addValueEventListener(usersValueEvenListener)
                    } else {
                      //  Log.w(BaseActivity.TAG, "signInWithCredential: failure", task.exception)
                        Snackbar.make((context as Activity).findViewById(R.id.homeScreenLayout), "Authentication Failed", Snackbar.LENGTH_SHORT)
                                .setAnchorView((context as Activity).findViewById(R.id.bottom_navigation))
                                .show()
                        //showSignIn()
                    }
                }
    }

    fun resetAndRestart(){
        BaseActivity.wallpaperDatabase.clearAllTables()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//    }


    fun showThemePreferenceDialog(){

        lateinit var alertDialog: AlertDialog

        var items: Array<CharSequence> = arrayOf("System Default", "Light", "Dark")

        var alertDialogBuilder = AlertDialog.Builder(context)
                .setTitle("Theme")
                .setSingleChoiceItems(items,
                        BaseActivity.getCurrentTheme(),
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            BaseActivity.saveCurrentTheme(i)

                            alertDialog.dismiss()
                            settingsAdapter.notifyDataSetChanged()
                            setCurrentTheme(i)
                            //context?.startActivity(Intent(context, SplashK::class.java))
//                            if(settingsAdapter != null)
//                                settingsAdapter.notifyDataSetChanged()
//                            alertDialog.dismiss()
                        })
                .setCancelable(true)

        alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }

    fun setCurrentTheme(themeCode: Int){
        when(themeCode){
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}