package com.example.work_school.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.work_school.Dao.AppDatabase;
import com.example.work_school.Dao.MyroomDao;
import com.example.work_school.R;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.example.work_school.databinding.FragmentSettingBinding;
import com.example.work_school.model.Expense;
import com.example.work_school.repository.ExpenseRepository;
import com.example.work_school.repository.IApiCallback;
import com.example.work_school.util.LocaleHelper;
import com.example.work_school.util.NetworkUtil;
import com.example.work_school.util.ThemeHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SettingFragment extends PreferenceFragmentCompat {

    private FragmentSettingBinding binding;

    private static final String PREF_LANGUAGE = "app_language";

    private static final String PREF_THEME = "app_theme";

    private MyroomDao myroomDao;

    private static final String PREF_RESTORE_EXPENSE = "unsynced_tasks";

    private int currentPage = 1;

    int countUnsyncedExpense = 0;

    private FirebaseAuth mAuth;

    private ExpenseRepository repository;


    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        mAuth = FirebaseAuth.getInstance();

        repository = new ExpenseRepository(requireContext());

        AppDatabase appDatabase = AppDatabase.getInstance(requireContext());

        this.myroomDao = appDatabase.myroomDao();

        updateUnsyncedTasksCount();

        // Language Preference Listener
        ListPreference languagePreference = findPreference(PREF_LANGUAGE);
        if (languagePreference != null) {
            languagePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                LocaleHelper.setLocale(requireActivity(), newValue.toString());
                requireActivity().recreate();
                return true;
            });
        }

        // Theme Preference Listener
        ListPreference themePreference = findPreference(PREF_THEME);
        if (themePreference != null) {
            themePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                ThemeHelper.setTheme(requireActivity(), newValue.toString());
                requireActivity().recreate();
                return true;
            });
        }

        // Backup Tasks Listener
        Preference backupPreference = findPreference("backup_tasks");
        if (backupPreference != null) {
            backupPreference.setOnPreferenceClickListener(preference -> {
                backupTasks();
                return true;
            });
        }

        Preference unsyncedTasksPreference = findPreference(PREF_RESTORE_EXPENSE);
        if (unsyncedTasksPreference != null) {
            unsyncedTasksPreference.setOnPreferenceClickListener(preference -> {
                if(countUnsyncedExpense > 0 && NetworkUtil.isNetworkAvailable(requireContext())){
                    showSyncConfirmationDialog();
                }
                return true;
            });
        }




    }

    private void showSyncConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Sync Tasks")
                .setMessage("You have " + countUnsyncedExpense + " unsynced tasks. Do you want to sync now?")
                .setPositiveButton("Sync", (dialog, which) -> syncUnsyncedExpense())
                .setNegativeButton("Cancel", null)
                .show();

    }

    private void syncUnsyncedExpense() {
        repository.syncTasks(new IApiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                updateUnsyncedTasksCount();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                updateUnsyncedTasksCount();
            }
        });

    }

    private void updateUnsyncedTasksCount() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            countUnsyncedExpense = myroomDao.getUnsyncedExpense().size();

            // Post result to main thread
            handler.post(() -> {
                Preference unsyncedTasksPreference = findPreference("unsynced_tasks");
                if (unsyncedTasksPreference != null) {
                    unsyncedTasksPreference.setSummary(getString(R.string.unsynced_expense_summary,countUnsyncedExpense));
                }
            });
        });
    }

    private void backupTasks() {
        String currentUserId = mAuth.getCurrentUser().getUid();
        repository.getExpenses(currentPage, currentUserId, new IApiCallback<List<Expense>>() {
            @Override
            public void onSuccess(List<Expense> result) {
                try {
                    // Generate a unique filename with timestamp
                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    String filename = "tasks_backup_" + timestamp + ".json";

                    // Convert tasks to JSON
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String jsonTasks = gson.toJson(result);

                    // Write serialized json to file
                    try (FileOutputStream fos = requireContext().openFileOutput(filename, Context.MODE_PRIVATE)) {
                        fos.write(jsonTasks.getBytes());
                        Toast.makeText(getContext(), "Tasks backed up to " + filename, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(getContext(), "Error backing up tasks " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }




}