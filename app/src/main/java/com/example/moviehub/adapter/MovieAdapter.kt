package com.example.moviehub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviehub.R
import com.example.moviehub.activities.DetailActivity
import com.example.moviehub.model.MovieResult
import com.example.moviehub.utilities.Constants.Companion.POSTER_BASE_URL
import com.example.moviehub.utilities.Utility

class MovieAdapter(private val context: Context, val movies: MutableList<MovieResult> = mutableListOf()) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val movie = movies[adapterPosition]
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("id", movie.id)
                intent.putExtra("type","movie")
                intent.putExtra("title",movie.title)
                intent.putExtra("subtitle",movie.originalTitle)
                context.startActivity(intent)
            }
        }
        fun bind(movie: MovieResult) {
            val imageView = itemView.findViewById<ImageView>(R.id.movie_image)
            val moviePosterURL = POSTER_BASE_URL + movie.posterPath
            Glide.with(context)
                .load(moviePosterURL)
                .placeholder(R.drawable.blank_person)
                .into(imageView)
            val rating = String.format("%.1f", movie.voteAverage)
            itemView.findViewById<TextView>(R.id.text_rating).text = rating
            val utility = Utility()
            utility.showRating(itemView, movie.voteAverage, R.id.rating_layout)
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewtype: Int
    ): MovieAdapter.MovieViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_list_item, parent, false)
        return MovieViewHolder(view)

    }
    fun updateMovies(newMovies: List<MovieResult>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: MovieAdapter.MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}