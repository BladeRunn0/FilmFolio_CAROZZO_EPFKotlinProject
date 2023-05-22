package com.epf.filmfolio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.runBlocking

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
            val test_id = filmsAPI.getFilmById(507250)

            recyclerViewFilms.adapter = FilmAdapter(trendFilm.results, this@ListTrendingActivity, R.layout.film_view)
            recyclerViewTV.adapter = FilmAdapter(trendTV.results, this@ListTrendingActivity, R.layout.film_view)
        }

        val seeAllFilms = findViewById<Button>(R.id.allTrendFilm_button)
        val seeAllTV = findViewById<Button>(R.id.allTrendTV_button)

        seeAllFilms.setOnClickListener {
            val intent = Intent(this, ListTrendFilmsActivity::class.java)
            startActivity(intent)
        }


    }

}