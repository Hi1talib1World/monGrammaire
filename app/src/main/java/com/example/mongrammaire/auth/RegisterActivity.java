package com.example.mongrammaire.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mongrammaire.R;
import com.example.mongrammaire.home;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPassword;
    private MaterialButton btnRegister;
    private ProgressBar progressBar;
    private TextView tvLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        try {
            mAuth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            Toast.makeText(this, "Erreur d'initialisation Firebase. Vérifiez google-services.json", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnRegister = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progress_bar);
        tvLogin = findViewById(R.id.tv_login);

        btnRegister.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Nom requis");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email requis");
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Mot de passe (min 6 caractères)");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.INVISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Update display name
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        if (mAuth.getCurrentUser() != null) {
                            mAuth.getCurrentUser().updateProfile(profileUpdates);
                        }

                        progressBar.setVisibility(View.GONE);
                        btnRegister.setVisibility(View.VISIBLE);
                        Toast.makeText(RegisterActivity.this, "Compte créé !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, home.class));
                        finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        btnRegister.setVisibility(View.VISIBLE);
                        Toast.makeText(RegisterActivity.this, "Erreur: " + (task.getException() != null ? task.getException().getMessage() : "Erreur inconnue"),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
