package com.example.mongrammaire.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.mongrammaire.R;
import com.example.mongrammaire.databinding.ActivityOnboardingBinding;
import com.example.mongrammaire.home;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private OnboardingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(OnboardingViewModel.class);

        setupObservers();
        setupListeners();
        setupDots(viewModel.getStepsCount());
    }

    private void setupObservers() {
        viewModel.getCurrentStepIndex().observe(this, this::updateUI);
        viewModel.getIsLoadingTransition().observe(this, loading -> {
            binding.btnNext.setEnabled(!loading);
            binding.btnSkip.setEnabled(!loading);
            binding.btnBack.setEnabled(!loading);
            // Visual feedback during transition
            binding.imgStep.animate().alpha(loading ? 0.3f : 1.0f).setDuration(200).start();
            binding.tvTitle.animate().alpha(loading ? 0.3f : 1.0f).setDuration(200).start();
            binding.tvDescription.animate().alpha(loading ? 0.3f : 1.0f).setDuration(200).start();
        });
        viewModel.getOnboardingFinished().observe(this, finished -> {
            if (finished) {
                navigateToMain();
            }
        });
    }

    private void setupListeners() {
        binding.btnNext.setOnClickListener(v -> viewModel.nextStep());
        binding.btnSkip.setOnClickListener(v -> viewModel.completeOnboarding());
        binding.btnBack.setOnClickListener(v -> viewModel.previousStep());
    }

    private void setupDots(int count) {
        binding.layoutDots.removeAllViews();
        for (int i = 0; i < count; i++) {
            View dot = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(24, 24);
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dot.setBackgroundResource(R.drawable.circle); 
            dot.setAlpha(0.3f);
            binding.layoutDots.addView(dot);
        }
    }

    private void updateUI(int index) {
        List<OnboardingStep> steps = viewModel.getSteps();
        OnboardingStep step = steps.get(index);

        binding.tvTitle.setText(step.getTitle());
        binding.tvDescription.setText(step.getDescription());
        binding.imgStep.setImageResource(step.getImageResource());

        updateDots(index);

        // Button morphing logic
        if (index == steps.size() - 1) {
            binding.btnNext.setText("GET STARTED");
            binding.btnNext.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
        } else {
            binding.btnNext.setText("NEXT");
            binding.btnNext.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
        }

        binding.btnBack.setVisibility(index > 0 ? View.VISIBLE : View.GONE);
    }

    private void updateDots(int activeIndex) {
        for (int i = 0; i < binding.layoutDots.getChildCount(); i++) {
            View dot = binding.layoutDots.getChildAt(i);
            dot.animate().alpha(i == activeIndex ? 1.0f : 0.3f).setDuration(300).start();
            // Scale effect for active dot
            dot.animate().scaleX(i == activeIndex ? 1.2f : 1.0f).scaleY(i == activeIndex ? 1.2f : 1.0f).setDuration(300).start();
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
        finish();
    }
}
