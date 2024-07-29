package com.example.moviehub.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviehub.R
import com.example.moviehub.model.VideoResult
import com.example.moviehub.utilities.Constants.Companion.IMG_URL
import com.example.moviehub.utilities.Constants.Companion.VIDEO_BASE_URL


class VideosAdapter(private val context: Context, private var videos: List<VideoResult> = mutableListOf()) :
    RecyclerView.Adapter<VideosAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val videoNameTextView: TextView = itemView.findViewById(R.id.video_name)
        private val thumbnailImageView: ImageView = itemView.findViewById(R.id.thumbnail_image)
        private val playButton: ImageView = itemView.findViewById(R.id.play_button)

        fun bind(video: VideoResult) {
            videoNameTextView.text = video.name

            val thumbnailUrl = "${IMG_URL}${video.key}/mqdefault.jpg"
            Glide.with(context)
                .load(thumbnailUrl)
                .placeholder(R.drawable.blank_image)  // Placeholder image
                .into(thumbnailImageView)


            playButton.setOnClickListener {
                val videoUri = Uri.parse("$VIDEO_BASE_URL${video.key}")
                val intent = Intent(Intent.ACTION_VIEW, videoUri)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): VideoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_videos, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.bind(video)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    fun setVideos(videos: List<VideoResult>) {
        this.videos = videos
        notifyDataSetChanged()
    }
}