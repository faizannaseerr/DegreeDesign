package com.example.coursemanager.ui.login;

import android.app.Activity;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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

import com.example.coursemanager.MainActivity;
import com.example.coursemanager.R;
import com.example.coursemanager.ui.login.LoginViewModel;
import com.example.coursemanager.ui.login.LoginViewModelFactory;
import com.example.coursemanager.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button registerButton = binding.register;
        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
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

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
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

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());


                // Adds functionality to the "Login or Sign Up" button to open a new "MainActivity"
                // We probably want to add a check here to see if an admin is logging in and if one is we can
                // create different "AdminActivity" to open in such case. Otherwise MainActivity can be
                // opened if a student is logging in/signing up. First fragment in MainActivity can be
                // a menu for students to view courses, create schedule, view schedule, etc.
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());


                //I've gone ahead and added a register button, since we'll most likely be separating
                //the buttons anyways.
                //This button will write to the database, while the other will read from it.
                //It still says login for now, but we plan on changing that.
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}