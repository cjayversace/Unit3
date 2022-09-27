package com.example.flixter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONArray

@Parcelize
data class Movie (
    val movieId: Int,
    val voteAvg: Double,
    val title: String,
    val overview: String,
    private val posterPath: String
) : Parcelable {

    val posterImageUrl ="https://image.tmdb.org/t/p/w342/$posterPath"


    companion object{
        fun fromJSONarr(movieArr: JSONArray) :List<Movie> {
            val movies = mutableListOf<Movie>()
            for(i in 0 until movieArr.length()){
                val movieJson = movieArr.getJSONObject(i)
                movies.add(
                    Movie(
                        movieJson.getInt("id"),
                        movieJson.getDouble("vote_average"),
                        movieJson.getString("title"),
                        movieJson.getString("overview"),
                        movieJson.getString("poster_path")

                    )
                )
            }
            return movies
        }

    }

}
