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
import com.example.moviehub.model.TvSeriesDetail
import com.example.moviehub.utilities.Constants.Companion.POSTER_BASE_URL
import com.example.moviehub.utilities.Utility

class TvAdapter(private val context: Context, val tvShows: MutableList<TvSeriesDetail> = mutableListOf()) :
    RecyclerView.Adapter<TvAdapter.TvViewHolder>() {

    inner class TvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val tvShow = tvShows[adapterPosition]
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("id", tvShow.id)
                intent.putExtra("type","tv")
                intent.putExtra("title",tvShow.name)
                intent.putExtra("subtitle",tvShow.originalName)
                context.startActivity(intent)
            }
        }

        fun bind(tvShow: TvSeriesDetail) {
            val imageView = itemView.findViewById<ImageView>(R.id.movie_image)
            val tvPosterURL = POSTER_BASE_URL + tvShow.posterPath
            Glide.with(context)
                .load(tvPosterURL)
                .placeholder(R.drawable.blank_person)
                .into(imageView)

            val rating = String.format("%.1f", tvShow.voteAverage)
            itemView.findViewById<TextView>(R.id.text_rating).text = rating

            val utility = Utility()
            utility.showRating(
                itemView,
                tvShow.voteAverage,
                R.id.rating_layout,
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): TvViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_list_item, parent, false)
        return TvViewHolder(view)
    }

    fun updateTvShows(newTvSeries: List<TvSeriesDetail>) {
        tvShows.clear()
        tvShows.addAll(newTvSeries)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TvViewHolder, position: Int) {
        val tvShow = tvShows[position]
        holder.bind(tvShow)
    }

    override fun getItemCount(): Int {
        return tvShows.size
    }
}
