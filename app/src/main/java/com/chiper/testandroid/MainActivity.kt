package com.chiper.testandroid


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.chiper.testandroid.adapter.MovieAdapter
import com.chiper.testandroid.databinding.ActivityMainBinding
import com.chiper.testandroid.model.Movie
import com.chiper.testandroid.service.APIService
import com.chiper.testandroid.util.MovieApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: MovieAdapter
    private val movies = mutableListOf<Movie>()
    private var countMovies = 1;
    private var loading = true
    private var pastVisiblesItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = MovieAdapter(movies)
        binding.rvMovies.layoutManager = LinearLayoutManager(this)
        binding.rvMovies.adapter = adapter
        binding.rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount =
                        (binding.rvMovies.layoutManager as LinearLayoutManager).childCount
                    totalItemCount =
                        (binding.rvMovies.layoutManager as LinearLayoutManager).itemCount
                    pastVisiblesItems =
                        (binding.rvMovies.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            countMovies++
                            findMoviesOnline(countMovies)
                            loading = true
                        }
                    }
                }
            }
        })

        if (isOnline(this)) {
            findMoviesOnline(countMovies)
        } else {
            findMoviesOnffline()
        }
    }

    private fun findMoviesOnline(page: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java)
                .getMovies(
                    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjODFiY2I1ZTZhODFhYjkzMDEwYjM1ZGUzMzA2MWJmMSIsInN1YiI6IjYxMTliMjFkYTBiNmI1MDA3ZGNlMjY2YiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.OR_piwS726iy3r82oBPkhXdE4JY0_GcKc34IgKQE6h0",
                    countMovies
                )
            val puppies = call.body()
            val moviesResult = puppies?.results ?: emptyList()
            if (!moviesResult.isEmpty()) {
                for (m in moviesResult) {
                    MovieApp.database.getMovieDao().insertAll(m)
                }
            }
            runOnUiThread {
                if (call.isSuccessful) {

                    movies.addAll(moviesResult)
                    adapter.notifyDataSetChanged()
                } else {
                    showError()
                }
            }
        }
    }

    private fun findMoviesOnffline() {
        CoroutineScope(Dispatchers.IO).launch {
            val moviesResult = MovieApp.database.getMovieDao().getAll();
            runOnUiThread {
                movies.clear()
                movies.addAll(moviesResult)
                adapter.notifyDataSetChanged()
            }
        }

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun showError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}