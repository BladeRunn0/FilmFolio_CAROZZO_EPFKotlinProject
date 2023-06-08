package com.epf.filmfolio

import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epf.filmfolio.model.Film
import kotlinx.coroutines.runBlocking
import java.io.File

class FavoritesActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        this.recyclerView = findViewById<RecyclerView>(R.id.list_favorites)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        val filmsAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(DatabaseService::class.java)

        val radioGroup = findViewById<RadioGroup>(R.id.search_radiogroup)
        val radioFilm = findViewById<RadioButton>(R.id.radio_film)


        radioGroup.check(radioFilm.id)
        getFavs("filmFavorites", filmsAPI, true)
        radioFilm.setOnClickListener {
            getFavs("filmFavorites", filmsAPI, true)
        }

        val radioTV = findViewById<RadioButton>(R.id.radio_tv)
        radioTV.setOnClickListener {
            getFavs("tvFavorites", filmsAPI, false)
        }

    }

    private fun getFavs(fileName: String, filmsAPI: DatabaseService, isFilm : Boolean){

        if(File(this.filesDir.path + "/${fileName}.txt").exists()){
            val listIdFavFilm : MutableList<String>

            listIdFavFilm = File(this.filesDir.path + "/${fileName}.txt").readLines().toMutableList()
            runBlocking {
                val listFilms = mutableListOf<Film>()
                listIdFavFilm.forEach { it ->
                    if(isFilm){
                        val film = filmsAPI.getFilmById(it.toInt())
                        listFilms.add(film)
                    }else{
                        val film = filmsAPI.getSerieById(it.toInt())
                        listFilms.add(film)
                    }
                }
                recyclerView.adapter = FilmAdapter(listFilms, this@FavoritesActivity, R.layout.film_view, isFilm, 0)
            }
        }
    }
}