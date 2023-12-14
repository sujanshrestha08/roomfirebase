package com.example.roomfinder.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomfinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.kaopiz.kprogresshud.KProgressHUD;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText etEmail, etPass;
    private CheckBox cbRemember;
    private TextView tvForget;
    private Button btnLogin;
    private TextView btnRegister;
    private KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        cbRemember = findViewById(R.id.cbRemember);
        tvForget = findViewById(R.id.tvForget);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        auth = FirebaseAuth.getInstance();

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        btnLogin.setOnClickListener(v -> {
            String txt_email = etEmail.getText().toString();
            String txt_password = etPass.getText().toString();

            if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                hud.show();
                login(txt_email, txt_password);
            }
        });

        btnRegister.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        tvForget.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class)));

    }


    private void login(final String txt_email, String txt_password) {
        auth.signInWithEmailAndPassword(txt_email, txt_password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                        hud.dismiss();
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    } else {
                        hud.dismiss();
                        Toast.makeText(LoginActivity.this, "Email or Password incorrect", Toast.LENGTH_SHORT).show();
                    }
                });


    }

}
