package com.epf.filmfolio

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epf.filmfolio.model.Film


class SearchResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_searchresult)

        val recyclerView = findViewById<RecyclerView>(R.id.list_films_result)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) // set LayoutManager to RecyclerView

//        recyclerView.adapter = FilmAdapter(Film.generate())

    }
}