package com.example.smartalert;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.smartalert.users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;

    private boolean isLogin = false;

    private TextView loginTextView;
    private EditText email;
    private EditText username;
    private EditText password;
    private Button loginButton;
    private TextView signupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginTextView = findViewById(R.id.loginTextView);
        email = findViewById(R.id.emailEditText);
        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupTextView = findViewById(R.id.signupTextView);

        isLogin = false;

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = getInstance();
        reference = database.getReference("users");

        Log.d("AEK", "Main");

        LocaleHelper.checkLocale(this);

        changeLoginSignUp();

        checkIfThereIsUser();
    }

    private void checkIfThereIsUser() {
        if (user == null)
            return;

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("username", user.getDisplayName());
        startActivity(intent);
        finish();
    }

    public void onButtonClick(View view) {
        if (isLogin)
            onLogin(view);
        else
            onRegister(view);

        checkIfThereIsUser();
    }

    private void onLogin(View view) {
        if(!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
            auth.signInWithEmailAndPassword(email.getText().toString(),
                    password.getText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    user = auth.getCurrentUser();
                    checkIfThereIsUser();
                }else {
                    showMessage("Error", Objects.requireNonNull(task.getException()).getLocalizedMessage());
                }
            });
        }
    }

    private void onRegister(View view) {
        if(!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !username.getText().toString().isEmpty()) {
            auth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            user = auth.getCurrentUser();
                            updateUser(user, username.getText().toString());

                            reference.push().setValue(
                                    new User(
                                            user.getUid(),
                                            username.getText().toString(),
                                            email.getText().toString()
                                    )
                            );

                            checkIfThereIsUser();
                        }else {
                            showMessage("Error", Objects.requireNonNull(task.getException()).getLocalizedMessage());
                        }
                    });
        }else {
            showMessage("Error","Please provide all info!");
        }
    }

    private void updateUser(FirebaseUser user, String nickname){
        if (user == null)
            return;

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(nickname)
                .build();
        user.updateProfile(request);
    }

    public void onChangeLoginSignUp(View view) {
        changeLoginSignUp();
    }

    private void changeLoginSignUp() {
        if (isLogin) {
            isLogin = false;
            username.setVisibility(View.VISIBLE);
            username.setActivated(true);
            loginTextView.setText(R.string.sign_up);
            loginButton.setText(R.string.sign_up);
            signupTextView.setText(R.string.already_have_an_account_login_now);
            return;
        }

        isLogin = true;
        loginTextView.setText(R.string.login);
        username.setVisibility(View.GONE);
        username.setActivated(false);
        loginTextView.setText(R.string.login);
        loginButton.setText(R.string.login);
        signupTextView.setText(R.string.not_yet_registered_signup_now);
    }

    void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}