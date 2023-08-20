package app.android.movieapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.android.movieapp.databinding.FragmentMovieBinding
import app.android.movieapp.response.MovieResponse
import app.android.movieapp.retrofit.ApiClient
import app.android.movieapp.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieFragment : Fragment() {
    private lateinit var binding: FragmentMovieBinding
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        movieAdapter = MovieAdapter()
        binding.recyclerView.adapter = movieAdapter

        // Llamar al método para cargar las películas
        loadMovies()
    }

    private fun loadMovies() {
        val apiService = ApiClient.getRetrofitInstance().create(ApiService::class.java)
        val call = apiService.getPopularMovies("ad7653bc5a772f683c8af65f3b212f5d")

        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movies = response.body()?.results
                    movieAdapter.setMovies(movies)
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                // Manejar el error
            }
        })
    }
}