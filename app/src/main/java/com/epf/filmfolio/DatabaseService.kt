package com.epf.filmfolio

import com.epf.filmfolio.model.Film
import com.epf.filmfolio.model.listResults
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface DatabaseService {

    @GET("trending/all/week?api_key=a6a78235d30c91448b19e93a96fe7fc2")
    suspend fun getFilms(): listResults

    @GET("movie/{movie_id}?api_key=a6a78235d30c91448b19e93a96fe7fc2")
    suspend fun getFilmById(@Path("movie_id") id: Int): Film

    @GET("movie/{movie_id}/similar?api_key=a6a78235d30c91448b19e93a96fe7fc2")
    suspend fun getSimilarFilms(@Path("movie_id") id: Int): listResults

    @GET("movie/{movie_id}/recommendations?api_key=a6a78235d30c91448b19e93a96fe7fc2")
    suspend fun getRecommendedFilms(@Path("movie_id") id: Int): listResults

}

object RetrofitHelper {
    //https://image.tmdb.org/t/p/original/ + URLimage

    //val baseUrl = "https://api.themoviedb.org/3/"

    fun getInstance(baseUrl: String): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }
}