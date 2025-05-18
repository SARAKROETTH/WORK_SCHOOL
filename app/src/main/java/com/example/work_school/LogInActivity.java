package com.example.work_school;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.work_school.databinding.ActivityLogInBinding;
import com.example.work_school.databinding.ActivityWelcomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    private ActivityLogInBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//         Binding action
        binding.signUpTextView.setOnClickListener(v -> {
            StartSignUp();
        });
        binding.loginButton.setOnClickListener(v -> {
            StartLogin();
        });

    }

    private void StartLogin() {

    }

    private void StartSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}