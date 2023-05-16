package com.epf.filmfolio

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.epf.filmfolio.model.Film

class FilmViewHolder(view : View) : RecyclerView.ViewHolder(view)

class FilmAdapter(val films : List<Film>, val context: Context) : RecyclerView.Adapter<FilmViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.film_view, parent,false)

        return FilmViewHolder(view)
    }

    override fun getItemCount() = films.size

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film : Film = films[position]

        val view = holder.itemView
        val textView = view.findViewById<TextView>(R.id.film_name)
        textView.text = film.title
        if (film.title == null) textView.text = film.name
    }
}