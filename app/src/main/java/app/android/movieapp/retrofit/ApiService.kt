package app.android.movieapp.retrofit

import app.android.movieapp.response.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String): Call<MovieResponse>
}