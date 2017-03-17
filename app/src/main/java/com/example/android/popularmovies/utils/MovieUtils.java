package com.example.android.popularmovies.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmovies.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjoern on 13.03.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: Reused code from the 'newsapp' app project.
 */

public class MovieUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = MovieUtils.class.getSimpleName();

    public MovieUtils() { /*no default constructor*/ }

    private static URL createUrl(String stringUrl) {
        /**
         * Returns new URL object from the given string URL.
         */

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String mResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return mResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                mResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem getting the the movies results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return mResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Query the Movie and return a list of {@link Movies} objects.
     */
    public static List<Movies> fetchMovieData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Movies}
        List<Movies> movies = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link movies}
        return movies;
    }

    /**
     * Return a list of {@link Movies} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Movies> extractFeatureFromJson(String moviesJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(moviesJSON)) { return null; }
        // Create an empty ArrayList that we can start adding movies to
        List<Movies> movieList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(moviesJSON);

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of features.
            JSONArray MoviesArray = baseJsonResponse.getJSONArray("results");
            // For each book in the NewsArray, create an {@link News} object
            for (int i = 0; i < MoviesArray.length(); i++) {
                JSONObject currentMovie = MoviesArray.getJSONObject(i);
                //getting the path of the poster image
                String poster = currentMovie.getString("poster_path");
                //getting the synopsis of the movie
                String overview = currentMovie.getString("overview");
                //getting the release date
                String release_date = currentMovie.getString("release_date");
                //getting the movie title
                String original_title = currentMovie.getString("original_title");
                //getting the second image of the movie
                String backdrop_path = currentMovie.getString("backdrop_path");
                //getting the average vote of the movie
                String vote_average = currentMovie.getString("vote_average");
                //creating a new object
                Movies movie = new Movies(original_title, overview, vote_average, release_date, poster, backdrop_path);
                //add the object to the movielist.
                movieList.add(movie);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("MovieUtils", "Problem parsing the the movie JSON results", e);
        }
        // Return the list of movies
        return movieList;
    }


}
