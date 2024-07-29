package com.example.moviehub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviehub.R
import com.example.moviehub.model.Poster
import com.example.moviehub.utilities.Constants

class ImagesAdapter(private val context: Context, private val posterList: MutableList<Poster> = mutableListOf()) :
    RecyclerView.Adapter<ImagesAdapter.CastViewHolder>() {

    inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val posterImage: ImageView = itemView.findViewById(R.id.image_view)
        fun bind(poster: Poster) {
            val posterURL = Constants.POSTER_BASE_URL + poster.filePath

            Glide.with(context)
                .load(posterURL)
                .placeholder(R.drawable.blank_person)
                .into(posterImage)
        }
    }
    fun updatePosters(newPoster: List<Poster>) {
        posterList.clear()
        posterList.addAll(newPoster)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): CastViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_images, parent, false)
        return CastViewHolder(view)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(posterList[position])
    }

    override fun getItemCount(): Int {
        return posterList.size
    }
}
