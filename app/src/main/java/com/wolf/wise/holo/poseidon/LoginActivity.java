package com.wolf.wise.holo.poseidon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnLogin)
    void loginClick() {
        if (!isFormValid()) {
            return;
        }

        showProgressDialog();

        firebaseAuth.signInWithEmailAndPassword(
                etEmail.getText().toString(),
                etPassword.getText().toString()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                hideProgressDialog();

                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this,
                            StoreActivity.class));

                } else {
                    Toast.makeText(LoginActivity.this,
                            task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.btnRegister)
    void registerClick(){
        startActivity(new Intent(LoginActivity.this,
                RegisterActivity.class));
    }

    private boolean isFormValid() {
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError(getString(R.string.required_error));
            return false;
        }

        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError(getString(R.string.required_error));
            return false;
        }

        return true;
    }
}
