package com.example.vubook.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vubook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    TextView sign_in;
    Button sign_up;
    TextInputLayout layout_email,layout_password;
    TextInputEditText et_email,et_password;
    ProgressBar progressBar;

    LinearLayout linearLayout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        layout_email = findViewById(R.id.layout_email);
        layout_password = findViewById(R.id.layout_password);

        sign_in = findViewById(R.id.textView_sign_in);
        sign_up = findViewById(R.id.btn_sign_up);

        et_email = findViewById(R.id.email_sign_up);
        et_password = findViewById(R.id.password_sign_up);

        linearLayout = findViewById(R.id.linearLayout);
        progressBar = findViewById(R.id.sign_up_progressbar);

        progressBar.setVisibility(View.GONE);
        sign_up.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_in = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(sign_in);
                finish();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                if (email.isEmpty()) {
                    layout_email.setError("E-mail is required");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    layout_email.setError("Please enter a valid email");
                    return;
                }

                if (password.isEmpty()) {
                    layout_password.setError("Password is required");
                    return;
                }

                if (password.length() < 6) {
                    layout_password.setError("Minimum length of password should be 6");
                    return;
                }
                else {
                    sign_up.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        sign_up.setVisibility(View.VISIBLE);
                                        linearLayout.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);

                                        FirebaseAuth.getInstance().signOut();

                                        Intent sign_in = new Intent(SignupActivity.this, ConfirmActivity.class);
                                        startActivity(sign_in);
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                    }
                                }
                            });
                }
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
