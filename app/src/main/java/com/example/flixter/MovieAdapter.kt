package com.example.flixter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


const val MOVIE_EXTRA = "MOVIE_EXTRA"
private const val TAG = "MovieAdapter"
class MovieAdapter(private val context: Context, private val movies: MutableList<Movie>):
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvOverview = itemView.findViewById<TextView>(R.id.tvOverview)
        private val pic = itemView.findViewById<ImageView>(R.id.titlePic)
        init{
            itemView.setOnClickListener(this)
        }
        fun bind(movie: Movie){
            tvTitle.text = movie.title
            tvOverview.text = movie.overview
            val radius = 5;
            val margin = 10;
            Glide.with(context).load(movie.posterImageUrl).into(pic)

        }
         override fun onClick(v: View?) {
            val movie = movies[adapterPosition]
            Toast.makeText(context, movie.title, Toast.LENGTH_SHORT).show()
            val intent= Intent(context, DetailActivity::class.java)
            intent.putExtra(MOVIE_EXTRA, movie)
            context.startActivity(intent)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "OnCreateViewHolder")
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "OnBindViewHolder: position $position")
        val movie = movies[position]
        holder.bind(movie)

    }

    override fun getItemCount(): Int {
        return movies.size
    }

}
