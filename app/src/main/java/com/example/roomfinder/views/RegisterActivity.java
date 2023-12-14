package com.example.roomfinder.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomfinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPass, etRePass;
    private Button btnRegister;
    private FirebaseAuth auth;
    private TextView tvLogin;
    private KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvLogin = findViewById(R.id.tvLogin);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etRePass = findViewById(R.id.etRePass);

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        btnRegister = findViewById(R.id.btnRegister);

        tvLogin.setOnClickListener(view -> finish());

        auth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(v -> {
            String txt_username = etName.getText().toString();
            String txt_password = etPass.getText().toString();
            String txt_cpassword = etRePass.getText().toString();
            String txt_email = etEmail.getText().toString();

            if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_cpassword) || TextUtils.isEmpty(txt_email)) {
                Toast.makeText(RegisterActivity.this, "Fill All Fields", Toast.LENGTH_SHORT).show();
            } else if (txt_password.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else if (!txt_password.equals(txt_cpassword)) {
                Toast.makeText(RegisterActivity.this, "Password does't match", Toast.LENGTH_SHORT).show();
            } else {
                register(txt_username, txt_email, txt_password);
            }
        });
    }

    private void register(final String username, String email, String password) {


        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null;
                        String userId = firebaseUser.getUid();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userId);
                        hashMap.put("username", username);
                        hashMap.put("imageURL", "default");

                        reference.setValue(hashMap).addOnCompleteListener(task1 -> {

                            if (task1.isSuccessful()) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                        });
                    } else {

                    }
                });

    }
}
