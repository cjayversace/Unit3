package com.example.flixter

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers

private const val TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=12901f72c4cbac15c5e22dc40ae7b77d&language=en-US"
private const val TAG  = "DetailActivity"
private const val YOUTUBE_API_KEY = "AIzaSyCcs8nM7g3q2O-6WhYGCg-gmJQtT5_1cAs"

class DetailActivity : YouTubeBaseActivity() {
    private lateinit var tvTitle: TextView
    private lateinit var tvOverview: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var ytPlayerView: YouTubePlayerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        tvTitle = findViewById(R.id.tvTitle)
        tvOverview = findViewById(R.id.tvOverview)
        ratingBar = findViewById(R.id.rbVoteAverage)
        ytPlayerView = findViewById(R.id.player)

        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie
        Log.i(TAG, "Movie is $movie")

        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        ratingBar.rating = movie.voteAvg.toFloat()

        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.movieId), object:JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure: $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess")
                val results = json.jsonObject.getJSONArray("results")
                if(results.length()==0){
                    Log.w(TAG, "No movie trailers found")
                    return
                }
                val movieTrailerJson = results.getJSONObject(0)
                val youtubeKey = movieTrailerJson.getString("key")
                initializeYoutube(youtubeKey, movie.voteAvg.toFloat())
            }

        })

    }

    private fun initializeYoutube(youtubeKey: String, voteAvg: Float) {
        ytPlayerView.initialize(YOUTUBE_API_KEY, object:YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.i(TAG, "onInitializationSuccess")
                if(voteAvg>7){
                    player?.loadVideo(youtubeKey);
                }
                else{
                    player?.cueVideo(youtubeKey);
                }


            }


            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.i(TAG, "onInitializationFailure")            }

        })
    }
}