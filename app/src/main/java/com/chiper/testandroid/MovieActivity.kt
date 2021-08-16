package com.chiper.testandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chiper.testandroid.databinding.ActivityMovieBinding
import com.squareup.picasso.Picasso

class MovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
    }

    private fun initComponents() {
        val title = intent.getStringExtra("title")
        val image = intent.getStringExtra("image")
        val overview = intent.getStringExtra("overview")
        binding.title.text = title
        binding.overview.text = overview
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + image)
            .into(binding.ivImage)
    }
}