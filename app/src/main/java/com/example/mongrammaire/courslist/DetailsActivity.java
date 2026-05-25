package com.example.mongrammaire.courslist;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mongrammaire.R;
import com.example.mongrammaire.Utils.ToastHelper;
import com.example.mongrammaire.databinding.ActivityDetailsBinding;
import com.example.mongrammaire.databinding.ItemLearningStepBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ActivityDetailsBinding binding;
    private final List<LearningStep> steps = new ArrayList<>();
    private TextToSpeech tts;
    private boolean isTtsReady = false;

    enum StepType { RULE, EXAMPLE, EXCEPTION, FINISH }

    static class LearningStep {
        StepType type;
        String content;
        String revealContent;

        LearningStep(StepType type, String content) {
            this.type = type;
            this.content = content;
        }

        LearningStep(StepType type, String content, String revealContent) {
            this.type = type;
            this.content = content;
            this.revealContent = revealContent;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize TTS
        tts = new TextToSpeech(this, this);

        Intent intent = getIntent();
        String rawContent = intent.getStringExtra("iContent");
        if (rawContent == null || rawContent.isEmpty()) {
            rawContent = intent.getStringExtra("iDescTv");
        }

        parseContent(rawContent);
        
        if (steps.isEmpty()) {
            ToastHelper.showCustomToast(this, "Leçon vide");
            finish();
            return;
        }

        // Add a final "Completion" step
        steps.add(new LearningStep(StepType.FINISH, "Félicitations ! Vous avez terminé cette leçon. Prêt pour le quiz ?"));

        setupViewPager();

        binding.btnContinue.setOnClickListener(v -> {
            int nextItem = binding.cardViewPager.getCurrentItem() + 1;
            if (nextItem < steps.size()) {
                binding.cardViewPager.setCurrentItem(nextItem, true);
            } else {
                startActivity(new Intent(this, com.example.mongrammaire.Quiz.MainGameActivity.class));
                finish();
            }
        });

        binding.btnBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.FRENCH);
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                isTtsReady = true;
            }
        }
    }

    private void parseContent(String rawText) {
        if (rawText == null || rawText.isEmpty()) return;

        Pattern pattern = Pattern.compile("\\[(RULE|EXAMPLE|EXCEPTION)]");
        Matcher matcher = pattern.matcher(rawText);

        List<String> foundTags = new ArrayList<>();
        List<Integer> startIndices = new ArrayList<>();

        while (matcher.find()) {
            foundTags.add(matcher.group(0));
            startIndices.add(matcher.start());
        }

        if (startIndices.isEmpty()) {
            steps.add(new LearningStep(StepType.RULE, rawText));
            return;
        }

        for (int i = 0; i < startIndices.size(); i++) {
            int start = startIndices.get(i) + foundTags.get(i).length();
            int end = (i + 1 < startIndices.size()) ? startIndices.get(i + 1) : rawText.length();
            
            String content = rawText.substring(start, end).trim();
            StepType type = StepType.RULE;
            String reveal = null;

            if (foundTags.get(i).equals("[EXAMPLE]")) {
                type = StepType.EXAMPLE;
                // Check if there is a reveal pattern like "hint -> [reveal]"
                if (content.contains("-> [")) {
                    int rStart = content.indexOf("-> [");
                    int rEnd = content.indexOf("]", rStart);
                    if (rEnd != -1) {
                        reveal = content.substring(rStart + 4, rEnd);
                        content = content.substring(0, rStart).trim();
                    }
                }
            } else if (foundTags.get(i).equals("[EXCEPTION]")) {
                type = StepType.EXCEPTION;
            }
            
            steps.add(new LearningStep(type, content, reveal));
        }
    }

    private void setupViewPager() {
        LessonCardAdapter adapter = new LessonCardAdapter(steps, text -> {
            if (isTtsReady) tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        });
        binding.cardViewPager.setAdapter(adapter);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(20));
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
            page.setAlpha(0.5f + r * 0.5f);
        });
        binding.cardViewPager.setPageTransformer(transformer);

        binding.cardViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                int progress = ((position + 1) * 100) / steps.size();
                binding.learningProgress.setProgress(progress, true);

                if (position == steps.size() - 1) {
                    binding.btnContinue.setText(R.string.quiz);
                } else {
                    binding.btnContinue.setText("CONTINUER");
                }
            }
        });
    }

    interface OnListenListener {
        void onListen(String text);
    }

    static class LessonCardAdapter extends RecyclerView.Adapter<LessonCardAdapter.CardViewHolder> {
        private final List<LearningStep> steps;
        private final OnListenListener listenListener;

        LessonCardAdapter(List<LearningStep> steps, OnListenListener listener) {
            this.steps = steps;
            this.listenListener = listener;
        }

        @NonNull
        @Override
        public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemLearningStepBinding itemBinding = ItemLearningStepBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new CardViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
            LearningStep step = steps.get(position);
            
            String title = "La Règle";
            int iconRes = R.drawable.bookstack;
            int cardColor = R.color.white;
            int strokeColor = R.color.surfaceVariant;

            switch (step.type) {
                case EXAMPLE:
                    title = "Exemple";
                    iconRes = R.drawable.ic_play;
                    cardColor = R.color.bg01;
                    break;
                case EXCEPTION:
                    title = "Attention !";
                    iconRes = R.drawable.star;
                    strokeColor = R.color.red;
                    break;
                case FINISH:
                    title = "Terminé !";
                    iconRes = R.drawable.ic_check_circle;
                    cardColor = R.color.green2;
                    break;
            }

            holder.binding.tvStepTitle.setText(title);
            holder.binding.ivStepIcon.setImageResource(iconRes);
            holder.binding.cardContainer.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), cardColor));
            holder.binding.cardContainer.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.getContext(), strokeColor)));

            String formattedContent = step.content
                    .replace("->", " ➔ ")
                    .replace("\n", "<br/>")
                    .replace("Ex:", "<b>Exemple :</b>");

            holder.binding.tvStepContent.setText(HtmlCompat.fromHtml(formattedContent, HtmlCompat.FROM_HTML_MODE_LEGACY));

            // Reveal logic
            if (step.revealContent != null) {
                holder.binding.revealCard.setVisibility(View.VISIBLE);
                holder.binding.tvRevealHint.setText("Appuyez pour voir la réponse");
                holder.binding.revealCard.setOnClickListener(v -> {
                    holder.binding.tvRevealHint.setText(step.revealContent);
                    holder.binding.revealCard.setCardBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.primaryContainer));
                });
            } else {
                holder.binding.revealCard.setVisibility(View.GONE);
            }

            holder.binding.btnListen.setOnClickListener(v -> {
                String textToRead = step.content;
                if (step.revealContent != null) textToRead += ". " + step.revealContent;
                listenListener.onListen(textToRead);
            });
        }

        @Override
        public int getItemCount() {
            return steps.size();
        }

        static class CardViewHolder extends RecyclerView.ViewHolder {
            ItemLearningStepBinding binding;
            CardViewHolder(ItemLearningStepBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }
}
