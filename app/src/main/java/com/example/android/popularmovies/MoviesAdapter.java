package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bjoern on 13.03.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: The adapter class for the popular movies project stage 1
 */
public class MoviesAdapter extends ArrayAdapter <Movies> {

    /** Tag for log messages */
    private static final String LOG_TAG = MoviesAdapter.class.getName();

    /**
     * Our default constructor for the MoviesAdapter
     * @param context
     * @param moviesList
     */
    public MoviesAdapter (Context context, List<Movies> moviesList) {
        super(context, 0, moviesList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        Context context = getContext();

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.movie_item, parent, false);
            holder = new ViewHolder();
            holder.moviePoster = (ImageView) convertView.findViewById(R.id.iv_movieposter);
            holder.movietitle = (TextView) convertView.findViewById(R.id.tv_movietitle);
            holder.moviereleasedate = (TextView) convertView.findViewById(R.id.tv_release_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Movies currentMovie = getItem(position);
        // Suggestion by the Udacity review
        Picasso.with(context).load(currentMovie.getmPosterURL())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.moviePoster);
        holder.movietitle.setText(currentMovie.getmOrignalTitle());
        holder.moviereleasedate.setText(currentMovie.getmReleaseDate());


        return convertView;
    }

    static class ViewHolder {
        //ViewHolder class
        ImageView moviePoster;
        TextView movietitle;
        TextView moviereleasedate;
    }


}
