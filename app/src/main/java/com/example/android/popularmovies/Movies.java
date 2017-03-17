package com.example.android.popularmovies;

import android.graphics.Movie;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by bjoern on 13.03.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: The main class of our project (Movies)
 * Includes also the serialsation of object via using parcel
 */
public final class Movies implements Parcelable {

    /** Tag for log messages */
    private static final String LOG_TAG = Movie.class.getName();

    /**Schema */
    private static final String URL_SCHEMA = "http";

    /**URL for our movie posters */
    private static final String POSTER_BASE_URL = "image.tmdb.org";

    /**URL folders */
    private static final String URL_FIRST_STAGE ="t";

    private static final String URL_SECOND_STAGE ="p";

    /**Poster size for our mobile device */
    private static final String POSTER_SIZE = "w500";

    /** private member variables of the class **/
    private String mOrignalTitle;                 //storing the movie title
    private String mOverview;                    //storing the movies overview
    private String mVoteAverage;                //storing the users average ratings
    private String mReleaseDate;               //storing the movies release date
    private String mPosterPath;               //storing the movies poster image name
    private String mPosterURL;               //the http path to the poster
    private String mBackdropPath;           //the second image of the movie
    private String mBackdropPathURL;        //the http path to the second image

    /**
     * Our constructor for this class
     * @param title                 the title of the movie
     * @param overview              the overview of the movie (synopsis)
     * @param voteaverage           the average user rating
     * @param releaseDate           the release date
     * @param posterpath            the path to the movie poster
     * @param backdroppath          the path for the second movie
     */
    public Movies (String title, String overview, String voteaverage,
                   String releaseDate, String posterpath, String backdroppath){
        mOrignalTitle = title;
        mOverview = overview;
        mVoteAverage = voteaverage;
        mReleaseDate = releaseDate;
        mPosterPath = posterpath;
        mBackdropPath = backdroppath;
        setPosterURL();
        setBackdropPathURL();
    }

    /**
     * The cunstructor for the parcel sync.
     * So we can use the whole object in the child intent
     * @param parcel
     */

    public Movies(Parcel parcel){
        this.mOrignalTitle = parcel.readString();
        this.mOverview = parcel.readString();
        this.mVoteAverage = parcel.readString();
        this.mReleaseDate = parcel.readString();
        this.mPosterPath = parcel.readString();
        this.mPosterURL = parcel.readString();
        this.mBackdropPath = parcel.readString();
        this.mBackdropPathURL = parcel.readString();
    }

    private void setPosterURL(){
        //creates the correct URL for the movie poster (will be displayed in the parent intent.
        Uri.Builder posterPath = new Uri.Builder();
        posterPath.scheme(URL_SCHEMA).authority(POSTER_BASE_URL).appendPath(URL_FIRST_STAGE)
                .appendPath(URL_SECOND_STAGE).appendPath(POSTER_SIZE).appendEncodedPath(mPosterPath);

        mPosterURL = posterPath.build().toString();

    }

    private void setBackdropPathURL(){
        //creates the correct URL for the movie picture (will be displayed in the child intent.
        Uri.Builder posterPath = new Uri.Builder();
        posterPath.scheme(URL_SCHEMA).authority(POSTER_BASE_URL).appendPath(URL_FIRST_STAGE)
                .appendPath(URL_SECOND_STAGE).appendPath(POSTER_SIZE).appendEncodedPath(mBackdropPath);

        mBackdropPathURL = posterPath.build().toString();
        Log.v("In der Klasse: ", mBackdropPathURL);

    }

    /** Basic get methods */
    public String getmOrignalTitle(){return mOrignalTitle;}
    public String getmOverview(){return mOverview;}
    public String getmVoteAverage(){return mVoteAverage;}
    public String getmReleaseDate() {return mReleaseDate;}
    public String getmPosterURL() {return mPosterURL;}
    public String getmBackdropPathURL() { return mBackdropPathURL; }

    @Override
    //Also for the parcel topic
    public int describeContents() {
        return 0;
    }

    @Override
    //Also for the parcel topic
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mOrignalTitle);
        parcel.writeString(mOverview);
        parcel.writeString(mVoteAverage);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mPosterPath);
        parcel.writeString(mPosterURL);
        parcel.writeString(mBackdropPath);
        parcel.writeString(mBackdropPathURL);
    }

    //Also for the parcel topic
    public static final Parcelable.Creator<Movies> CREATOR =
            new Parcelable.Creator<Movies>(){
                @Override
                public Movies createFromParcel(Parcel source) {
                    return new Movies(source);
                }

                @Override
                public Movies[] newArray(int size) {
                    return new Movies[size];
                }
            };

}
