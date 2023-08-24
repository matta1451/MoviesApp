package app.android.movieapp.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import app.android.movieapp.model.Movie
import app.android.movieapp.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository = MovieRepository()

    fun getMovie(idMovie: Int): LiveData<Movie> {
        return repository.getMovie(idMovie)
    }
}
/*
    fun addFavorite(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavorites(product)
        }
    }

    fun deleteProduct(product: Product) {

    }
}*/