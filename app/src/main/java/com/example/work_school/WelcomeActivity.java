package com.example.work_school;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.work_school.databinding.ActivityWelcomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;
    LottieAnimationView lottieAnimationView;
    private boolean isPlaying = false;

    private FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            reload();
        }

    }
    private void reload() {
        Intent MainIntent = new Intent(this,MainActivity.class);
        startActivity(MainIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        binding.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    binding.startButton.playAnimation();
                    StartLogin();
            }
        });
    }

    private void StartLogin() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.lottieAnimationView.cancelAnimation(); // Stop animation when activity goes to background
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       binding.startButton.cancelAnimation();
       binding.lottieAnimationView.cancelAnimation(); // Also stop animation when activity is destroyed
    }

}