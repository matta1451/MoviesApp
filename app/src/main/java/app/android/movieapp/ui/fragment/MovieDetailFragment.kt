package app.android.movieapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import app.android.movieapp.databinding.FragmentMovieDetailBinding
import app.android.movieapp.model.Movie
import app.android.movieapp.vm.MovieDetailViewModel
import coil.load

class MovieDetailFragment : Fragment() {
    private lateinit var binding: FragmentMovieDetailBinding
    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var movie: Movie
    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = args.movie
        viewModel = ViewModelProvider(requireActivity())[MovieDetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*viewModel.getMovie(movie.id).observe(requireActivity()) {
            binding.txtTitle.text = "Title: ${it.title}"
            binding.txtOverview.text = "Description: ${it.overview}"
            binding.imgMovie.load("https://image.tmdb.org/t/p/w500" + it.poster_path)
        }*/
        binding.txtTitle.text = "Title: ${movie.title}"
        binding.txtOverview.text = "Description: ${movie.overview}"
        binding.imgMovie.load("https://image.tmdb.org/t/p/w500" + movie.poster_path)
    }

}