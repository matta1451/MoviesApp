package app.android.movieapp.retrofit

import app.android.movieapp.model.Movie
import app.android.movieapp.response.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String): Call<MovieResponse>

    @GET("movie/{id}")
    fun getPopularMovieDetail(@Path("id") id: Int, @Query("api_key") apiKey: String): Call<Movie>
}