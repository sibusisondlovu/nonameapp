package co.za.jaspasystems.nonameapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import co.za.jaspasystems.nonameapp.R;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private TextView tvMessage;
    private ProgressBar progressBar;
    private Button btnLogin;
    private LinearLayout llProgress;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        initViews();
    }

    private void initViews() {

        etUsername = findViewById(R.id.activity_login_etUsername);
        etPassword = findViewById(R.id.activity_login_etPassword);
        tvMessage = findViewById(R.id.activity_login_etMessage);
        progressBar = findViewById(R.id.activity_login_progressBar);
        btnLogin = findViewById(R.id.activity_login_btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputs();
            }
        });
        llProgress = findViewById(R.id.activity_login_llProgress);
    }

    private void checkInputs() {

        if (TextUtils.isEmpty(etUsername.getText())) {
            etUsername.setError("Username is required");
            return;
        }

        if (TextUtils.isEmpty(etPassword.getText())) {
            etPassword.setError("Password is required");
            return;
        }

        loginUser();
    }

    private void loginUser(){
        btnLogin.setEnabled(false);
        llProgress.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(
                etUsername.getText().toString().trim() + "@nonameapp.co.za",
                etPassword.getText().toString().trim())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        tvMessage.setText(e.getMessage());
                        llProgress.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        tvMessage.setVisibility(View.VISIBLE);
                        btnLogin.setEnabled(true);
                    }
                });
    }
}