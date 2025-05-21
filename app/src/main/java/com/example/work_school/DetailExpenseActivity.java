package com.example.work_school;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.work_school.databinding.ActivityDetailExpenseBinding;

import java.text.SimpleDateFormat;

public class DetailExpenseActivity extends BaseActivity {

    private ActivityDetailExpenseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDetailExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        if (intent != null) {
            String expenseId = intent.getStringExtra("expenseId");
            String amounts = intent.getStringExtra("Amounts");
            String currencys = intent.getStringExtra("Currencys");
            String expenseCategory = intent.getStringExtra("expenseCategory");
            String remark = intent.getStringExtra("Remark");
            String createdDate = intent.getStringExtra("CreatedDate");


            binding.showCategory.setText(expenseCategory);
            binding.showAmount.setText(amounts);
            binding.showCurrency.setText(currencys);
            binding.showDate.setText(createdDate);
        }




    }
}