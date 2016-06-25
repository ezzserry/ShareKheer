package net.AWstreams.ShareKhher.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.AWstreams.ShareKhher.R;
import net.AWstreams.ShareKhher.fragments.CreateEventFragment;
import net.AWstreams.ShareKhher.fragments.HomeFragment;
import net.AWstreams.ShareKhher.fragments.MyEventsFragment;
import net.AWstreams.ShareKhher.fragments.SearchByLocation;
import net.AWstreams.ShareKhher.fragments.SearchFragment;
import net.AWstreams.ShareKhher.models.CitiesList;
import net.AWstreams.ShareKhher.models.City;
import net.AWstreams.ShareKhher.utils.Constants;
import net.AWstreams.ShareKhher.utils.CustomTypefaceSpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageButton btnSearch;
    Toolbar toolbar;
    TextView tvUserName, tvEmail, tvSearchlocation, tvSearchType, tvSeach;
    Button btnFilter, btnFilterBylocation;
    RadioGroup rgSearch;
    RadioButton rbDonation, rbVolunteer, rbBoth;
    String type = "";
    Spinner spCities, spAreas;
    String sSelectedCity = "";
    String sSelectedArea = "";

    Typeface typefaceLight, typefaceBold;

    CitiesList citiesList;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Locale locale_ar = new Locale("ar");
        Locale.setDefault(locale_ar);
        Configuration config_ar = new Configuration();
        config_ar.locale = locale_ar;
        getBaseContext().getResources().updateConfiguration(config_ar,
                getBaseContext().getResources().getDisplayMetrics());

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
        View hView = navigationView.getHeaderView(0);
        tvUserName = (TextView) hView.findViewById(R.id.username_tv);
        tvEmail = (TextView) hView.findViewById(R.id.email_tv);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String Username = prefs.getString(Constants.User_name, "");
        String Email = prefs.getString(Constants.User_email, "");
        tvUserName.setText(Username);
        tvEmail.setText(Email);


        btnSearch = (ImageButton) findViewById(R.id.search_bar_ib);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        LinearLayout navigationViewleft = (LinearLayout) findViewById(R.id.nav2_view);
//        View hView1 = navigationViewleft.getHeaderView(0);
        typefaceLight = Typeface.createFromAsset(getAssets(), Constants.FONT_REGULAR_AR);
        typefaceBold = Typeface.createFromAsset(getAssets(), Constants.FONT_BOLD_AR);

        tvSearchlocation = (TextView) navigationViewleft.findViewById(R.id.searchloc_tv);
        tvSearchType = (TextView) navigationViewleft.findViewById(R.id.searchtype_tv);
        tvSeach = (TextView) navigationViewleft.findViewById(R.id.search_tv);
        rbBoth = (RadioButton) navigationViewleft.findViewById(R.id.both_rb);
        rbDonation = (RadioButton) navigationViewleft.findViewById(R.id.donation_rb);
        rbVolunteer = (RadioButton) navigationViewleft.findViewById(R.id.volunteer_rb);
        rgSearch = (RadioGroup) navigationViewleft.findViewById(R.id.myRadioGroup);
        spCities = (Spinner) navigationViewleft.findViewById(R.id.cities_spinner);
        spAreas = (Spinner) navigationViewleft.findViewById(R.id.areas_spinner);

        rbBoth.setTypeface(typefaceLight);
        rbDonation.setTypeface(typefaceLight);
        rbVolunteer.setTypeface(typefaceLight);
        tvSearchlocation.setTypeface(typefaceBold);
        tvSearchType.setTypeface(typefaceBold);
        tvSeach.setTypeface(typefaceBold);
        TextView tv = (TextView) findViewById(R.id.textviewspinner);
//        btnFilter = (Button) navigationViewleft.findViewById(R.id.search_btn);
//        btnFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int selectedId = rgSearch.getCheckedRadioButtonId();
//                // find which radioButton is checked by id
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                SharedPreferences.Editor editor = prefs.edit();
//
//                if (selectedId == rbDonation.getId()) {
//                    type = "Donation";
//                    editor.putString(Constants.FILTER_TYPE, type);
//                } else if (selectedId == rbVolunteer.getId()) {
//
//                    type = "Volunteering";
//                    editor.putString(Constants.FILTER_TYPE, type);
//
//
//                } else if (selectedId == rbBoth.getId()) {
//
//                    type = "Both";
//                    editor.putString(Constants.FILTER_TYPE, type);
//
//
//                }
//                editor.apply();
//                Fragment fragment = new SearchFragment();
//                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment).commit();
//
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.END);
//            }
//        });

        spCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sp1 = String.valueOf(spCities.getSelectedItem());
//                                                   Toast.makeText(getActivity(), sp1, Toast.LENGTH_SHORT).show();
                if (sp1.contentEquals("القاهرة")) {
                    sSelectedCity = "cairo";

                    ArrayList<City> cityArrayList = new ArrayList<>();

                    cityArrayList.add(new City("el mokattam", "المقطم"));
                    cityArrayList.add(new City("nasr city", "مدينة نصر"));
                    cityArrayList.add(new City("shoubra", "شبرا"));
                    cityArrayList.add(new City("masr el gdeda", "مصر الجديدة"));
                    cityArrayList.add(new City("helioplis", "هليوبليس"));
                    cityArrayList.add(new City("maadi", "المعادي"));
                    cityArrayList.add(new City("new cairo", "القاهرة الجديدة"));
                    cityArrayList.add(new City("abbasia", "العباسية"));
                    cityArrayList.add(new City("gesr el suiz", "جسر السويس"));
                    cityArrayList.add(new City("tahrir", "التحرير"));
                    cityArrayList.add(new City("hadaek el kobba", "حدائق القبة"));
                    citiesList = new CitiesList();
                    citiesList.setcairoArrayList(cityArrayList);
                    //fill data in spinner
                    ArrayAdapter<City> adapter = new ArrayAdapter<City>(getApplicationContext(), R.layout.spinner_item, cityArrayList);
                    adapter.setDropDownViewResource(R.layout.spinner_item);
                    adapter.notifyDataSetChanged();
                    spAreas.setAdapter(adapter);

                }
                if (sp1.contentEquals("الجيزة")) {
                    sSelectedCity = "giza";
                    ArrayList<City> cityArrayList = new ArrayList<>();
                    cityArrayList.add(new City("dokki", "الدقي"));
                    cityArrayList.add(new City("haram", "الهرم"));
                    cityArrayList.add(new City("feisal", "فيصل"));
                    cityArrayList.add(new City("agouza", "العجوزة"));
                    cityArrayList.add(new City("mohandseen", "المهندسين"));
                    cityArrayList.add(new City("talbeya", "الطالبية"));
                    citiesList = new CitiesList();
                    citiesList.setGizaArrayList(cityArrayList);


                    ArrayAdapter<City> adapter = new ArrayAdapter<City>(getApplicationContext(), R.layout.spinner_item, cityArrayList);
                    adapter.setDropDownViewResource(R.layout.spinner_item);
                    adapter.notifyDataSetChanged();
                    spAreas.setAdapter(adapter);
                }
                if (sp1.contentEquals("الاسكندرية")) {
                    sSelectedCity = "alex";
                    ArrayList<City> cityArrayList = new ArrayList<>();
                    cityArrayList.add(new City("somouha", "سموحة"));
                    cityArrayList.add(new City("sidi gaber", "سيدي جابر"));
                    cityArrayList.add(new City("moharram bek", "محرم بك"));
                    cityArrayList.add(new City("sporting", "سبورتنج"));
                    cityArrayList.add(new City("azarita", "الازاريطة"));
                    cityArrayList.add(new City("abo kir", "ابو قير"));
                    citiesList = new CitiesList();
                    citiesList.setAlexArrayList(cityArrayList);


                    ArrayAdapter<City> adapter = new ArrayAdapter<City>(getApplicationContext(), R.layout.spinner_item, cityArrayList);
                    adapter.setDropDownViewResource(R.layout.spinner_item);
                    adapter.notifyDataSetChanged();
                    spAreas.setAdapter(adapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City city = (City) parent.getSelectedItem();
//                Toast.makeText(getApplicationContext(), "arabic: " + city.getArabicCity() + ",  english : " + city.getEnglishCity(), Toast.LENGTH_SHORT).show();
                sSelectedArea = city.getEnglishCity();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(Constants.FILTER_LOCATION, sSelectedArea);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnFilterBylocation = (Button) navigationViewleft.findViewById(R.id.searchcities_btn);
        btnFilterBylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rgSearch.getCheckedRadioButtonId();
                // find which radioButton is checked by id
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();

                if (selectedId == rbDonation.getId()) {
                    type = "Donation";
                    editor.putString(Constants.FILTER_TYPE, type);
                } else if (selectedId == rbVolunteer.getId()) {

                    type = "Volunteering";
                    editor.putString(Constants.FILTER_TYPE, type);


                } else if (selectedId == rbBoth.getId()) {

                    type = "Both";
                    editor.putString(Constants.FILTER_TYPE, type);


                }
                editor.apply();
                Fragment fragment = new SearchByLocation();
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.END);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                    navigationView.getMenu().getItem(0).setChecked(true);

                } else
                    drawer.openDrawer(GravityCompat.END);


            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);

        } else {
            super.onBackPressed();
        }
    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), Constants.FONT_REGULAR_AR);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = new Fragment();
        Bundle args = new Bundle();

        if (id == R.id.nav_home) {
            // Handle the camera action
            fragment = new HomeFragment();
        } else if (id == R.id.nav_my_events) {
            fragment = new MyEventsFragment();
        } else if (id == R.id.nav_create_event) {
            fragment = new CreateEventFragment();

        } else if (id == R.id.nav_my_signout) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Constants.isLoggedin, false);
            editor.apply();
            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
