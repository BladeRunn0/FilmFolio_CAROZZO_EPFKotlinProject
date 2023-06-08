package com.epf.filmfolio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.runBlocking

class ListTrendSeriesActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_listtrendseries)

        this.recyclerView = findViewById<RecyclerView>(R.id.list_trend)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val filmsAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(DatabaseService::class.java)

        runBlocking {
            val trendFilm = filmsAPI.getTrendingSeries()

            recyclerView.adapter = FilmAdapter(trendFilm.results,
                this@ListTrendSeriesActivity, R.layout.film_view_trendlist, true, 0)
        }
    }
}