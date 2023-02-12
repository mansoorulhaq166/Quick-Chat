
package com.example.quickchat.Activities;

        import android.Manifest;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.provider.Settings;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.WindowManager;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

        import com.example.quickchat.Adapters.UsersAdapter;
        import com.example.quickchat.Models.Users;
        import com.example.quickchat.R;
        import com.example.quickchat.Retrofit.ApiController;

        import java.util.ArrayList;
        import java.util.List;

        import pub.devrel.easypermissions.EasyPermissions;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    SharedPreferences sharedPreferences;
    private SwipeRefreshLayout refreshLayout;
    UsersAdapter usersAdapter;
    public static String sendImage;
    public static String loggedUser;
    RecyclerView recyclerView;
    ArrayList<Users> usersArrayList;
    private static final int RC_Location = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        refreshLayout = findViewById(R.id.swipeControl);
        loggedUser = getSharedPreferences("credentials", MODE_PRIVATE).getString("user", "");
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
        switch (item.getItemId()) {
            case R.id.options_settings:
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                return true;
            case R.id.navigation_location:
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                } else {
                    // checking if location permission is granted
                    if (!EasyPermissions.hasPermissions(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        EasyPermissions.requestPermissions(HomeActivity.this, "This app requires location permission to get your current location.",
                                RC_Location, Manifest.permission.ACCESS_FINE_LOCATION);
                    } else {
                        Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
                        startActivity(intent);
                        return true;
                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }

//    private void logout() {
//        Dialog dialog = new Dialog(SplashActivity.this, R.style.Dialog);
//        dialog.setContentView(R.layout.dialog_logout);
//
//        Button yes, no;
//
//        yes = dialog.findViewById(R.id.dialog_yes);
//        no = dialog.findViewById(R.id.dialog_no);
//
//        yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
//                editor.apply();
//                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                finish();
//            }
//        });
//        no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == RC_Location) {
            Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == RC_Location) {
            Toast.makeText(this, "Location Permission is needed to get your current Location", Toast.LENGTH_SHORT).show();
        }
    }
}