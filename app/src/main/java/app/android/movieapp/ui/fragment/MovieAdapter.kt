package app.android.movieapp.ui.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.android.movieapp.R
import app.android.movieapp.model.Movie
import app.android.movieapp.ui.MovieDetailActivity
import com.squareup.picasso.Picasso

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private var movies: List<Movie> = ArrayList()

    fun setMovies(movies: List<Movie>?) {
        this.movies = movies ?: ArrayList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val posterImageView: ImageView = itemView.findViewById(R.id.posterImageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        init {
            itemView.setOnClickListener {
                val movie = movies[adapterPosition]
                val intent = Intent(itemView.context, MovieDetailActivity::class.java)
                intent.putExtra("movie", movie)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(movie: Movie) {
            // Cargar la imagen utilizando una biblioteca como Picasso o Glide
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.poster_path).into(posterImageView)
            titleTextView.text = movie.title
            dateTextView.text = movie.release_date
        }
    }
}