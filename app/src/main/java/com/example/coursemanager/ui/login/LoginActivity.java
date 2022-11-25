package com.example.coursemanager.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        final int[] loginAction = new int[1];
        final int[] registerAction = new int[1];

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

            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // All the below code checks the database and if all the information of the user matches
                // completely with a user from the database
                User user = new User (usernameEditText.getText().toString(), passwordEditText.getText().toString());
                DatabaseReference ref = FirebaseDatabase.getInstance("https://course-manager-b07-default-rtdb.firebaseio.com/").getReference();

                // Check if the user information entered is a student login in the database
                ref.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (usernameEditText.getText().toString().compareTo("") == 0) {
                            //does nothing but prevents app from crashing
                        }
                        if (usernameEditText.getText().toString().compareTo(".") == 0) {
                            int length = usernameEditText.getText().toString().length();

                        }
                        //if the email is correct, continue with the checks, otherwise, display msg
                        else if (snapshot.hasChild(usernameEditText.getText().toString())) {
                            ref.child("students").child(usernameEditText.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {

                                    DataSnapshot ds = task.getResult();
                                    User login = ds.getValue(User.class);
                                    if(user.password.compareTo(login.getPassword()) == 0){
                                        // Checks if the password entered matches the password of the given email.
                                        // If it does, bring them to the student landing page
                                        loginAction[0] = 3;
                                        registerAction[0] = 1;

                                    }
                                    else{
                                        //wrong password msg
                                        loginAction[0] = 2;
                                        registerAction[0] = 1;

                                    }
                                }
                            });
                        }
                        else {
                            loginAction[0] = 1;
                            registerAction[0] = 2;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                // Check if the user information entered is an admin login in the database
                ref.child("admins").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (usernameEditText.getText().toString().compareTo("") == 0) {
                            //does nothing but prevents app from crashing
                        }
                        //if the email is correct, continue with the checks, otherwise, display msg
                        else if (snapshot.hasChild(usernameEditText.getText().toString())) {
                            ref.child("admins").child(usernameEditText.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {

                                    DataSnapshot ds = task.getResult();
                                    User login = ds.getValue(User.class);
                                    if(user.password.compareTo(login.getPassword()) == 0){

                                        // Checks if the password entered matches the password of the given email.
                                        // If it does, bring them to the admin landing page
                                        loginAction[0] = 4;
                                    }
                                    else {
                                        //wrong password msg
                                        loginAction[0] = 2;
                                    }
                                }
                            });
                        }
                        else {
                            loginAction[0] = 1;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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

                if (loginAction[0] == 1) {
                    String warningMsg = "This username is not associated \nwith an account";
                    Toast.makeText(getApplicationContext(), warningMsg, Toast.LENGTH_LONG).show();
                }
                else if (loginAction[0] == 2) {
                    String warningMsg = "The password entered is incorrect";
                    Toast.makeText(getApplicationContext(), warningMsg, Toast.LENGTH_LONG).show();
                }
                else if (loginAction[0] == 3) {
                    finish();
                    //Setting up passing username to next screen
                    Intent passer = new Intent(LoginActivity.this, MainActivityStudent.class);
                    passer.putExtra("username", usernameEditText.getText().toString());
                    startActivity(passer);
                }
                else if (loginAction[0] ==4 ) {
                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivityAdmin.class));
                }



            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

                //email error
                if (registerAction[0] == 1) {
                    String warningMsg = "This username is already \nassociated with an account";
                    Toast.makeText(getApplicationContext(), warningMsg, Toast.LENGTH_LONG).show();
                }
                //new acc
                else if (registerAction[0] == 2) {
                    User user = new User (usernameEditText.getText().toString(), passwordEditText.getText().toString());
                    DatabaseReference ref = FirebaseDatabase.getInstance("https://course-manager-b07-default-rtdb.firebaseio.com/").getReference();
                    ref.child("students").child(usernameEditText.getText().toString()).setValue(user);
                    startActivity(new Intent(LoginActivity.this, MainActivityStudent.class));
                }
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}