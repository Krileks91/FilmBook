package com.ftninformatika.termin27_webservices.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ftninformatika.termin27_webservices.Activities.MainActivity;
import com.ftninformatika.termin27_webservices.Model.Movie;
import com.ftninformatika.termin27_webservices.R;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.Calendar;

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
        Button buttonDate = view.findViewById(R.id.buttonSelectDate);
        Button buttonTime = view.findViewById(R.id.buttonSelectTime);

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
            }
        });

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "TimePicker");
            }
        });

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

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Toast.makeText(getActivity(), dayOfMonth + "/" + month + "/" + year + ".", Toast.LENGTH_SHORT).show();
        }
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();

            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minutes = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minutes, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Toast.makeText(getActivity(), hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
        }
    }
}


