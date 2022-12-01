package com.example.coursemanager.ui.login;

import android.app.Activity;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursemanager.MainActivityAdmin;
import com.example.coursemanager.MainActivityStudent;
import com.example.coursemanager.databinding.ActivityLoginBinding;

// ***
// *** This is the View component of the login module
// ***

public class LoginActivity extends AppCompatActivity {

    private LoginPresenter loginPresenter;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginPresenter = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginPresenter.class);
        loginPresenter.setLoginActivity(LoginActivity.this);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button registerButton = binding.register;
        final ProgressBar loadingProgressBar = binding.loading;

        loginPresenter.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginButton.isEnabled()) {
                    Drawable buttonDrawable = loginButton.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, Color.rgb(243, 204, 85));
                    loginButton.setBackground(buttonDrawable);
                    loginButton.setTextColor(Color.rgb(0, 0, 0));
                }
                registerButton.setEnabled(loginFormState.isDataValid());
                if (registerButton.isEnabled()) {
                    Drawable buttonDrawable = registerButton.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, Color.rgb(243, 204, 85));
                    registerButton.setBackground(buttonDrawable);
                    registerButton.setTextColor(Color.rgb(0, 0, 0));
                }
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginPresenter.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // You can no longer type "."
                // This prevents a crash
                if (usernameEditText.getText().toString().contains(".")){
                    usernameEditText.setText(usernameEditText.getText().toString().replaceAll("[.]", ""));
                }

                // Check if the user information entered is a student login in the database
                loginPresenter.checkStudentInDB(usernameEditText.getText().toString(), passwordEditText.getText().toString());

                // Check if the user information entered is an admin login in the database
                loginPresenter.checkAdminInDB(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginPresenter.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginPresenter.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginPresenter.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

                //upon press login Presenter will decide what the login button does
                loginPresenter.loginButtonAction(usernameEditText.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginPresenter.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

                loginPresenter.registerButtonAction(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
    }

    public void displayToastMsg(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }

    public void completeActivity(String username, boolean student) {
        finish();
        Intent passer;
        if (student) {
            passer = new Intent(LoginActivity.this, MainActivityStudent.class);
        } else {
            passer = new Intent(LoginActivity.this, MainActivityAdmin.class);
        }
        passer.putExtra("username", username);
        startActivity(passer);
    }


    private void updateUiWithUser(LoggedInUserView model) {
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}