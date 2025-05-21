package com.example.work_school;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.work_school.databinding.ActivityMainBinding;
import com.example.work_school.fragment.ExpenseListFragment;
import com.example.work_school.fragment.HomeFragment;
import com.example.work_school.fragment.SettingFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    private FirebaseAuth mAuth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.signout){
            mAuth.signOut();
            Intent intent = new Intent(this,WelcomeActivity.class);
            startActivity(intent);
        }else if (itemId == R.id.AddNewExpense){
            Intent intent = new Intent(this, AddNewExpenseActivity.class);
            startActivity(intent);
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);

        mAuth = FirebaseAuth.getInstance();

        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LoadFragment(new ExpenseListFragment());

        setSupportActionBar(binding.topAppBar);
        getSupportActionBar().setTitle("");


//        action binding

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_expenseList) {
                LoadFragment(new ExpenseListFragment());
            }else {
                LoadFragment(new SettingFragment());
            }
            return true;
        });




    }

    private void LoadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}