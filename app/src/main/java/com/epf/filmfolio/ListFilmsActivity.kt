package com.epf.filmfolio

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.runBlocking

class ListFilmsActivity : AppCompatActivity(){
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_listfilms)

        this.recyclerView = findViewById<RecyclerView>(R.id.list_films)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val filmsAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(DatabaseService::class.java)

        runBlocking {
            val test = filmsAPI.getFilms()
            val test_id = filmsAPI.getFilmById(507250)

            recyclerView.adapter = FilmAdapter(test.results, this@ListFilmsActivity)
        }


    }

}