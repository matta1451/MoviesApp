package app.android.movieapp.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.android.movieapp.databinding.MovieItemFirestoreBinding
import app.android.movieapp.model.Movie
import coil.load

class RVMovieFirestoreAdapter(var movies: List<Movie>): RecyclerView.Adapter<MovieVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {

        val binding = MovieItemFirestoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieVH(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        holder.bind(movies[position])
    }
}

class MovieVH(private val binding: MovieItemFirestoreBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(movie: Movie) {
        binding.txtTitle.text = "Título: ${movie.title}"
        binding.txtReleaseDate.text = "Fecha de lanzamiento: ${movie.release_date}"
        binding.txtOverview.text = "Descripción: ${movie.overview}"
        binding.txtVoteAverage.text = "Puntuación: ${movie.vote_average.toString()}"
        if(movie.poster_path.isNotEmpty()){
            binding.imgMovie.load(movie.poster_path)
        }
    }

}