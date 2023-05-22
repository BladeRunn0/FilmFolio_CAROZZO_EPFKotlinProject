package com.epf.filmfolio

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epf.filmfolio.model.Film
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

            recyclerView.adapter = FilmAdapter(searchedFilm.results, this@SearchResultActivity, R.layout.film_view_searched)

        }

    }

    private fun onSearchButtonShowPopupWindowClick(view : View?) : View{
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popupwindow_search, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.elevation = 50F
//        popupView.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, -600)


        return popupView

    }
}