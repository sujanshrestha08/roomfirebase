package com.example.roomfinder.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomfinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText etResetEmail;
    private Button btnResetPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        etResetEmail = findViewById(R.id. etResetEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        mAuth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEmail = etResetEmail.getText().toString();

                if (TextUtils.isEmpty(userEmail))
                {
                    Toast.makeText(ForgetPasswordActivity.this, "Please! Enter your valid Email", Toast.LENGTH_LONG).show();
                }
                else
                {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())

                            {
                                Toast.makeText(ForgetPasswordActivity.this, "Please check your email.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                            }
                            else 

                                {
                                String message =task.getException().getMessage();
                                Toast.makeText(ForgetPasswordActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

    }
}
