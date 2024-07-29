package com.example.moviehub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviehub.R
import com.example.moviehub.model.Cast
import com.example.moviehub.utilities.Constants

class TopCastAdapter(private val context: Context, private val castList: MutableList<Cast> = mutableListOf()) :
    RecyclerView.Adapter<TopCastAdapter.CastViewHolder>() {

    inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val castImage: ImageView = itemView.findViewById(R.id.cast_image)
        private val castName: TextView = itemView.findViewById(R.id.cast_name)
        private val castCharacter: TextView = itemView.findViewById(R.id.cast_character)

        fun bind(cast: Cast) {
            castName.text = cast.name
            castCharacter.text = cast.character
            val castPosterURL = Constants.POSTER_BASE_URL + cast.profilePath
            Glide.with(context)
                .load(castPosterURL)
                .placeholder(R.drawable.blank_person)
                .into(castImage)
        }
    }
    fun updateCasts(newCast: List<Cast>) {
        castList.clear()
        castList.addAll(newCast)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): CastViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_top_cast_item, parent, false)
        return CastViewHolder(view)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(castList[position])
    }

    override fun getItemCount(): Int {
        return castList.size
    }
}
