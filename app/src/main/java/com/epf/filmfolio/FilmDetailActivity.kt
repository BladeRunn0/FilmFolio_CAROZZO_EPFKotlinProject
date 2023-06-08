package com.epf.filmfolio

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.epf.filmfolio.model.Film
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Text
import java.io.File
import java.io.OutputStreamWriter

class FilmDetailActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filmdetail)

        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        val currentView = findViewById<View>(android.R.id.content).rootView

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val latMenu = findViewById<NavigationView>(R.id.lat_menu)
        latMenu.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.favorites -> startActivity(Intent(this, FavoritesActivity::class.java))
                R.id.search -> {
                    drawerLayout.close()
                    searchPopup(currentView)
                }
                R.id.qr_scan -> startActivity(Intent(this, QRScannerActivity::class.java))
                R.id.about -> startActivity(Intent(this, AboutActivity::class.java))
            }
            true
        }

        this.recyclerView = findViewById<RecyclerView>(R.id.list_films)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val filmId = this.intent.getIntExtra("Film", 0)

        val filmsAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(DatabaseService::class.java)

        runBlocking {
            val film = filmsAPI.getFilmById(filmId)
            val similarFilms = filmsAPI.getSimilarFilms(filmId)

            val favorite = findViewById<CheckBox>(R.id.favorite)
            checkExistingFav(favorite, film.id.toString())
            favorite.setOnClickListener {
                if(favorite.isChecked){
                    saveFavToFile(film.id.toString(), "filmFavorites")
                }else{
                    deleteFavOnFile(film.id.toString(), "filmFavorites")
                }
            }


            val filmName = findViewById<TextView>(R.id.film_name)
            filmName.text = film.title
            if (film.title == null) filmName.text = film.name

            val tagline = findViewById<TextView>(R.id.film_tagline)
            if(film.tagline != null){
                tagline.text = film.tagline
            }


            val filmGenres = findViewById<TextView>(R.id.film_genres)
            if(film.genres != null){
                filmGenres.text = film.genres.toString().replace("[", "").replace("]","")
            }else{
                filmGenres.text = "Unknown"
            }

            val collection = findViewById<TextView>(R.id.collection)
            val space = findViewById<Space>(R.id.space)
            collection.visibility = View.GONE
            space.visibility = View.GONE

            if(film.belongs_to_collection != null){
                collection.visibility = View.VISIBLE
                collection.text = film.belongs_to_collection["name"].toString()
                space.visibility = View.VISIBLE
            }

            val duration = findViewById<TextView>(R.id.film_duration)
            if(film.runtime != null){
                duration.text = film.runtime.toString() + "min"
            }

            val synopsis = findViewById<TextView>(R.id.film_synopsis)
            synopsis.text = film.overview

            val languages = findViewById<TextView>(R.id.languages)
            languages.text = film.spoken_languages.toString().replace("[", "").replace("]","")

            val status = findViewById<TextView>(R.id.film_status)
            status.text = film.status

            if(film.backdrop_path != null){
                Glide.with(this@FilmDetailActivity)
                    .load("https://image.tmdb.org/t/p/original/" + film.backdrop_path)
                    .into(findViewById(R.id.film_image))
            }else{
                Glide.with(this@FilmDetailActivity)
                    .load("https://image.tmdb.org/t/p/original/" + film.poster_path)
                    .into(findViewById(R.id.film_image))
            }

            recyclerView.adapter = FilmAdapter(similarFilms.results,
                this@FilmDetailActivity, R.layout.film_view, true, 7)

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.back -> {
                finish()
            }else-> {
            if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
                true
            }else return super.onOptionsItemSelected(item)
        }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveFavToFile(data: String, fileName: String) {
        val writer =
            OutputStreamWriter(this.openFileOutput("${fileName}.txt", MODE_APPEND))
        writer.append(data + "\n")
        writer.close()
    }
    private fun deleteFavOnFile(data: String, fileName: String){

        val listIdFav : MutableList<String>

        listIdFav = File(this.filesDir.path + "/${fileName}.txt").readLines().toMutableList()

        listIdFav.remove(data)

        val writer =
            OutputStreamWriter(this.openFileOutput("${fileName}.txt", MODE_PRIVATE))
        writer.write("")
        writer.close()

        listIdFav.forEach { it -> saveFavToFile(it, fileName) }
    }
    private fun checkExistingFav(favorite: CheckBox, id: String){

        if(File(this.filesDir.path + "/filmFavorites.txt").exists()){
            val listIdFavFilm : MutableList<String>
            listIdFavFilm = File(this.filesDir.path + "/filmFavorites.txt").readLines().toMutableList()

            listIdFavFilm.forEach { it ->
                if(it.equals(id)){
                    favorite.isChecked = true
                }
            }
        }

        if(File(this.filesDir.path + "/tvFavorites.txt").exists()){
            val listIdFavTV : MutableList<String>
            listIdFavTV = File(this.filesDir.path + "/tvFavorites.txt").readLines().toMutableList()

            listIdFavTV.forEach { it ->
                if(it.equals(id)){
                    favorite.isChecked = true
                }
            }
        }
    }

    private fun searchPopup(currentView : View){
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

    private fun onSearchButtonShowPopupWindowClick(view : View?) : View{
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popupwindow_search, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.elevation = 50F

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, -600)


        return popupView

    }


}