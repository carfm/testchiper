package com.chiper.testandroid.adapter


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.chiper.testandroid.MovieActivity
import com.chiper.testandroid.R
import com.chiper.testandroid.databinding.ItemMovieBinding
import com.chiper.testandroid.model.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(private val movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        this.context = parent.context
        return MovieViewHolder(layoutInflater.inflate(R.layout.item_movie, parent, false))
    }

    override fun getItemCount(): Int = movies.size
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = movies[position]
        holder.bind(item, this.context)
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemMovieBinding.bind(view)

        fun bind(movie: Movie, context: Context) {
            binding.title.text = movie.title
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.poster_path)
                .into(binding.ivMovie)
            itemView.setOnClickListener {
                val intent = Intent(context, MovieActivity::class.java)
                intent.putExtra("title", movie.title)
                intent.putExtra("image", movie.backdrop_path)
                intent.putExtra("overview", movie.overview)
                val bundle = Bundle()
                startActivity(context, intent, bundle)
            }
        }
    }
}