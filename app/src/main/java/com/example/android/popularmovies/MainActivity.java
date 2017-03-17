package com.example.android.popularmovies;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movies>>{

    /**BASE URL**/
    private static final String BASE_URL = "api.themoviedb.org";

    /**SCHEMA */
    private static final String URI_SCHEMA = "https";
    /**Building the path **/
    private static final String MOVIES_STAGE ="3";

    private static final String MOVIES_FOLDER ="movie";

    //The query command for getting the popular movies
    private static final String MOVIE_SORT_POPULAR ="popular";

    //The query commanf for getting the highest ratted movies
    private static final String MOVIE_SORT_TOP_RATED = "top_rated";

    /** API Key **/
    private static final String API_KEY = BuildConfig.API_KEY;

    private String COMPLETE_URI = null;

    private static final int MOVIE_LOADER_ID = 2;

    private MoviesAdapter mAdapter;

    @BindView(R.id.gridView) GridView gridView;

    @BindView(R.id.pb_loader) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAdapter = new MoviesAdapter(this, new ArrayList<Movies>());
        buildUri(MOVIE_SORT_POPULAR);
        // Set the adapter on the {@link GridView}
        // so the list can be populated in the user interface
        // set the adapter
        gridView.setAdapter(mAdapter);
        if (isNetworkConnected()) {
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(MainActivity.this, MovieDetails.class);
                    Movies currentMovie = mAdapter.getItem(position);
                    intent.putExtra("currentObject",currentMovie);
                    startActivity(intent);
                }
            });
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_LOADER_ID, null, MainActivity.this);
        }else {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }

    }

    private boolean isNetworkConnected() {
        // The helper function for checking if the network connection is working.
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void buildUri (String sortType) {
        //The correct URI will be created here
        Uri.Builder uri = new Uri.Builder();
        uri.scheme(URI_SCHEMA).authority(BASE_URL).appendPath(MOVIES_STAGE)
                .appendPath(MOVIES_FOLDER).appendPath(sortType)
                .appendQueryParameter("api_key", API_KEY);
        COMPLETE_URI = uri.build().toString();
    }

    @Override
    public Loader<List<Movies>> onCreateLoader(int i, Bundle bundle) {
        return new MovieLoader(this, COMPLETE_URI);
    }

    @Override
    public void onLoadFinished(Loader<List<Movies>> loader, List<Movies> moviesList) {
        // If there is a valid list of {@link moviesList}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        progressBar.setVisibility(View.GONE);
        mAdapter.clear();

        if (moviesList != null && !moviesList.isEmpty()) {
            mAdapter.addAll(moviesList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "prefence" option
            case R.id.action_preference:
                perferenceDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoaderReset(Loader<List<Movies>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    private void perferenceDialog(){
        // Creating the dialog for the perferences
        AlertDialog.Builder perferenceBuilder = new AlertDialog.Builder(this);
        perferenceBuilder.setTitle("Sort by");
        String[] types = {"most popular", "highest rated"};
        perferenceBuilder.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface perferenceDialog, int selectedItem) {

                perferenceDialog.dismiss();
                switch(selectedItem){
                    case 0:
                        buildUri(MOVIE_SORT_POPULAR);
                        getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, MainActivity.this);
                        break;
                    case 1:
                        buildUri(MOVIE_SORT_TOP_RATED);
                        getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, MainActivity.this);
                        break;
                }
            }

        });
        perferenceBuilder.show();
    }
}
