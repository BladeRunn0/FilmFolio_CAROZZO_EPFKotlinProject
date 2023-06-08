package com.epf.filmfolio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.runBlocking
import java.io.OutputStreamWriter

class ListTrendingActivity : AppCompatActivity(){
    lateinit var recyclerViewFilms: RecyclerView
    lateinit var recyclerViewTV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_listtrending)

        this.recyclerViewFilms = findViewById<RecyclerView>(R.id.list_films)
        recyclerViewFilms.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        this.recyclerViewTV = findViewById<RecyclerView>(R.id.list_TV)
        recyclerViewTV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        val filmsAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(DatabaseService::class.java)

        runBlocking {
            val trendFilm = filmsAPI.getTrendingFilms()
            val trendTV = filmsAPI.getTrendingSeries()

            recyclerViewFilms.adapter = FilmAdapter(trendFilm.results,
                this@ListTrendingActivity, R.layout.film_view, true, 7)
            recyclerViewTV.adapter = FilmAdapter(trendTV.results,
                this@ListTrendingActivity, R.layout.film_view, false, 7)
        }

        val seeAllFilms = findViewById<Button>(R.id.allTrendFilm_button)
        val seeAllTV = findViewById<Button>(R.id.allTrendTV_button)
        seeAllFilms.setOnClickListener {
            val intent = Intent(this, ListTrendFilmsActivity::class.java)
            startActivity(intent)
        }

        seeAllTV.setOnClickListener {
            val intent = Intent(this, ListTrendSeriesActivity::class.java)
            startActivity(intent)
        }
    }


}