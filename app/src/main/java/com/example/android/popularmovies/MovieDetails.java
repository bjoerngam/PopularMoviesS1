package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movies>>{

    //Suggestion by Udacity review
    private static final int IMAGE_WIDTH = 420;
    private static final int IMAGE_HEIGHT = 300;

    //Suggestion by Udacity review
    @BindView(R.id.iv_movie_details_image) ImageView mSecondMovieImage;
    @BindView(R.id.tv_movie_details_title) TextView mMovieTitle;
    @BindView(R.id.tv_movie_details_overview) TextView mMovieOverview;
    @BindView(R.id.tv_movie_details_average_ratings) TextView mAverageRatings;
    @BindView(R.id.tv_movie_details_release_date) TextView mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        Intent i = getIntent();
        // getting the current object and its content via parcel
        Movies currentMovie = i.getExtras().getParcelable("currentObject");
        Picasso.with(this).load(currentMovie.getmBackdropPathURL()).resize(IMAGE_WIDTH, IMAGE_HEIGHT).centerCrop().into(mSecondMovieImage);
        mMovieTitle.setText(currentMovie.getmOrignalTitle());
        mMovieOverview.setText(currentMovie.getmOverview());
        mAverageRatings.setText(getString(R.string.movie_details_average_votes)+currentMovie.getmVoteAverage());
        mReleaseDate.setText(getString(R.string.movie_details_release_date)+currentMovie.getmReleaseDate());
    }

    @Override
    public Loader<List<Movies>> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Movies>> loader, List<Movies> moviesList) {

    }

    @Override
    public void onLoaderReset(Loader<List<Movies>> loader) {

    }
}
