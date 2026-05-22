package com.example.mongrammaire.Quiz;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mongrammaire.R;
import com.example.mongrammaire.home;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import com.example.mongrammaire.Utils.ProgressionManager;

public class MainGameActivity extends AppCompatActivity {
    MaterialButton buttonA, buttonB, buttonC, buttonD, btnCheck;
    TextView questionText, timeText;
    LinearProgressIndicator quizProgress;
    TriviaQuizHelper triviaQuizHelper;
    TriviaQuestion currentQuestion;
    List<TriviaQuestion> list;
    int qid = 0;
    int timeValue = 20;
    CountDownTimer countDownTimer;
    MaterialButton selectedButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        
        ProgressionManager.init(this);
        ProgressionManager.updateStreak();

        // Initializing variables
        questionText = findViewById(R.id.triviaQuestion);
        buttonA = findViewById(R.id.buttonA);
        buttonB = findViewById(R.id.buttonB);
        buttonC = findViewById(R.id.buttonC);
        buttonD = findViewById(R.id.buttonD);
        btnCheck = findViewById(R.id.btn_check);
        timeText = findViewById(R.id.timeText);
        quizProgress = findViewById(R.id.quiz_progress);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        // Option Click Listeners
        View.OnClickListener optionListener = v -> {
            if (selectedButton != null) {
                resetButtonStyle(selectedButton);
            }
            selectedButton = (MaterialButton) v;
            highlightButtonStyle(selectedButton);
            btnCheck.setEnabled(true);
        };

        buttonA.setOnClickListener(optionListener);
        buttonB.setOnClickListener(optionListener);
        buttonC.setOnClickListener(optionListener);
        buttonD.setOnClickListener(optionListener);

        // Check Button Listener
        btnCheck.setOnClickListener(v -> checkAnswer());

        // Database
        triviaQuizHelper = new TriviaQuizHelper(this);
        if (triviaQuizHelper.getAllOfTheQuestions().size() == 0) {
            triviaQuizHelper.allQuestion();
        }

        List<TriviaQuestion> allQuestions = triviaQuizHelper.getAllOfTheQuestions();
        list = new ArrayList<>(allQuestions);
        Collections.shuffle(list);

        if (list.size() > 10) {
            list = list.subList(0, 10);
        }

        currentQuestion = list.get(qid);
        updateQueAndOptions();

        // Timer
        countDownTimer = new CountDownTimer(22000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeText.setText(timeValue + "s");
                timeValue -= 1;
                if (timeValue == -1) {
                    showWrongDialog();
                }
            }
            public void onFinish() {}
        }.start();
    }

    private void checkAnswer() {
        if (selectedButton == null) return;
        
        String selectedAnswer = selectedButton.getText().toString();
        if (selectedAnswer.equals(currentQuestion.getAnswer())) {
            updateProgression(true);
            showCorrectDialog();
        } else {
            updateProgression(false);
            showWrongDialog();
        }
    }

    public void updateQueAndOptions() {
        questionText.setText(currentQuestion.getQuestion());
        buttonA.setText(currentQuestion.getOptA());
        buttonB.setText(currentQuestion.getOptB());
        buttonC.setText(currentQuestion.getOptC());
        buttonD.setText(currentQuestion.getOptD());

        resetAllButtonStyles();
        selectedButton = null;
        btnCheck.setEnabled(false);

        timeValue = 20;
        countDownTimer.cancel();
        countDownTimer.start();

        int progress = (int) (((float) (qid) / list.size()) * 100);
        quizProgress.setProgress(progress);
    }

    private void resetAllButtonStyles() {
        resetButtonStyle(buttonA);
        resetButtonStyle(buttonB);
        resetButtonStyle(buttonC);
        resetButtonStyle(buttonD);
    }

    private void resetButtonStyle(MaterialButton btn) {
        btn.setStrokeColor(ContextCompat.getColorStateList(this, R.color.outline));
        btn.setStrokeWidth(Math.round(1 * getResources().getDisplayMetrics().density));
        btn.setBackgroundColor(Color.TRANSPARENT);
        btn.setTextColor(ContextCompat.getColor(this, R.color.onSurface));
    }

    private void highlightButtonStyle(MaterialButton btn) {
        btn.setStrokeColor(ContextCompat.getColorStateList(this, R.color.primary));
        btn.setStrokeWidth(Math.round(2 * getResources().getDisplayMetrics().density));
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryContainer));
    }

    private void updateProgression(boolean correct) {
        if (correct) {
            ProgressionManager.addScore(10);
        }
    }

    private void showCorrectDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_correct);
        styleDialog(dialog);

        dialog.findViewById(R.id.dialogNext).setOnClickListener(v -> {
            dialog.dismiss();
            moveToNextQuestion();
        });
        dialog.show();
    }

    private void showWrongDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_wrong);
        styleDialog(dialog);

        TextView correctAns = dialog.findViewById(R.id.correctAnswerText);
        correctAns.setText(currentQuestion.getAnswer());

        dialog.findViewById(R.id.dialogRetry).setOnClickListener(v -> {
            dialog.dismiss();
            moveToNextQuestion();
        });
        dialog.show();
    }

    private void styleDialog(Dialog dialog) {
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            dialog.getWindow().setAttributes(lp);
        }
        dialog.setCancelable(false);
    }

    private void moveToNextQuestion() {
        if (qid < list.size() - 1) {
            qid++;
            currentQuestion = list.get(qid);
            updateQueAndOptions();
        } else {
            ProgressionManager.unlockNextLevel(ProgressionManager.getUserScore());
            startActivity(new Intent(this, GameWon.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, home.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
