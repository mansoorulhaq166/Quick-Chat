package com.example.quickchat.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.quickchat.Models.Signup_resp;
import com.example.quickchat.R;
import com.example.quickchat.Retrofit.ApiController;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText regName, regEmail, regPassword, regMobile;
    Button regSubmit;
    TextView alreadyLogin;
    ProgressBar progressBar;
    CircleImageView profileImage;
    final int SELECT_PICTURE = 1;
    Bitmap bitmap;
    private static Bitmap defaultBitmap;
    private static String defaultEncodedImageString;
    SharedPreferences sharedPreferences;
    Uri imageUri;
    String encodedImageString;
    //   String regexPassword = ".{8,8}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        // encoding defaultImage
        defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_image);
        CompressImage compressImage = new CompressImage(defaultBitmap, getResources());
        compressImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        alreadyLogin = findViewById(R.id.already_login);
        regEmail = findViewById(R.id.reg_email);
        regName = findViewById(R.id.reg_name);
        regPassword = findViewById(R.id.reg_password);
        regMobile = findViewById(R.id.reg_mobile);
        regSubmit = findViewById(R.id.reg_submit);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        progressBar = findViewById(R.id.progress_bar);

        SpannableString already = new SpannableString(getResources().getString(R.string.register_olg_member));
        already.setSpan(new UnderlineSpan(), 0, already.length(), 0);
        alreadyLogin.setText(already);
        // using validation
        AwesomeValidation mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        mAwesomeValidation.addValidation(this, R.id.reg_email, Patterns.EMAIL_ADDRESS, R.string.err_email);
        mAwesomeValidation.addValidation(this, R.id.reg_name, RegexTemplate.NOT_EMPTY, R.string.err_name);

//        mAwesomeValidation.addValidation(this, R.id.reg_password, regexPassword,
//                R.string.err_password);

        mAwesomeValidation.addValidation(this, R.id.reg_password, RegexTemplate.NOT_EMPTY, R.string.error);
        mAwesomeValidation.addValidation(this, R.id.reg_email, RegexTemplate.NOT_EMPTY, R.string.error);
        mAwesomeValidation.addValidation(this, R.id.reg_mobile, RegexTemplate.NOT_EMPTY, R.string.error);

        alreadyLogin.setOnClickListener(view -> {
            Intent login_Activity = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(login_Activity);
        });

        regSubmit.setOnClickListener(view -> {
            if (mAwesomeValidation.validate()) {
                progressBar.setVisibility(View.VISIBLE);
                regSubmit.setVisibility(View.GONE);
                String name = regName.getText().toString();
                String email = regEmail.getText().toString();
                String password = regPassword.getText().toString();
                String mobile = regMobile.getText().toString();
                String status = "Hey, there I'm using this Application!";
                // String img = "Not uploaded";
                registerUser(name, email, mobile, password, status);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                selectImageLauncher.launch("image/*");
            }
        });
    }

    private void registerUser(String name, String email, String mobile, String password, String status) {

        if (encodedImageString == null) {
            encodedImageString = defaultEncodedImageString;
        }
        Call<Signup_resp> call = ApiController.getInstance().getApi()
                .getRegister(name, email, encodedImageString, mobile, password, status);
        call.enqueue(new Callback<Signup_resp>() {
            @Override
            public void onResponse(@NonNull Call<Signup_resp> call, @NonNull Response<Signup_resp> response) {
                Signup_resp repModel = response.body();
                assert repModel != null;
                String result = repModel.getMessage().trim();
                switch (result) {
                    case "registered":
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user", email);
                        editor.putString("password", password);
                        editor.apply();
                        progressBar.setVisibility(View.GONE);
                        regSubmit.setVisibility(View.VISIBLE);
                        Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                        regName.setText("");
                        regEmail.setText("");
                        regMobile.setText("");
                        regPassword.setText("");
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                        finish();
                        break;
                    case "Email exists":
                        progressBar.setVisibility(View.GONE);
                        regSubmit.setVisibility(View.VISIBLE);
                        Toast.makeText(RegisterActivity.this, "Already Registered", Toast.LENGTH_SHORT).show();
                        regName.setText("");
                        regEmail.setText("");
                        regMobile.setText("");
                        regPassword.setText("");
                        regName.requestFocus();
                        break;
                    case "failed":
                        progressBar.setVisibility(View.GONE);
                        regSubmit.setVisibility(View.VISIBLE);
                        Toast.makeText(RegisterActivity.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                        regName.setText("");
                        regEmail.setText("");
                        regMobile.setText("");
                        regPassword.setText("");
                        regName.requestFocus();
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<Signup_resp> call, @NonNull Throwable t) {
                Log.e("myerror", "onFailure: " + t.getMessage());
                Toast.makeText(RegisterActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private final ActivityResultLauncher<String> selectImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        imageUri = uri;
                        //   profileImage.setImageURI(uri);
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            profileImage.setImageBitmap(bitmap);
                            encodeBitmapImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        byte[] bytesOfImage = outputStream.toByteArray();
        encodedImageString = android.util.Base64.encodeToString(bytesOfImage, Base64.DEFAULT);

    }

    private static class CompressImage extends AsyncTask<Void, Void, String> {

        public CompressImage(Bitmap bitmap, Resources resources) {
        }

        @Override
        protected String doInBackground(Void... voids) {
            // encoding default image
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            defaultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayOutputStream);
            byte[] bytesArray = arrayOutputStream.toByteArray();
            defaultEncodedImageString = Base64.encodeToString(bytesArray, Base64.DEFAULT);
            return defaultEncodedImageString;
        }
    }
}