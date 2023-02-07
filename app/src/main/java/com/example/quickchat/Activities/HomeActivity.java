package com.example.quickchat.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.quickchat.Adapters.UsersAdapter;
import com.example.quickchat.Retrofit.ApiController;
import com.example.quickchat.Models.Users;
import com.example.quickchat.R;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private SwipeRefreshLayout refreshLayout;
    UsersAdapter usersAdapter;
    public static String sendImage;
    RecyclerView recyclerView;
    ArrayList<Users> usersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        refreshLayout = findViewById(R.id.swipeControl);
        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        recyclerView = findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersArrayList = new ArrayList<>();

        // setting toolbar
        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        // Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // fetching user data from server
        processData();

        // refreshing data in real time
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                processData();
                refreshLayout.setRefreshing(false);
            }
        });
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void processData() {
        Call<ArrayList<Users>> call = ApiController.getInstance().getApi().getUsers();
        usersArrayList.clear();
        call.enqueue(new Callback<ArrayList<Users>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Users>> call, @NonNull Response<ArrayList<Users>> response) {
                ArrayList<Users> data = response.body();
                if (response.isSuccessful()) {
                    usersArrayList = data;

                    // removing current user from layout
                    Users currentUser = getCrntUser();

                    // profile pic for chat activity
                    if (currentUser != null) {
                        sendImage = currentUser.getProfile_pic();
                    }
                    usersArrayList.remove(currentUser);

                    usersAdapter = new UsersAdapter(usersArrayList, HomeActivity.this);
                    recyclerView.setAdapter(usersAdapter);

                    usersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Users>> call, @NonNull Throwable t) {
                Log.e("Users Response", "onFailure: " + t.getMessage());
                Toast.makeText(HomeActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // getting Current User
    public Users getCrntUser() {
        String currentUserEmail = getSharedPreferences("credentials", MODE_PRIVATE)
                .getString("user", "");
        for (Users user1 : usersArrayList) {
            if (user1.getEmail().equals(currentUserEmail)) {
                return user1;
            }
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.options_logout) {
            logout();
        } else if (item.getItemId() == R.id.options_settings) {
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Dialog dialog = new Dialog(HomeActivity.this, R.style.Dialog);
        dialog.setContentView(R.layout.dialog_logout);

        Button yes, no;

        yes = dialog.findViewById(R.id.dialog_yes);
        no = dialog.findViewById(R.id.dialog_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}