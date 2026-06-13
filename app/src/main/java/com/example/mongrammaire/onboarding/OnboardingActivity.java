package com.example.mongrammaire.onboarding;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.mongrammaire.R;
import com.example.mongrammaire.databinding.ActivityOnboardingBinding;
import com.example.mongrammaire.home;
import java.util.List;

/**
 * High-Fidelity Onboarding Controller
 * Implements Design Pillar 3: Micro-Feedback & Interactive Animations
 */
public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private OnboardingViewModel viewModel;
    private static final int ANIM_DURATION = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(OnboardingViewModel.class);

        initializeUI();
        setupObservers();
        setupListeners();
    }

    private void initializeUI() {
        // Prepare initial state
        binding.cardContent.setAlpha(0f);
        binding.cardContent.setTranslationY(100f);
        binding.cardContent.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
        
        setupDots(viewModel.getStepsCount());
    }

    private void setupObservers() {
        viewModel.getCurrentStepIndex().observe(this, this::transitionToStep);
        
        viewModel.getIsLoadingTransition().observe(this, loading -> {
            // Guarding inputs during transitions
            binding.btnNext.setClickable(!loading);
            binding.btnSkip.setClickable(!loading);
            binding.btnBack.setClickable(!loading);
            
            // Visual feedback for loading/locked state
            float targetAlpha = loading ? 0.5f : 1.0f;
            binding.cardContent.animate().alpha(targetAlpha).setDuration(200).start();
        });

        viewModel.getOnboardingFinished().observe(this, finished -> {
            if (finished) navigateToMain();
        });
    }

    private void setupListeners() {
        binding.btnNext.setOnClickListener(v -> viewModel.nextStep());
        binding.btnSkip.setOnClickListener(v -> viewModel.completeOnboarding());
        binding.btnBack.setOnClickListener(v -> viewModel.previousStep());
    }

    private void transitionToStep(int index) {
        List<OnboardingStep> steps = viewModel.getSteps();
        OnboardingStep step = steps.get(index);

        // Content Cross-fade Micro-interaction
        binding.cardContent.animate()
                .alpha(0f)
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(ANIM_DURATION / 2)
                .withEndAction(() -> {
                    binding.tvTitle.setText(step.getTitle());
                    binding.tvDescription.setText(step.getDescription());
                    binding.imgStep.setImageResource(step.getImageResource());
                    
                    updateDots(index);
                    updateActionButtons(index, steps.size());

                    binding.cardContent.animate()
                            .alpha(1f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(ANIM_DURATION / 2)
                            .start();
                }).start();
    }

    private void updateActionButtons(int index, int total) {
        boolean isLast = index == total - 1;
        
        // Button Morphing Contract
        binding.btnNext.setText(isLast ? "GET STARTED" : "NEXT");
        
        ColorStateList nextColor = ColorStateList.valueOf(ContextCompat.getColor(this, 
                isLast ? R.color.accent_green : R.color.primary));
        binding.btnNext.setBackgroundTintList(nextColor);

        // Visibility with animation
        if (index > 0) {
            if (binding.btnBack.getVisibility() != View.VISIBLE) {
                binding.btnBack.setVisibility(View.VISIBLE);
                binding.btnBack.setAlpha(0f);
                binding.btnBack.animate().alpha(1f).setDuration(300).start();
            }
        } else {
            binding.btnBack.animate().alpha(0f).setDuration(300).withEndAction(() -> 
                    binding.btnBack.setVisibility(View.GONE)).start();
        }
    }

    private void setupDots(int count) {
        binding.layoutDots.removeAllViews();
        for (int i = 0; i < count; i++) {
            View dot = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.dot_size_inactive),
                    getResources().getDimensionPixelSize(R.dimen.dot_size_inactive)
            );
            params.setMargins(12, 0, 12, 0);
            dot.setLayoutParams(params);
            dot.setBackgroundResource(R.drawable.circle); 
            dot.setAlpha(0.3f);
            binding.layoutDots.addView(dot);
        }
    }

    private void updateDots(int activeIndex) {
        for (int i = 0; i < binding.layoutDots.getChildCount(); i++) {
            View dot = binding.layoutDots.getChildAt(i);
            boolean isActive = (i == activeIndex);
            
            float targetAlpha = isActive ? 1.0f : 0.3f;
            float targetScale = isActive ? 1.4f : 1.0f;
            
            dot.animate()
                    .alpha(targetAlpha)
                    .scaleX(targetScale)
                    .scaleY(targetScale)
                    .setDuration(ANIM_DURATION)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
