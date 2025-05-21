package com.example.work_school;

import static com.example.work_school.util.DateConvertor.convertToDate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.work_school.databinding.ActivityAddNewExpenseBinding;
import com.example.work_school.fragment.HomeFragment;
import com.example.work_school.model.Expense;
import com.example.work_school.repository.ExpenseRepository;
import com.example.work_school.repository.IApiCallback;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Phaser;

public class AddNewExpenseActivity extends BaseActivity {

    private ActivityAddNewExpenseBinding binding ;

    MaterialDatePicker datePicker;
    MaterialTimePicker timePicker;

    private String selectedCurrency;

    private ExpenseRepository repository;

    private FirebaseAuth mAuth;

    Calendar calendar ;

    private String selectCategories;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddNewExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        repository = new ExpenseRepository();

        mAuth =FirebaseAuth.getInstance();



        calendar = calendar.getInstance();

        String[] item = getResources().getStringArray(R.array.item_spiner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, item);
        binding.CategoryInput.setAdapter(adapter);

        binding.CategoryInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectCategories = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectCategories = " Not Have Item";
            }
        });

        binding.DateInput.setOnClickListener(v -> showDatePicker());
        binding.imageCalender.setOnClickListener(v -> showTimePicker());
        binding.buttonAddexpense.setOnClickListener(v -> AddExpense());

    }

    private void AddExpense() {
        String Amount = binding.AmountInput.getText().toString();
        String Remark = binding.RemarkInput.getText().toString();
        int selectedId = binding.chooseCurrent.getCheckedRadioButtonId();
        String Date = binding.DateInput.getText().toString();
        String Time = binding.timeinput.getText().toString();

        if (Amount.isEmpty() || Remark.isEmpty() || Date.isEmpty() || Time.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedId != -1){
            RadioButton selectedRadioButton = findViewById(selectedId);
            selectedCurrency = selectedRadioButton.getText().toString();
        }
        Expense expense = new Expense(Integer.parseInt(Amount),selectedCurrency,selectCategories,Remark,mAuth.getCurrentUser().getUid(),convertToDate(Date,Time));
        repository.createExpense(expense, new IApiCallback<Expense>() {

            @Override
            public void onSuccess(Expense result) {
                hideProgressBar();
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                hideProgressBar();
                Toast.makeText(AddNewExpenseActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void hideProgressBar() {
        binding.expenseProgressBar.setVisibility(View.GONE);
    }

    private void showTimePicker() {
        new TimePickerDialog(
                AddNewExpenseActivity.this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    binding.timeinput.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        ).show();
    }

    private void showDatePicker() {
        new DatePickerDialog(
                AddNewExpenseActivity.this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    binding.DateInput.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }










}