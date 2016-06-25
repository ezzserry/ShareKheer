package net.AWstreams.ShareKhher.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.AWstreams.ShareKhher.R;
import net.AWstreams.ShareKhher.utils.ConnectionDetector;
import net.AWstreams.ShareKhher.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText etFirstname, etUsername, etPassword, etRePassword, etPhonenumber;
    private String sName, sUsername, sPassword, sRePassword, sEmail, sPhonenumber;
    private View mProgressView;
    private View mRegisterFormView;
    private Button btnRegister;
    private ScrollView sScrollview;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;

    ProgressDialog loading = null;
    Typeface typefaceLight, typefaceBold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        initViews();
    }

    private void initViews() {
        loading = new ProgressDialog(this);
        loading.setCancelable(true);
        loading.setMessage("جاري التحميل");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        typefaceLight = Typeface.createFromAsset(getAssets(), Constants.FONT_REGULAR_AR);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        etPassword = (EditText) findViewById(R.id.password_et);
        etUsername = (EditText) findViewById(R.id.username_et);
        etFirstname = (EditText) findViewById(R.id.firstname_et);
        etRePassword = (EditText) findViewById(R.id.repassword_et);
        etPhonenumber = (EditText) findViewById(R.id.phonenumber_et);

        mEmailView.setTypeface(typefaceLight);
        etPassword.setTypeface(typefaceLight);
        etUsername.setTypeface(typefaceLight);
        etFirstname.setTypeface(typefaceLight);
        etPhonenumber.setTypeface(typefaceLight);
        etRePassword.setTypeface(typefaceLight);


        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.login_progress);
        sScrollview = (ScrollView) findViewById(R.id.register_form);

        btnRegister = (Button) findViewById(R.id.register_btn);
        btnRegister.setOnClickListener(this);


    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private boolean registerValidation() {
        // Reset errors.
        etFirstname.setError(null);
        etUsername.setError(null);
        etPassword.setError(null);
        etRePassword.setError(null);
        mEmailView.setError(null);


        // Store values at the time of the login attempt.
        sEmail = mEmailView.getText().toString();
        sPassword = etPassword.getText().toString();
        sRePassword = etRePassword.getText().toString();
        sUsername = etUsername.getText().toString();
        sName = etFirstname.getText().toString();
        sPhonenumber = etPhonenumber.getText().toString();

        boolean cancel = true;

        // Check for the first name
        if (TextUtils.isEmpty(sName)) {
            etFirstname.setError(getString(R.string.error_enter_your_name));
            etFirstname.requestFocus();
            cancel = false;
        }
        // Check for a username
        if (TextUtils.isEmpty(sUsername)) {
            etUsername.setError(getString(R.string.error_username));
            etUsername.requestFocus();
            cancel = false;
        }
        // Check for a valid password, if the user entered one.
        if (sPassword.equals("") || sPassword.length() < 6) {
            etPassword.setError(getString(R.string.error_invalid_password));
            etPassword.requestFocus();
            cancel = false;
        }

        // Check for the repeated password is matching the password
        if (!sRePassword.equals(sPassword)) {
            etRePassword.setError(getString(R.string.error_re_password));
            etRePassword.requestFocus();
            cancel = false;
        }
        if (TextUtils.isEmpty(sEmail)) {
            mEmailView.setError(getString(R.string.error_empty_email));
            mEmailView.requestFocus();
            cancel = false;
        } else if (!isEmailValid(sEmail)) {
            mEmailView.setError(getString(R.string.error_invalid_email));

            cancel = false;
        }
        if (!isValidPhoneNumber(sPhonenumber)) {
            etPhonenumber.setError("خطأ في رقم الهاتف");
            cancel = false;
        }
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (!isInternetPresent) {

            Snackbar.make(sScrollview, "لا يوجد اتصال بالانترنت", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            cancel = false;
        }

        return cancel;
    }

    private void executeRegistration(String name, final String username, String password, String email, String phone) {
        Map<String, String> params = new HashMap();
        params.put("username", username);
        params.put("password", password);
        params.put("email", email);
        params.put("name", name);
        params.put("phone", phone);
        String url = Constants.REGISTER_SERVICE;
        JSONObject paramaters = new JSONObject(params);
//        url = String.format(url, username, email, password, name, sPhonenumber);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, paramaters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status_code = response.getString("code");
                            if (status_code.equals("CREATED")) {
                                Snackbar.make(sScrollview, "لقد تم التسجيل بنجاح", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                String User_Token = response.getJSONObject("data").getString("token");
                                JSONObject userJsonObject = response.getJSONObject("data").getJSONObject("user");
                                String Username = userJsonObject.getString("username");
                                String Phone = userJsonObject.getString("phone");
                                String UserID = userJsonObject.getString("id");
                                String email = userJsonObject.getString("email");

                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString(Constants.User_Token, User_Token);
                                editor.putString(Constants.User_name, Username);
                                editor.putString(Constants.User_phone, Phone);
                                editor.putString(Constants.KEY_USER_ID, UserID);
                                editor.putString(Constants.User_email, email);
                                editor.putBoolean(Constants.isLoggedin, true);

                                editor.apply();
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(), "لقد تم التسجيل بنجاح", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, BaseActivity.class);
                                startActivity(intent);
                                finish();

                            } else if (status_code.equals("E_VALIDATION")) {
                                loading.dismiss();
                                JSONObject data = response.getJSONObject("data");
                                try {
                                    if (data.getJSONArray("username") != null) {
                                        etUsername.setError( "ادخل اسم مستخدم اخر");
//                                        Snackbar.make(sScrollview, "ادخل اسم مستخدم اخر", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }
                                try {
                                    if (data.getJSONArray("email") != null) {
                                        mEmailView.setError( "لقد قمت بتسجيل هذا البريد الالكتروني من قبل");
//                                        Snackbar.make(sScrollview, "لقد قمت بتسجيل هذا البريد الالكتروني من قبل", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                    }
                                } catch (JSONException e) {

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(sScrollview, "ادخل اسم مستخدم أو بريدالاكتروني اخر", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            loading.dismiss();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        Snackbar.make(sScrollview, "ادخل اسم مستخدم أو بريدالاكتروني اخر", Snackbar.LENGTH_LONG)
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

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty() || phoneNumber.length() != 11 || !phoneNumber.startsWith("01")) {
            return false;
        } else
            return true;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn:
                if (registerValidation()) {
                    executeRegistration(sName, sUsername, sPassword, sEmail, sPhonenumber);
                    loading.show();
                }
                break;
        }

    }
}

