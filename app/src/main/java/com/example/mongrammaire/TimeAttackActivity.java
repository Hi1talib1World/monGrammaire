package com.example.mongrammaire;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mongrammaire.Quiz.TriviaQuestion;
import com.example.mongrammaire.Quiz.TriviaQuizHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.orhanobut.hawk.Hawk;

import java.util.Collections;
import java.util.List;

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

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        TriviaQuizHelper db = new TriviaQuizHelper(this);
        questions = db.getAllOfTheQuestions();
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
                tvTimer.setText(seconds + "s");
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
        boolean isCorrect = selected.equals(currentQuestion.getAnswer());
        int flashColor = isCorrect ? 0x8800FF00 : 0x88FF0000;
        
        questionCard.setCardBackgroundColor(flashColor);
        if (isCorrect) {
            score += 10;
            correctAnswers++;
        } else {
            score = Math.max(0, score - 5);
        }
        tvScore.setText("Score: " + score);

        new Handler().postDelayed(() -> {
            questionCard.setCardBackgroundColor(Color.WHITE);
            loadNextQuestion();
        }, 200);
    }

    private void showEndGameDialog() {
        int highScore = Hawk.get("time_attack_high", 0);
        StringBuilder message = new StringBuilder();
        message.append("Score : ").append(score).append(" XP\n");
        message.append("Précision : ").append(correctAnswers).append(" bonnes réponses");
        
        if (score > highScore) {
            Hawk.put("time_attack_high", score);
            message.append("\n\nNOUVEAU RECORD ! 🏆");
        }

        new MaterialAlertDialogBuilder(this)
            .setTitle("Temps écoulé !")
            .setMessage(message.toString())
            .setCancelable(false)
            .setPositiveButton("Rejouer", (d, w) -> recreate())
            .setNegativeButton("Quitter", (d, w) -> finish())
            .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}
