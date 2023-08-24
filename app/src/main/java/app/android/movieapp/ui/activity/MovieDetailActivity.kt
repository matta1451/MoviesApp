package app.android.movieapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import app.android.movieapp.R
import app.android.movieapp.model.Movie
import com.squareup.picasso.Picasso

class MovieDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val movie = intent.getSerializableExtra("movie") as Movie
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        val overviewTextView = findViewById<TextView>(R.id.overviewTextView)
        val posterImageView = findViewById<ImageView>(R.id.posterImageView)
        // ...

        titleTextView.text = movie.title
        dateTextView.text = movie.release_date
        overviewTextView.text = movie.overview
        // ...
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.poster_path).into(posterImageView)
    }
}