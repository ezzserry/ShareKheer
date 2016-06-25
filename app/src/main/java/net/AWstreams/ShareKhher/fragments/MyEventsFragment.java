package net.AWstreams.ShareKhher.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.AWstreams.ShareKhher.R;
import net.AWstreams.ShareKhher.activities.BaseActivity;
import net.AWstreams.ShareKhher.adapters.EventsRecyclerAdapter;
import net.AWstreams.ShareKhher.models.EventItem;
import net.AWstreams.ShareKhher.utils.ConnectionDetector;
import net.AWstreams.ShareKhher.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by LENOVO on 07/06/2016.
 */
public class MyEventsFragment extends Fragment {
    EventsRecyclerAdapter adapter;
    EditText etSearch;
    RecyclerView recyclerView;
    //    ProgressDialog loading = null;
    private ArrayList<EventItem> eventItemArrayList;

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    View view;
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Locale locale_ar = new Locale("ar");
        Locale.setDefault(locale_ar);
        Configuration config_ar = new Configuration();
        config_ar.locale = locale_ar;
        getActivity().getBaseContext().getResources().updateConfiguration(config_ar,
                getActivity().getBaseContext().getResources().getDisplayMetrics());

        view = inflater.inflate(R.layout.frgament_myevents, container, false);
        initViews();
        loadEvents();
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        // Toast.makeText(getActivity(), "Back Pressed",
                        // Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), BaseActivity.class);
                        startActivity(i);
                        getActivity().finish();
                        return true;
                    }
                }
                return false;
            }
        });
        return view;
    }

    private void initViews() {
//        loading = new ProgressDialog(getActivity());
//        loading.setCancelable(false);
//        loading.setMessage("جاري التحميل");
//        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        loading.show();
        progressBar = (ProgressBar) view.findViewById(R.id.loadingpanel);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void loadEvents() {
        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String Token = prefs.getString(Constants.User_Token, null);
            String UserID = prefs.getString(Constants.KEY_USER_ID, null);
            String url = Constants.GET_MY_EVENTS;
            url = String.format(url, UserID, Token);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status_code = response.getString("code");
                                if (status_code.equals("OK")) {
                                    eventItemArrayList = new ArrayList<>();
                                    JSONArray itemsArray = response.getJSONArray("data");
                                    for (int i = 0; i < itemsArray.length(); i++) {
                                        JSONObject object = (JSONObject) itemsArray.get(i);
                                        EventItem eventItem = new EventItem();
                                        eventItem.setName(object.getString("title"));
                                        eventItem.setAddress(object.getString("address"));
                                        eventItem.setType(object.getString("type"));
                                        eventItem.setDesc(object.getString("description"));
                                        eventItem.setDate(object.getString("date"));
                                        eventItem.setTime(object.getString("time"));
                                        eventItem.setCity(object.getString("city"));
                                        eventItem.setArea(object.getString("city"));
                                        eventItem.setLat(object.getString("lat"));
                                        eventItem.setLng(object.getString("lng"));
                                        eventItem.setPhone(object.getString("phone"));
                                        String thumbnailURL = object.getString("cover");
                                        String sFullURL = Constants.GET_PIC + thumbnailURL;
                                        eventItem.setThumbnail(sFullURL);
                                        eventItemArrayList.add(eventItem);
                                    }
                                    if (eventItemArrayList.size() != 0) {

                                        adapter = new EventsRecyclerAdapter(getActivity(), eventItemArrayList);
                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        Toast.makeText(getActivity(), "عفوا لا توجد نتائج", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
//                                    loading.dismiss();
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
//                                    loading.dismiss();
                                    Snackbar.make(recyclerView, "حدث خطأ", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Snackbar.make(recyclerView, "حدث خطأ", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
//                                loading.dismiss();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Snackbar.make(recyclerView, "حدث خطأ", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
//                            loading.dismiss();
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
            ) {

            };
            request.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getActivity()).add(request);
        } else {
//            loading.dismiss();
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "عفوا لا يوجد اتصال بالانترنت", Toast.LENGTH_LONG).show();
        }
    }
}
