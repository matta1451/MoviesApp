package app.android.movieapp.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.android.movieapp.model.Movie
import app.android.movieapp.repository.MovieRepository

class MovieViewModel: ViewModel(){
    private val repository = MovieRepository()

    fun listarMovies(): LiveData<List<Movie>> {
        return repository.getPPopularMovies()
    }
}