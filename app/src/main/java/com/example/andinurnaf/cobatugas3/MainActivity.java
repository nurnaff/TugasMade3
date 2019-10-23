package com.example.andinurnaf.cobatugas3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  implements  NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener{

    Toolbar toolbar;
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    MaterialSearchBar search_bar;
    RecyclerView recycler_view;

    private KamusHelper kamusHelper;
    private SearchAdapter adapter;

    private ArrayList<KamusModel> list = new ArrayList<>();
    private boolean isEnglish = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        drawer_layout=(DrawerLayout)findViewById(R.id.drawer_layout);
        nav_view=(NavigationView)findViewById(R.id.nav_view);
        recycler_view=(RecyclerView)findViewById(R.id.recycler_view);
        search_bar=(MaterialSearchBar)findViewById(R.id.search_bar);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.setDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);
        search_bar.setOnSearchActionListener(this);

        kamusHelper = new KamusHelper(this);

        setupList();
        loadData();
        nav_view.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_english_indonesia) {
            isEnglish = true;
            loadData();
        }

        if (id == R.id.nav_indonesia_english) {
            isEnglish = false;
            loadData();
        }

        if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + "\n\n" + getString(R.string.share_description));
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));
        }

        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onSearchStateChanged(boolean enabled) {

    }

    public void onSearchConfirmed(CharSequence text) {
        loadData(String.valueOf(text));
    }

    public void onButtonClicked(int buttonCode) {

    }

    private void setupList() {
        adapter = new SearchAdapter();
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);
    }

    private void loadData(String search) {
        try {
            kamusHelper.open();
            if (search.isEmpty()) {
                list = kamusHelper.getAllData(isEnglish);
            } else {
                list = kamusHelper.getDataByName(search, isEnglish);
            }

            if (isEnglish) {
                getSupportActionBar().setSubtitle(getResources().getString(R.string.english_indonesia));
            } else {
                getSupportActionBar().setSubtitle(getResources().getString(R.string.indonesia_english));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            kamusHelper.close();
        }
        adapter.replaceAll(list);
    }

    private void loadData() {
        loadData("");
    }
}