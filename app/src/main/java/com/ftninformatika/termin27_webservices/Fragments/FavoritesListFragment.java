package com.ftninformatika.termin27_webservices.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ftninformatika.termin27_webservices.Adapters.SearchFavoritesListViewAdapter;
import com.ftninformatika.termin27_webservices.Model.Movie;
import com.ftninformatika.termin27_webservices.Net.ORMLight.DatabaseHelper;
import com.ftninformatika.termin27_webservices.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

public class FavoritesListFragment extends Fragment {

    private ListView lvMovies;
    private List<Movie> movies = null;
    private DatabaseHelper databaseHelper;
    private onItemClickListener listener;

    public FavoritesListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites_list, container, false);

        lvMovies = view.findViewById(R.id.lvMoviesFavorites);

        try {
            movies = getDatabaseHelper().getMovieDao().queryForAll();
            SearchFavoritesListViewAdapter adapter = new SearchFavoritesListViewAdapter(getActivity(), movies);
            lvMovies.setAdapter(adapter);
            lvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listener.onItemClicked(adapter.getItem(position));
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return view;
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onItemClickListener) {
            listener = (onItemClickListener) context;
        } else {
            Toast.makeText(getActivity(), "Morate implementirati intefrace", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface onItemClickListener {
        void onItemClicked(Movie movie);
    }

}