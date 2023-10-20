package com.example.login_signup_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, editTextConfirmPassword;
    Button registerButton;
    FirebaseAuth mAuth;
    ProgressBar progressBar, passwordStrengthProgressBar;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.registerButton);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        passwordStrengthProgressBar = findViewById(R.id.passwordStrengthProgressBar);
        textView = findViewById(R.id.loginNow);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int passwordStrength = calculatePasswordStrength(s.toString());
                passwordStrengthProgressBar.setProgress(passwordStrength);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, confirmPassword;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                confirmPassword = String.valueOf(editTextConfirmPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Please enter email", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(Register.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

//                if (!isStrongPassword(password)) {
//                    Toast.makeText(Register.this, "Password should contain at least 8 characters including uppercase, lowercase letters, and at least one symbol.", Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                    return;
//                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(Register.this, "Account created.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

//            private boolean isStrongPassword(String password) {
//                // Add your password strength criteria here.
//                // This is a basic example; customize it based on your requirements.
//                return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
//            }

            private int calculatePasswordStrength(String password){
                int strength = 0;

                // Check for at least one uppercase letter
                if (password.matches(".*[A-Z].*")) {
                    strength += 25;
                }

                // Check for at least one lowercase letter
                if (password.matches(".*[a-z].*")) {
                    strength += 25;
                }

                // Check for at least one symbol (non-alphanumeric character)
                if (password.matches(".*[^a-zA-Z0-9].*")) {
                    strength += 25;
                }

                // Check for at least one number
                if (password.matches(".*[0-9].*")) {
                    strength += 25;
                }

                return strength;
            }
        });
    }

    private int calculatePasswordStrength(String password) {
        int strength = 0;

        // Check for at least one uppercase letter
        if (password.matches(".*[A-Z].*")) {
            strength += 25;
        }

        // Check for at least one lowercase letter
        if (password.matches(".*[a-z].*")) {
            strength += 25;
        }

        // Check for at least one symbol (non-alphanumeric character)
        if (password.matches(".*[^a-zA-Z0-9].*")) {
            strength += 25;
        }

        // Check for at least one number
        if (password.matches(".*[0-9].*")) {
            strength += 25;
        }
        return strength;
    }
}