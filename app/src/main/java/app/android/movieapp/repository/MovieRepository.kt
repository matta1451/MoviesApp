package app.android.movieapp.repository

import androidx.lifecycle.MutableLiveData
import app.android.movieapp.model.Movie
import app.android.movieapp.response.MovieResponse
import app.android.movieapp.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository {

    var responseMovies = MutableLiveData<List<Movie>>()
    var responseMovie = MutableLiveData<Movie>()

    fun getPPopularMovies(): MutableLiveData<List<Movie>> {
        val call:Call<MovieResponse> = ApiClient.storeInstance.getPopularMovies("ad7653bc5a772f683c8af65f3b212f5d")
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    responseMovies.value = response.body()?.results
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                // Manejar el error
            }
        })
        return responseMovies
    }

    fun getMovie(idMovie: Int): MutableLiveData<Movie> {
        val call:Call<Movie> = ApiClient.storeInstance.getPopularMovieDetail(idMovie, "ad7653bc5a772f683c8af65f3b212f5d")
        call.enqueue(object :Callback<Movie>{
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                responseMovie.value= response.body()
            }
            override fun onFailure(call: Call<Movie>, t: Throwable) {
            }
        })
        return responseMovie
    }
}