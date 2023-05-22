package com.epf.filmfolio

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.epf.filmfolio.model.Film
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val currentView = findViewById<View>(android.R.id.content).rootView

        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val searchButton = findViewById<FloatingActionButton>(R.id.search_button)
        val listButton = findViewById<Button>(R.id.film_list)

        searchButton.setOnClickListener{
            val popupView = onSearchButtonShowPopupWindowClick(currentView)

            val searchEdittext = popupView.findViewById<EditText>(R.id.search_edittext_popup)
            val searchButtonpopup = popupView.findViewById<Button>(R.id.validate_search)
            var searchedFilm = ""

            searchButtonpopup.setOnClickListener {
                searchedFilm = searchEdittext.text.toString()
                if(searchedFilm != ""){
                    val intent = Intent(this, SearchResultActivity::class.java)
                    intent.putExtra("SearchedFilm", searchedFilm)
                    startActivity(intent)
                }
            }
        }

        listButton.setOnClickListener {
            val intent = Intent(this, ListTrendingActivity::class.java)
            startActivity(intent)
        }

    }

    private fun onSearchButtonShowPopupWindowClick(view : View?) : View{
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popupwindow_search, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.elevation = 50F
//        popupView.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, -600)


        return popupView

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
//            if(item.itemId == R.id.list_films_latmenu){
//
//            }
            true
        } else super.onOptionsItemSelected(item)
    }

    fun getFilmForQuery(film : String) : String {
        return film
    }
}