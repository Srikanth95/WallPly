package com.wallply.wallply.adapters

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.wallply.wallply.R
import com.wallply.wallply.activities.BaseActivity

class SettingsAdapter constructor(context: Context, itemList: ArrayList<String>): BaseAdapter() {

    private val context = context
    private val itemList = itemList
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(p0: Int): String {
        return itemList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        val itemText = getItem(p0)
        val view = inflater.inflate(R.layout.settings_list_item, p2, false)
        val textView = view.findViewById(R.id.settings_item_text) as TextView
        var subTextView = view.findViewById(R.id.sub_text) as TextView

        textView.text = itemText

        if (itemText.equals(context.getString(R.string.theme))){
            subTextView.visibility = View.VISIBLE
            subTextView.setText(getThemePreference())
        } else subTextView.visibility = View.GONE


        return view
    }

    fun getThemePreference(): String{
        when(BaseActivity.getCurrentTheme()){
            1 -> return "Light"
            2 -> return "Dark"
        }

        return "System Default"
    }
}