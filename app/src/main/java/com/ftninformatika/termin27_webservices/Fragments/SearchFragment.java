package com.ftninformatika.termin27_webservices.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ftninformatika.termin27_webservices.Adapters.SearchListViewAdapter;
import com.ftninformatika.termin27_webservices.Model.Result;
import com.ftninformatika.termin27_webservices.Model.Search;
import com.ftninformatika.termin27_webservices.Net.WebServices.RESTService;
import com.ftninformatika.termin27_webservices.R;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private EditText etSearch;
    private Button bSearch;
    private ListView lvMovies;

    private onListItemClickListener listener;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        etSearch = view.findViewById(R.id.editText_Search);
        bSearch = view.findViewById(R.id.buttonSearch);
        lvMovies = view.findViewById(R.id.listMovies);

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.onEditorAction(EditorInfo.IME_ACTION_DONE); //Hide keyboard
                if (!etSearch.getText().toString().equals("")) {
                    getMoviesByTitle(etSearch.getText().toString().trim());
                } else {
                    Toast.makeText(getActivity(), getString(R.string.search_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void getMoviesByTitle(String title) {
        HashMap<String, String> query = new HashMap<>();
        query.put("apikey", RESTService.API_KEY);
        query.put("s", title);

        Call<Result> call = RESTService.apiInterface().getMoviesByTitle(query);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.code() == 200) {
                    showMovies(response.body().getSearch());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showMovies(List<Search> movies) {
        if (movies != null) {
            SearchListViewAdapter adapter = new SearchListViewAdapter(getActivity(), movies);
            lvMovies.setAdapter(adapter);
            lvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listener.onListItemClicked(adapter.getItem(position).getImdbID());
                }
            });
        }
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onListItemClickListener) {
            listener = (onListItemClickListener) context;
        } else {
            Toast.makeText(getActivity(), "Morate implementirati intefrace", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    public interface onListItemClickListener {
        void onListItemClicked(String ImdbID);
    }

}