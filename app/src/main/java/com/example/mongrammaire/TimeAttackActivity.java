package com.example.mongrammaire;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;

import com.example.mongrammaire.Quiz.TriviaQuestion;
import com.example.mongrammaire.Quiz.TriviaQuizHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.orhanobut.hawk.Hawk;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TimeAttackActivity extends AppCompatActivity {

    private TextView tvTimer, tvScore, tvQuestion;
    private MaterialCardView timerCard, questionCard;
    private MaterialButton btnOpt1, btnOpt2, btnOpt3, btnOpt4;
    
    private List<TriviaQuestion> questions;
    private TriviaQuestion currentQuestion;
    private int score = 0;
    private int correctAnswers = 0;
    private int questionIndex = 0;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_attack);

        tvTimer = findViewById(R.id.tvTimer);
        tvScore = findViewById(R.id.tvScore);
        tvQuestion = findViewById(R.id.tvQuestion);
        timerCard = findViewById(R.id.timerCard);
        questionCard = findViewById(R.id.questionCard);
        btnOpt1 = findViewById(R.id.btnOpt1);
        btnOpt2 = findViewById(R.id.btnOpt2);
        btnOpt3 = findViewById(R.id.btnOpt3);
        btnOpt4 = findViewById(R.id.btnOpt4);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
        findViewById(R.id.btn_back).setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        try (TriviaQuizHelper db = new TriviaQuizHelper(this)) {
            questions = db.getAllOfTheQuestions();
        }
        Collections.shuffle(questions);

        loadNextQuestion();
        startTimeAttack();

        MaterialButton[] buttons = {btnOpt1, btnOpt2, btnOpt3, btnOpt4};
        for (MaterialButton btn : buttons) {
            btn.setOnClickListener(v -> checkAnswer(btn.getText().toString()));
        }
    }

    private void startTimeAttack() {
        timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                tvTimer.setText(getString(R.string.time_format, seconds));
                if (seconds <= 10) {
                    timerCard.setStrokeColor(Color.RED);
                    tvTimer.setTextColor(Color.RED);
                }
            }
            public void onFinish() {
                showEndGameDialog();
            }
        }.start();
    }

    private void loadNextQuestion() {
        if (questions == null || questions.isEmpty()) return;
        
        if (questionIndex >= questions.size()) {
            Collections.shuffle(questions);
            questionIndex = 0;
        }
        currentQuestion = questions.get(questionIndex++);
        tvQuestion.setText(currentQuestion.getQuestion());
        btnOpt1.setText(currentQuestion.getOptA());
        btnOpt2.setText(currentQuestion.getOptB());
        btnOpt3.setText(currentQuestion.getOptC());
        btnOpt4.setText(currentQuestion.getOptD());
    }

    private void checkAnswer(String selected) {
        boolean isCorrect = Objects.equals(selected, currentQuestion.getAnswer());
        int flashColor = isCorrect ? 0x8800FF00 : 0x88FF0000;
        
        questionCard.setCardBackgroundColor(flashColor);
        if (isCorrect) {
            score += 10;
            correctAnswers++;
        } else {
            score = Math.max(0, score - 5);
        }
        tvScore.setText(getString(R.string.score_format, score));

        new Handler().postDelayed(() -> {
            questionCard.setCardBackgroundColor(Color.WHITE);
            loadNextQuestion();
        }, 200);
    }

    private void showEndGameDialog() {
        int highScore = Hawk.get("time_attack_high", 0);
        StringBuilder message = new StringBuilder();
        message.append(getString(R.string.xp_format, score)).append("\n");
        message.append(getString(R.string.accuracy_correct_format, correctAnswers));
        
        if (score > highScore) {
            Hawk.put("time_attack_high", score);
            message.append("\n\n").append(getString(R.string.new_high_score));
        }

        new MaterialAlertDialogBuilder(this)
            .setTitle(R.string.time_attack_end_title)
            .setMessage(message.toString())
            .setCancelable(false)
            .setPositiveButton(R.string.play_again, (d, w) -> recreate())
            .setNegativeButton(R.string.quit, (d, w) -> finish())
            .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}
