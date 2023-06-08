package com.epf.filmfolio

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.runBlocking


class SearchResultActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView

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
            val searchedTV = filmsAPI.getTVQuery(searched.toString())

            val radioGroup = findViewById<RadioGroup>(R.id.search_radiogroup)
            val radioFilm = findViewById<RadioButton>(R.id.radio_film)

            radioGroup.check(radioFilm.id)
            recyclerView.adapter = FilmAdapter(searchedFilm.results,
                this@SearchResultActivity, R.layout.film_view_searched, true, 0)
            radioFilm.setOnClickListener {
                recyclerView.adapter = FilmAdapter(searchedFilm.results,
                    this@SearchResultActivity, R.layout.film_view_searched, true, 0)
            }

            val radioTV = findViewById<RadioButton>(R.id.radio_tv)
            radioTV.setOnClickListener {
                recyclerView.adapter = FilmAdapter(searchedTV.results,
                    this@SearchResultActivity, R.layout.film_view_searched, false, 0)
            }
        }
    }
}