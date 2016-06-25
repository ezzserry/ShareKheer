package net.AWstreams.ShareKhher.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import net.AWstreams.ShareKhher.R;
import net.AWstreams.ShareKhher.utils.ConnectionDetector;
import net.AWstreams.ShareKhher.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, FacebookCallback<LoginResult> {
    //    private LoginButton btnFb_login;
    private Button btnLogin;
    private CallbackManager callbackManager;
    private EditText etUsername, etPassword;
    String sUsername, sPassword;
    TextView tvSignup;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;

    ScrollView sScrollview;
    ProgressDialog loading = null;
    Typeface typefaceLight, typefaceBold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();


    }

    private void initViews() {
        typefaceLight = Typeface.createFromAsset(getAssets(), Constants.FONT_REGULAR_AR);
        typefaceBold = Typeface.createFromAsset(getAssets(), Constants.FONT_BOLD_AR);
        etUsername = (EditText) findViewById(R.id.username_et);
        etPassword = (EditText) findViewById(R.id.password_et);
//        etUsername.setTypeface(typefaceLight);
//        etPassword.setTypeface(typefaceLight);
        btnLogin = (Button) findViewById(R.id.login_btn);
        btnLogin.setTypeface(typefaceBold);
        btnLogin.setOnClickListener(this);

        tvSignup = (TextView) findViewById(R.id.signup_tv);
        tvSignup.setTypeface(typefaceLight);
        tvSignup.setOnClickListener(this);

//        btnFb_login = (LoginButton) findViewById(R.id.btn_fb_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
//        btnFb_login.registerCallback(callbackManager, this);

        sScrollview = (ScrollView) findViewById(R.id.login_form);

        loading = new ProgressDialog(this);
        loading.setCancelable(true);
        loading.setMessage("جاري التحميل");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                cd = new ConnectionDetector(getApplicationContext());
                isInternetPresent = cd.isConnectingToInternet();

                // check for Internet status
                if (isInternetPresent) {
                    sUsername = etUsername.getText().toString();
                    sPassword = etPassword.getText().toString();
                    if (!sUsername.isEmpty() && isPasswordValid(sPassword)) {
                        login(sUsername, sPassword);
                    } else
                        Snackbar.make(sScrollview, "Fill your full data ", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                } else
                    Snackbar.make(sScrollview, "لا يوجد اتصال بالانترنت", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                break;
            case R.id.signup_tv:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }

    private void login(String sUsername, String sPassword) {
        loading.show();
        String url = Constants.Sign_in_SERVICE;
        Map<String, String> params = new HashMap();
        params.put("username", sUsername);
        params.put("password", sPassword);
//        url = String.format(url, sUsername, sPassword);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status_code = response.getString("code");
                            if (status_code.equals("OK")) {
                                String User_Token = response.getJSONObject("data").getString("token");
                                JSONObject jsonObject_user = response.getJSONObject("data").getJSONObject("user");
                                String username = jsonObject_user.getString("username");
                                String email = jsonObject_user.getString("email");
                                String user_id = jsonObject_user.getString("id");

                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString(Constants.User_Token, User_Token);
                                editor.putString(Constants.User_name, username);
                                editor.putString(Constants.User_email, email);
                                editor.putString(Constants.KEY_USER_ID, user_id);
                                editor.putBoolean(Constants.isLoggedin, true);

                                editor.apply();
                                loading.dismiss();
                                Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                                startActivity(intent);
                                finish();

                            } else if (status_code.equals("E_USER_NOT_FOUND")) {
                                loading.dismiss();
                                Snackbar.make(sScrollview, "هذا المستخدم غير موجود", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(sScrollview, "حدث خطأ", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                            loading.dismiss();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Snackbar.make(sScrollview, "حدث خطأ", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        loading.dismiss();
                    }
                }
        ) {

        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);

    }

    @Override
    public void onSuccess(final LoginResult loginResult) {
        AccessToken accessToken = loginResult.getAccessToken();
        Profile profile = Profile.getCurrentProfile();
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Bundle bFacebookData = getFacebookData(object);
                            String UserID = loginResult.getAccessToken().getUserId();
                            String Auth_Token = loginResult.getAccessToken().getToken();
                            String Gender = object.getString("gender");
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString(Constants.Facebook_Username, object.getString("name"));
                            editor.apply();

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public void onCancel() {
        Snackbar.make(sScrollview, "لقد قمت بإلغاء محاولة الدخول السابقة", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onError(FacebookException error) {

    }

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;

        }

    }

}