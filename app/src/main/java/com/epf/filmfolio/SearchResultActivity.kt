package com.epf.filmfolio

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.runBlocking


class SearchResultActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView
    lateinit var ratingBar: RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchresult)
        val searched = this.intent.getStringExtra("SearchedFilm")


        this.recyclerView = findViewById<RecyclerView>(R.id.list_searched_films)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) // set LayoutManager to RecyclerView

        val filmsAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(DatabaseService::class.java)

        runBlocking {

            val searchedFilm = filmsAPI.getFilmQuery(searched.toString())

            recyclerView.adapter = FilmAdapter(searchedFilm.results, this@SearchResultActivity, R.layout.film_view_searched, true)

        }


    }
}