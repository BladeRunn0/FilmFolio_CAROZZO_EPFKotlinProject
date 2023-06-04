package com.epf.filmfolio

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.runBlocking

class SerieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seriedetail)

        val serieId = this.intent.getIntExtra("Serie", 0)

        val seriesAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(DatabaseService::class.java)

        runBlocking {
            val serie = seriesAPI.getSerieById(serieId)


            val serieName = findViewById<TextView>(R.id.serie_name)
            serieName.text = serie.title
            if (serie.title == null) serieName.text = serie.name

            val tagline = findViewById<TextView>(R.id.serie_tagline)
            if(serie.tagline != null){
                tagline.text = serie.tagline
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