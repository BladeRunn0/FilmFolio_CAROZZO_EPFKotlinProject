package com.epf.filmfolio.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class listResults(val results: List<Film>)



@Parcelize
data class Film(val adult: Boolean,
                val backdrop_path: String,
                val episode_run_time: @RawValue List<Int>,
                val first_air_date: String,
                val belongs_to_collection: @RawValue Map<String, Any>,
                val budget: Int,
                val genres: @RawValue List<Genre>,
                val id: Int,
                val name: String,
                val original_language: String,
                val original_title: String,
                val number_of_episodes: Int,
                val number_of_seasons: Int,
                val overview: String,
                val popularity: Double,
                val poster_path: String,
                val release_date: String,
                val revenue: Int,
                val runtime: Int,
                val spoken_languages: @RawValue List<Language>,
                val languages : @RawValue List<String>,
                val last_air_date: String,
                val last_episode_to_air: @RawValue Map<String, Any>,
                val status: String,
                val tagline: String,
                val title: String,
                val video: Boolean,
                val vote_average: Float,
                val vote_count: Int
) : Parcelable {
    override fun toString(): String {
        return "Film(adult=$adult, backdrop_path='$backdrop_path', budget=$budget, id=$id, name='$name', original_language='$original_language', original_title='$original_title', overview='$overview', popularity=$popularity, poster_path='$poster_path', release_date='$release_date', revenue=$revenue, runtime=$runtime, title='$title', video=$video, vote_average=$vote_average, vote_count=$vote_count)"
    }
}

@Parcelize
class Genre (val id : Int,
             val name: String) : Parcelable{
    override fun toString(): String {
        return name
    }

}

@Parcelize
class Language (val english_name : String,
                val iso_639_1 : String,
                val name : String) : Parcelable {
    override fun toString(): String {
        return iso_639_1
    }
}
