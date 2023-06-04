package com.epf.filmfolio

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.epf.filmfolio.model.Film
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.OutputStreamWriter

class FilmViewHolder(view : View) : RecyclerView.ViewHolder(view)

class FilmAdapter(val films : List<Film>, val context: Context, val layout : Int, val isFilm : Boolean) : RecyclerView.Adapter<FilmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(layout, parent,false)

        return FilmViewHolder(view)
    }

    override fun getItemCount() = films.size

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film : Film = films[position]

        val view = holder.itemView

        val textView = view.findViewById<TextView>(R.id.film_name)
        textView.text = film.title
        if (film.title == null) textView.text = film.name
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/original/" + film.poster_path)
            .into(view.findViewById(R.id.film_image));

        val rate = view.findViewById<RatingBar>(R.id.rating)
        if(rate != null){
            rate.rating = film.vote_average
        }

        val favorite = view.findViewById<CheckBox>(R.id.favorite)

        checkExistingFav(favorite, film.id.toString())

        favorite.setOnClickListener {
            if(favorite.isChecked){
                if(isFilm){
                    saveFavToFile(film.id.toString(), "filmFavorites")
                }else{
                    saveFavToFile(film.id.toString(), "tvFavorites")
                }
            }else{
                if(isFilm){
                    deleteFavOnFile(film.id.toString(), "filmFavorites")
                }else{
                    deleteFavOnFile(film.id.toString(), "tvFavorites")
                }
            }

        }


        view.setOnClickListener {
            val intent : Intent
            if(isFilm){
                intent = Intent(context, FilmDetailActivity::class.java)
                intent.putExtra("Film", film.id)
            }else{
                intent = Intent(context, SerieDetailActivity::class.java)
                intent.putExtra("Serie", film.id)
            }

            startActivity(context, intent, null)
        }

    }

    private fun saveFavToFile(data: String, fileName: String) {
        val writer =
            OutputStreamWriter(context.openFileOutput("${fileName}.txt", AppCompatActivity.MODE_APPEND))
        writer.append(data + "\n")
        writer.close()
    }

    private fun deleteFavOnFile(data: String, fileName: String){

        val listIdFav : MutableList<String>

        listIdFav = File(context.filesDir.path + "/${fileName}.txt").readLines().toMutableList()

        listIdFav.remove(data)

        val writer =
            OutputStreamWriter(context.openFileOutput("${fileName}.txt", AppCompatActivity.MODE_PRIVATE))
        writer.write("")
        writer.close()

        listIdFav.forEach { it -> saveFavToFile(it, fileName) }
    }

    private fun checkExistingFav(favorite: CheckBox, id: String){

        if(File(context.filesDir.path + "/filmFavorites.txt").exists()){
            val listIdFavFilm : MutableList<String>
            listIdFavFilm = File(context.filesDir.path + "/filmFavorites.txt").readLines().toMutableList()

            listIdFavFilm.forEach { it ->
                if(it.equals(id)){
                    favorite.isChecked = true
                }
            }
        }

        if(File(context.filesDir.path + "/tvFavorites.txt").exists()){
            val listIdFavTV : MutableList<String>
            listIdFavTV = File(context.filesDir.path + "/tvFavorites.txt").readLines().toMutableList()

            listIdFavTV.forEach { it ->
                if(it.equals(id)){
                    favorite.isChecked = true
                }
            }
        }
    }


}
