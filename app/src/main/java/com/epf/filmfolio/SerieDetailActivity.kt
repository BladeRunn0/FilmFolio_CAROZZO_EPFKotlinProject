package com.epf.filmfolio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.OutputStreamWriter

class SerieDetailActivity : AppCompatActivity() {

    lateinit var recyclerViewTV: RecyclerView
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seriedetail)

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

        val serieId = this.intent.getIntExtra("Serie", 0)

        val seriesAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(DatabaseService::class.java)

        this.recyclerViewTV = findViewById<RecyclerView>(R.id.list_TV)
        recyclerViewTV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        runBlocking {
            val serie = seriesAPI.getSerieById(serieId)

            val favorite = findViewById<CheckBox>(R.id.favorite)
            checkExistingFav(favorite, serie.id.toString())
            favorite.setOnClickListener {
                if(favorite.isChecked){
                    saveFavToFile(serie.id.toString())
                }else{
                    deleteFavOnFile(serie.id.toString())
                }
            }


            val serieName = findViewById<TextView>(R.id.serie_name)
            serieName.text = serie.title
            if (serie.title == null) serieName.text = serie.name

            val tagline = findViewById<TextView>(R.id.serie_tagline)
            if(serie.tagline != null){
                tagline.text = serie.tagline
            }

            val numberSeasonsAndEpisodes = findViewById<TextView>(R.id.number_seasons_and_episodes)
            numberSeasonsAndEpisodes.text = serie.number_of_episodes.toString() + " episode(s) / " +
                    serie.number_of_seasons.toString() + " season(s)"

            val duration = findViewById<TextView>(R.id.episode_duration)
            if(serie.episode_run_time.isNotEmpty()){
                duration.text = serie.episode_run_time[0].toString() + " min per episode"
            }else{
                duration.text = "Unknown"
            }

            val serieGenres = findViewById<TextView>(R.id.serie_genres)
            if(serie.genres != null){
                serieGenres.text = serie.genres.toString().replace("[", "").replace("]","")
            }else{
                serieGenres.text = "Unknown"
            }

            val firstAir = findViewById<TextView>(R.id.first_air)
            firstAir.text = serie.first_air_date

            val lastAir = findViewById<TextView>(R.id.last_air)
            lastAir.text = serie.last_air_date

            val synopsis = findViewById<TextView>(R.id.serie_synopsis)
            synopsis.text = serie.overview

            var languages = findViewById<TextView>(R.id.languages)
            serie.languages.forEach {
                languages.text = it + languages.text
            }

            val status = findViewById<TextView>(R.id.serie_status)
            status.text = serie.status

            if(serie.last_episode_to_air != null){
                val lastEpisodeName = findViewById<TextView>(R.id.last_episode_name)
                lastEpisodeName.text = serie.last_episode_to_air.get("name").toString()

                val episodeDescription = findViewById<TextView>(R.id.episode_description)
                episodeDescription.text = serie.last_episode_to_air.get("overview").toString()

                findViewById<ImageView>(R.id.clock_episode).scaleX = 0.75F
                findViewById<ImageView>(R.id.clock_episode).scaleY = 0.75F

                val episodePosition = findViewById<TextView>(R.id.episode_position)
                episodePosition.text = "S" + serie.last_episode_to_air.get("season_number").toString().split(".")[0] +
                        "E" + serie.last_episode_to_air.get("episode_number").toString().split(".")[0]

                val episodeDuration = findViewById<TextView>(R.id.duration_episode)
                val tempDuration = serie.last_episode_to_air.get("runtime").toString().split(".")[0]
                episodeDuration.text = tempDuration + " min"


                Glide.with(this@SerieDetailActivity)
                    .load("https://image.tmdb.org/t/p/original/" + serie.last_episode_to_air.get("still_path").toString())
                    .apply(RequestOptions().override(413,232))
                    .into(findViewById(R.id.last_episode_image))
            }



            if(serie.backdrop_path != null){
                Glide.with(this@SerieDetailActivity)
                    .load("https://image.tmdb.org/t/p/original/" + serie.backdrop_path)
                    .into(findViewById(R.id.serie_image))
            }else{
                Glide.with(this@SerieDetailActivity)
                    .load("https://image.tmdb.org/t/p/original/" + serie.poster_path)
                    .into(findViewById(R.id.serie_image))
            }
            val similarTV = seriesAPI.getSimilarTV(serieId)
            if(similarTV.results.isNotEmpty()){
                recyclerViewTV.adapter = FilmAdapter(similarTV.results,
                    this@SerieDetailActivity, R.layout.film_view, false, 7)
            }

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

    private fun saveFavToFile(data: String) {
        val writer =
            OutputStreamWriter(this.openFileOutput("tvFavorites.txt", MODE_APPEND))
        writer.append(data + "\n")
        writer.close()
    }
    private fun deleteFavOnFile(data: String){

        val listIdFav : MutableList<String>

        listIdFav = File(this.filesDir.path + "/tvFavorites.txt").readLines().toMutableList()

        listIdFav.remove(data)

        val writer =
            OutputStreamWriter(this.openFileOutput("tvFavorites.txt", MODE_PRIVATE))
        writer.write("")
        writer.close()

        listIdFav.forEach { it -> saveFavToFile(it) }
    }
    private fun checkExistingFav(favorite: CheckBox, id: String){

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