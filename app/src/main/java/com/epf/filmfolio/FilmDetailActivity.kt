package com.epf.filmfolio

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.epf.filmfolio.model.Film
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Text

class FilmDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filmdetail)

        val filmId = this.intent.getIntExtra("Film", 0)
        var serieId : Int = 0
        if(filmId == 0){
            serieId = this.intent.getIntExtra("Serie", 0)
        }

        val filmsAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(DatabaseService::class.java)

        runBlocking {
            val film : Film
            if(filmId != 0){
                film = filmsAPI.getFilmById(filmId)
            }else{
                film = filmsAPI.getSerieById(serieId)
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


}