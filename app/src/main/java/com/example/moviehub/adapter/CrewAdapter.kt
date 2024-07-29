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
import com.example.moviehub.model.Crew
import com.example.moviehub.utilities.Constants

class TopCrewAdapter(private val context: Context, private val crewList: MutableList<Crew> = mutableListOf()) :
    RecyclerView.Adapter<TopCrewAdapter.CrewViewHolder>() {
    inner class CrewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val crewImage: ImageView = itemView.findViewById(R.id.crew_image)
        private val crewName: TextView = itemView.findViewById(R.id.crew_name)
        private val crewJob: TextView = itemView.findViewById(R.id.crew_job)

        fun bind(crew: Crew) {
            crewName.text = crew.name
            crewJob.text = crew.job
            val crewPosterURL = Constants.POSTER_BASE_URL + crew.profilePath
            Glide.with(context)
                .load(crewPosterURL)
                .placeholder(R.drawable.blank_person)
                .into(crewImage)
        }
    }

    fun updateCrew(newCrew: List<Crew>) {
        crewList.clear()
        crewList.addAll(newCrew)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): CrewViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_top_crew_item, parent, false)
        return CrewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CrewViewHolder, position: Int) {
        holder.bind(crewList[position])
    }

    override fun getItemCount(): Int {
        return crewList.size
    }
}