package com.epf.filmfolio

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.epf.filmfolio.model.Film

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
        view.setOnClickListener {
            val intent = Intent(context, FilmDetailActivity::class.java)
            if(isFilm){
                intent.putExtra("Film", film.id)
            }else{
                intent.putExtra("Serie", film.id)
            }

            startActivity(context, intent, null)
        }

    }


}

