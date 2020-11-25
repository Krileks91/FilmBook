package com.ftninformatika.termin27_webservices.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ftninformatika.termin27_webservices.Adapters.DrawerListViewAdapter;
import com.ftninformatika.termin27_webservices.Dialogs.DialogAbout;
import com.ftninformatika.termin27_webservices.Fragments.DetailsFragment;
import com.ftninformatika.termin27_webservices.Fragments.FavoriteDetailsFragment;
import com.ftninformatika.termin27_webservices.Fragments.FavoritesListFragment;
import com.ftninformatika.termin27_webservices.Fragments.SearchFragment;
import com.ftninformatika.termin27_webservices.Fragments.SettingsFragment;
import com.ftninformatika.termin27_webservices.Model.Movie;
import com.ftninformatika.termin27_webservices.Model.NavigationItem;
import com.ftninformatika.termin27_webservices.Net.ORMLight.DatabaseHelper;
import com.ftninformatika.termin27_webservices.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchFragment.onListItemClickListener, FavoritesListFragment.onItemClickListener {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private CharSequence drawerTitle;
    private CharSequence title;

    private DatabaseHelper databaseHelper;

    private final ArrayList<NavigationItem> navigationItems = new ArrayList<>();

    private boolean searchShown = false;
    private boolean detailsShown = false;
    private boolean settingsShown = false;
    private boolean favoritesListShown = false;
    private boolean favoritesShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawer();
        showSearchFragment();
    }

    private void setupDrawer() {
        setupDrawerNavigationItems();
        title = drawerTitle = getTitle();
        setupDrawerItems();
        setupToolbar();
    }

    private void setupDrawerNavigationItems() {
        navigationItems.add(new NavigationItem(getString(R.string.drawer_home_title), getString(R.string.drawer_home_subtitle), R.drawable.home_icon));
        navigationItems.add(new NavigationItem(getString(R.string.drawer_settings_title), getString(R.string.drawer_settings_subtitle), R.drawable.settings_icon));
        navigationItems.add(new NavigationItem(getString(R.string.drawer_about_title), getString(R.string.drawer_about_subtitle), R.drawable.about_icon));
        navigationItems.add(new NavigationItem(getString(R.string.drawer_favorites_title), getString(R.string.drawer_favorites_subtitle), R.drawable.star_icon));
    }

    private void setupDrawerItems() {
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.leftDrawer);

        DrawerListViewAdapter adapter = new DrawerListViewAdapter(navigationItems, this);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showSearchFragment();
                        break;
                    case 1:
                        showSettingsFragment();
                        break;
                    case 2:
                        showAboutDialog();
                        break;
                    case 3:
                        showFavoritesListFragment();
                        break;
                }
                drawerLayout.closeDrawer(drawerList);
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }

        new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };
    }

    private void showSearchFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SearchFragment searchFragment = new SearchFragment();
        transaction.replace(R.id.root, searchFragment);
        transaction.commit();

        searchShown = true;
        detailsShown = false;
        settingsShown = false;
        favoritesListShown = false;
        favoritesShown = false;
    }

    private void showFavoritesListFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FavoritesListFragment favoritesListFragment = new FavoritesListFragment();
        transaction.replace(R.id.root, favoritesListFragment);
        transaction.commit();

        searchShown = false;
        detailsShown = false;
        settingsShown = false;
        favoritesListShown = true;
        favoritesShown = false;
    }

    private void showFavoritesDetailsFragment(Movie movie) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FavoriteDetailsFragment favoriteDetailsFragment = new FavoriteDetailsFragment();
        favoriteDetailsFragment.setMovie(movie);
        transaction.replace(R.id.root, favoriteDetailsFragment);
        transaction.commit();

        searchShown = false;
        detailsShown = false;
        settingsShown = false;
        favoritesListShown = false;
        favoritesShown = true;
    }

    private void showDetailsFragment(String ImdbID) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setImdbID(ImdbID);
        transaction.replace(R.id.root, detailsFragment);
        transaction.commit();

        searchShown = false;
        detailsShown = true;
        settingsShown = false;
        favoritesListShown = false;
        favoritesShown = false;
    }

    private void showSettingsFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SettingsFragment settingsFragment = new SettingsFragment();
        transaction.replace(R.id.root, settingsFragment);
        transaction.commit();

        searchShown = false;
        detailsShown = false;
        settingsShown = true;
        favoritesListShown = false;
    }

    private void showAboutDialog() {
        AlertDialog dialog = new DialogAbout(this).prepareDialog();
        dialog.show();
    }

    @Override
    public void onListItemClicked(String ImdbID) {
        showDetailsFragment(ImdbID);
    }

    @Override
    public void onItemClicked(Movie movie) {
        showFavoritesDetailsFragment(movie);
    }

    @Override
    public void onBackPressed() {
        if (searchShown) {
            finish();
        } else if (detailsShown) {
            getSupportFragmentManager().popBackStack();
            showSearchFragment();
        } else if (settingsShown) {
            getSupportFragmentManager().popBackStack();
            showSearchFragment();
        } else if (favoritesListShown) {
            getSupportFragmentManager().popBackStack();
            showSearchFragment();
        } else if (favoritesShown) {
            getSupportFragmentManager().popBackStack();
            showFavoritesListFragment();
        }
    }
    //Metoda koja komunicira sa bazom podataka
    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

}