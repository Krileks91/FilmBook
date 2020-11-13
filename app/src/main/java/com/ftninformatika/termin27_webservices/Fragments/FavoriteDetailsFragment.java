package com.ftninformatika.termin27_webservices.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ftninformatika.termin27_webservices.Activities.MainActivity;
import com.ftninformatika.termin27_webservices.Model.Movie;
import com.ftninformatika.termin27_webservices.Net.ORMLight.DatabaseHelper;
import com.ftninformatika.termin27_webservices.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;

public class FavoriteDetailsFragment extends Fragment {

    private Movie movie;
    private RatingBar ratingBar;

    public FavoriteDetailsFragment() {
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite_details, container, false);

        setHasOptionsMenu(true);

        ImageView poster = view.findViewById(R.id.imageView_PosterFavorites);
        TextView tvTitle = view.findViewById(R.id.textView_TitleFavorites);
        TextView tvYear = view.findViewById(R.id.textView_YearFavorites);
        ratingBar = view.findViewById(R.id.ratingBar_Favorites);

        tvTitle.setText(movie.getTitle());
        tvYear.setText(movie.getYear());

        ratingBar.setRating((float) movie.getRating());

        Picasso.get().load(movie.getPoster()).into(poster);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.favorites_details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                doUpdateElement();
                break;
            case R.id.action_delete:
                doRemoveElement();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doUpdateElement() {
        movie.setRating(ratingBar.getRating());
        try {
            ((MainActivity) getActivity()).getDatabaseHelper().getMovieDao().update(movie);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Toast.makeText(getActivity(), getString(R.string.Updated), Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    private void doRemoveElement() {
        try {
            ((MainActivity) getActivity()).getDatabaseHelper().getMovieDao().delete(movie);
            Toast.makeText(getActivity(), getString(R.string.Deleted), Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}