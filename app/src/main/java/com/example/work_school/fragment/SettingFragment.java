package com.example.work_school.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.work_school.R;
import androidx.preference.PreferenceFragmentCompat;
import com.example.work_school.databinding.FragmentSettingBinding;
import com.example.work_school.util.LocaleHelper;


public class SettingFragment extends PreferenceFragmentCompat {

    private FragmentSettingBinding binding;

    private static final String PREF_LANGUAGE = "app_language";



    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey){
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Language Preference Listener
        ListPreference languagePreference = findPreference(PREF_LANGUAGE);
        if (languagePreference != null) {
            languagePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                LocaleHelper.setLocale(requireActivity(), newValue.toString());
                requireActivity().recreate();
                return true;
            });
        }







    }

}