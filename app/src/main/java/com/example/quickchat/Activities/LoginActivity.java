package com.example.quickchat.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.quickchat.Models.Users;
import com.example.quickchat.Retrofit.ApiController;
import com.example.quickchat.Models.Login_resp;
import com.example.quickchat.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView forgotPassword;
    TextView signUp;
    EditText TextEmail, TextPassword;
    Button login;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        verifyUserExistence();

        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        signUp = (TextView) findViewById(R.id.sign_up);
        TextEmail = (EditText) findViewById(R.id.enter_email);
        login = findViewById(R.id.button_login);
        TextPassword = (EditText) findViewById(R.id.enter_password);
        progressBar = findViewById(R.id.login_progress_bar);

        Drawable drawable = TextPassword.getCompoundDrawables()[2];

        AwesomeValidation mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        mAwesomeValidation.addValidation(this, R.id.enter_password, RegexTemplate.NOT_EMPTY, R.string.error);
        mAwesomeValidation.addValidation(this, R.id.enter_email, RegexTemplate.NOT_EMPTY, R.string.error);

        TextPassword.setOnTouchListener((view, motionEvent) -> {
            final int DRAWABLE_RIGHT = 2;
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= (TextPassword.getRight() - TextPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (TextPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                        TextPassword.setTransformationMethod(null);
                        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                        drawable.setAlpha(255);
                    } else {
                        TextPassword.setTransformationMethod(new PasswordTransformationMethod());
                        drawable.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                        drawable.setAlpha(255);
                    }
                    // password.performClick();
                    return true;
                }
            }
            return false;
        });
        SpannableString forget = new SpannableString("Forgot Password ?");
        forget.setSpan(new UnderlineSpan(), 0, forget.length(), 0);
        forgotPassword.setText(forget);

        SpannableString sign_up = new SpannableString(getResources().getString(R.string.no_account_sign_up));
        sign_up.setSpan(new UnderlineSpan(), 0, sign_up.length(), 0);
        signUp.setText(sign_up);

        signUp.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAwesomeValidation.validate()) {
                    progressBar.setVisibility(View.VISIBLE);
                    login.setVisibility(View.GONE);
                    String email = TextEmail.getText().toString().trim();
                    String password = TextPassword.getText().toString().trim();

                    userLogin(email, password);
                }
            }
        });
    }

    private void userLogin(String email, String password) {
        Call<Login_resp> call = ApiController.getInstance().getApi()
                .getLogin(email, password);
        call.enqueue(new Callback<Login_resp>() {
            @Override
            public void onResponse(@NonNull Call<Login_resp> call, @NonNull Response<Login_resp> response) {
                Login_resp rep_model = response.body();
                assert rep_model != null;
                String result = rep_model.getMessage().trim();
                if (result.equals("success")) {
                    sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user", email);
                    editor.putString("password", password);
                    editor.apply();

                    progressBar.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);
                    TextEmail.setText("");
                    TextPassword.setText("");
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();

                } else if (result.equals("failed")) {
                    progressBar.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);
                    TextEmail.setText("");
                    TextPassword.setText("");
                    Toast.makeText(LoginActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login_resp> call, Throwable t) {
                Log.e("myerror", "onFailure: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void verifyUserExistence() {
        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        if (sharedPreferences.contains("user")) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
    }
}