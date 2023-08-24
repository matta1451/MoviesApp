package app.android.movieapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.android.movieapp.databinding.FragmentMovieFirestoreBinding
import app.android.movieapp.model.Movie
import app.android.movieapp.ui.adapter.RVMovieFirestoreAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class MovieFirestoreFragment : Fragment() {
    private lateinit var binding: FragmentMovieFirestoreBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieFirestoreBinding.inflate(inflater, container, false)
        firestore = Firebase.firestore
        storage = FirebaseStorage.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RVMovieFirestoreAdapter(listOf())
        binding.rvMovies.adapter = adapter
        binding.rvMovies.layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)

        getMovies().observe(requireActivity()) {
            adapter.movies = it
            adapter.notifyDataSetChanged()
        }
    }

    private fun getMovies(): LiveData<List<Movie>> {
        val peliculasLiveData = MutableLiveData<List<Movie>>()
        val peliculas = mutableListOf<Movie>()
        firestore.collection("peliculas").get()
            .addOnSuccessListener {
                val movies = it.documents
                for (movie in movies){
                    if(movie.get("title").toString().isNotEmpty()){
                        val pelicula = Movie(0, movie.get("title").toString(), movie.get("release_date").toString(),
                            movie.get("poster_path").toString(), movie.get("overview").toString(),
                            movie.get("vote_average").toString().toDouble())
                        peliculas.add(pelicula)
                    }
                }
            }
            .addOnCompleteListener {
                peliculasLiveData.postValue(peliculas)
            }
        return peliculasLiveData
    }
}