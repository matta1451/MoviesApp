package app.android.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.android.movieapp.databinding.MovieItemBinding
import app.android.movieapp.model.Movie
import coil.load

class RVMovieAdapter(var movies: List<Movie>, val onClickMovie: (Movie) -> Unit): RecyclerView.Adapter<MovieVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieVH(binding, onClickMovie)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        holder.bind(movies[position])
    }
}

class MovieVH(private val binding: MovieItemBinding, val onClickMovie: (Movie) -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(movie: Movie) {
        binding.txtTitle.text = "Título: ${movie.title}"
        binding.txtReleaseDate.text = "Fecha de lanzamiento: ${movie.release_date}"
        if(movie.poster_path.isNotEmpty()){
            binding.imgMovie.load("https://image.tmdb.org/t/p/w500" + movie.poster_path)
        }
        itemView.setOnClickListener {
            onClickMovie(movie)
        }
    }
}