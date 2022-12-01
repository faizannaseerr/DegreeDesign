package com.example.coursemanager.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursemanager.data.LoginRepository;
import com.example.coursemanager.data.Result;
import com.example.coursemanager.data.model.LoggedInUser;
import com.example.coursemanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

// ***
// *** This is the Presenter component of the login module
// ***

public class LoginPresenter extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;
    private final int[] loginAction = new int[1];
    private final int[] registerAction = new int[1];
    private LoginActivity loginActivity;

    LoginPresenter(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void setLoginActivity(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    // Check username in database for students
    public void checkStudentInDB(String username, String pass) {
        LoginModel loginModel = new LoginModel(username, pass);

        loginModel.ref.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (username.compareTo("") == 0) {
                    //does nothing but prevents app from crashing
                }
                //if the email is correct, continue with the checks, otherwise, display msg
                else if (snapshot.hasChild(username)) {
                    loginModel.ref.child("students").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {

                            DataSnapshot ds = task.getResult();
                            User login = ds.getValue(User.class);
                            if(loginModel.checkPassword(login.getPassword())){
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

    }

    // Check username in database for admins
    public void checkAdminInDB(String username, String pass) {
        LoginModel loginModel = new LoginModel(username, pass);

        loginModel.ref.child("admins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (username.compareTo("") == 0) {
                    //does nothing but prevents app from crashing
                }
                //if the email is correct, continue with the checks, otherwise, display msg
                else if (snapshot.hasChild(username)) {
                    loginModel.ref.child("admins").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {

                            DataSnapshot ds = task.getResult();
                            User login = ds.getValue(User.class);
                            if(loginModel.checkPassword(login.getPassword())){

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

    public void loginButtonAction(String username) {
        if (loginAction[0] == 1) {
            loginActivity.displayToastMsg("This username is not associated \nwith an account");
        }
        else if (loginAction[0] == 2) {
            loginActivity.displayToastMsg("The password entered is incorrect");
        }
        else if (loginAction[0] == 3) {
            loginActivity.completeActivity(username, true);
        }
        else if (loginAction[0] ==4 ) {
            loginActivity.completeActivity(username, false);
        }
    }

    public void registerButtonAction(String username, String pass) {
        //email error
        if (registerAction[0] == 1) {
            loginActivity.displayToastMsg("This username is already \nassociated with an account");
        }
        //new acc
        else if (registerAction[0] == 2) {
            LoginModel loginModel = new LoginModel(username, pass);
            loginModel.createNewAccount();

            //fix for bugs where new student accounts don't display name and crash when adding courses
            loginActivity.completeActivity(username, true);
        }
    }

}