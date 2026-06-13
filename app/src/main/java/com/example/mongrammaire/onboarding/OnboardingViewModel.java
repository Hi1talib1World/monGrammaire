package com.example.mongrammaire.onboarding;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.mongrammaire.R;
import java.util.ArrayList;
import java.util.List;

public class OnboardingViewModel extends AndroidViewModel {

    private static final String PREF_NAME = "onboarding_prefs";
    private static final String KEY_IS_FIRST_RUN_COMPLETED = "is_first_run_completed";

    private final MutableLiveData<Integer> currentStepIndex = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> isLoadingTransition = new MutableLiveData<>(false);
    private final List<OnboardingStep> steps = new ArrayList<>();
    private final SharedPreferences sharedPreferences;

    public OnboardingViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        loadSteps();
    }

    private void loadSteps() {
        steps.add(new OnboardingStep(
                "Welcome to monGrammaire",
                "The ultimate companion for mastering French grammar with ease.",
                R.drawable.ic_onboarding_welcome // Assuming these exist or will be added
        ));
        steps.add(new OnboardingStep(
                "Interactive Lessons",
                "Learn through engaging cards and interactive exercises designed for all levels.",
                R.drawable.ic_onboarding_lessons
        ));
        steps.add(new OnboardingStep(
                "Track Your Progress",
                "Stay motivated by tracking your daily achievements and mastering new levels.",
                R.drawable.ic_onboarding_progress
        ));
    }

    public LiveData<Integer> getCurrentStepIndex() {
        return currentStepIndex;
    }

    public LiveData<Boolean> getIsLoadingTransition() {
        return isLoadingTransition;
    }

    public List<OnboardingStep> getSteps() {
        return steps;
    }

    public int getStepsCount() {
        return steps.size();
    }

    public void nextStep() {
        Integer current = currentStepIndex.getValue();
        if (current != null && current < steps.size() - 1) {
            setLoading(true);
            currentStepIndex.setValue(current + 1);
        } else {
            completeOnboarding();
        }
    }

    public void previousStep() {
        Integer current = currentStepIndex.getValue();
        if (current != null && current > 0) {
            setLoading(true);
            currentStepIndex.setValue(current - 1);
        }
    }

    public void completeOnboarding() {
        setLoading(true);
        sharedPreferences.edit().putBoolean(KEY_IS_FIRST_RUN_COMPLETED, true).apply();
        // The View will observe the loading/state to trigger navigation
    }

    private void setLoading(boolean loading) {
        isLoadingTransition.setValue(loading);
        if (loading) {
            // Simulate transition delay
            new android.os.Handler().postDelayed(() -> isLoadingTransition.setValue(false), 400);
        }
    }

    public boolean isFirstRunCompleted() {
        return sharedPreferences.getBoolean(KEY_IS_FIRST_RUN_COMPLETED, false);
    }
}
