package com.example.work_school;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.work_school.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends BaseActivity {

    private ActivitySignUpBinding binding;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        binding.signupButton.setOnClickListener(v -> {
            StartSignUp();
        });


//        Binding Action
        binding.textLogin.setOnClickListener(v -> {
            StartLogin();
        });

    }

    private void StartSignUp() {
        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();
        String confirmPassword = binding.inputComfirmPassword.getText().toString();

        if(!password.equals(confirmPassword)){
            Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show();
            return;
        }else {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent loginIntent = new Intent(SignUpActivity.this, LogInActivity.class);
                        loginIntent.putExtra("email", email);
                        loginIntent.putExtra("password", password);
                        setResult(RESULT_OK,loginIntent);
                        finish();
                    }else {
                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


    }

    private void StartLogin() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
}