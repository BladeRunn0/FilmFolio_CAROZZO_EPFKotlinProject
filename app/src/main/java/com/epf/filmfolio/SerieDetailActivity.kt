package com.epf.filmfolio

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.OutputStreamWriter

class SerieDetailActivity : AppCompatActivity() {

    lateinit var recyclerViewTV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seriedetail)

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

            if(serie.backdrop_path != null){
                Glide.with(this@SerieDetailActivity)
                    .load("https://image.tmdb.org/t/p/original/" + serie.backdrop_path)
                    .into(findViewById(R.id.serie_image))
            }else{
                Glide.with(this@SerieDetailActivity)
                    .load("https://image.tmdb.org/t/p/original/" + serie.poster_path)
                    .into(findViewById(R.id.serie_image))
            }
            val trendTV = seriesAPI.getSimilarTV(serieId)
            recyclerViewTV.adapter = FilmAdapter(trendTV.results, this@SerieDetailActivity, R.layout.film_view, false)
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
}