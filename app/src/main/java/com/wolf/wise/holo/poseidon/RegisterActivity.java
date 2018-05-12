package com.wolf.wise.holo.poseidon;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnRegister)
    public void registerClick() {
        if (!isFormValid()) {
            return;
        }

        showProgressDialog();

        firebaseAuth.createUserWithEmailAndPassword(
                etEmail.getText().toString(), etPassword.getText().toString()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();

                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    firebaseUser.updateProfile(
                            new UserProfileChangeRequest.Builder().
                                    setDisplayName(etUsername.getText().toString()).build()
                    );

                    Toast.makeText(RegisterActivity.this, "REG OK",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, getString(R.string.register_failure)+
                                    task.getException().getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                Toast.makeText(RegisterActivity.this,
                        "error: "+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isFormValid() {
        if (TextUtils.isEmpty(etUsername.getText().toString())) {
            etUsername.setError(getString(R.string.required_error));
            return false;
        }

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
