package com.example.acme_explorer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.acme_explorer.entity.Trip;
import com.example.acme_explorer.services.FirebaseDatabaseService;
import com.example.acme_explorer.services.FirestoreService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0x120;
    private FirebaseAuth mAuth;
    private Button singinButtonGoogle;
    private Button singinButtonMail;
    private Button loginButtonSingup;
    private ProgressBar progressBar;
    private AutoCompleteTextView loginEmail;
    private AutoCompleteTextView loginPass;
    private TextInputLayout loginEmailParent;
    private TextInputLayout loginPassParent;

    private ValueEventListener valueEventListener;
    private FirebaseDatabaseService firebaseDatabaseService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.login_progress);
        loginEmail = findViewById(R.id.login_email_et);
        loginPass = findViewById(R.id.login_password_et);
        singinButtonGoogle = findViewById(R.id.login_button_google);
        singinButtonMail = findViewById(R.id.login_button_mail);
        loginButtonSingup = findViewById(R.id.login_button_register);
        loginEmailParent = findViewById(R.id.login_email);
        loginPassParent = findViewById(R.id.login_password);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_client_id))
                .requestEmail()
                .build();

        singinButtonGoogle.setOnClickListener(l -> attemptLoginGoogle(googleSignInOptions));

        singinButtonMail.setOnClickListener(l -> atteptLoginMail());

        loginButtonSingup.setOnClickListener(l -> redirectSignUpActivity());
    }

    private void redirectSignUpActivity() {
        Intent intent = new Intent(this, SignupActivity.class);
        intent.putExtra(SignupActivity.EMAIL_PARAM, loginEmail.getText().toString());
        startActivity(intent);
    }

    private void attemptLoginGoogle(GoogleSignInOptions googleSignInOptions) {
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> result = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = result.getResult(ApiException.class);
                assert account != null;
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                if (mAuth == null){
                    mAuth = FirebaseAuth.getInstance();
                }

                if (mAuth != null){
                    mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
                       if (task.isSuccessful()) {
                           FirebaseUser user = task.getResult().getUser();
                           checkUserDatabaseLogin(user);
                       } else {
                           showErrorDialogMail();
                       }
                    });
                } else {
                    showGooglePlayServicesError();
                }
            } catch (ApiException e) {
                showErrorDialogMail();
            }
        }
    }

    private void atteptLoginMail() {
        loginEmailParent.setError(null);
        loginPassParent.setError(null);

        if(loginEmail.getText().length() == 0) {
            loginEmailParent.setErrorEnabled(true);
            loginEmailParent.setError(getString(R.string.login_mail_error_1));
        } else if(loginPass.getText().length() == 0) {
            loginPassParent.setErrorEnabled(true);
            loginPassParent.setError(getString(R.string.login_mail_error_2));
        } else {
            signInEmail();
        }
    }

    private void signInEmail() {
        if(mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }

        if(mAuth != null) {
            mAuth.signInWithEmailAndPassword(loginEmail.getText().toString(),loginPass.getText().toString()).addOnCompleteListener(this, task -> {
                if (!task.isSuccessful() || task.getResult().getUser() == null) {
                    showErrorDialogMail();
                } else  if (!task.getResult().getUser().isEmailVerified()){
                    showErrorEmailVerify(task.getResult().getUser());
                } else {
                    FirebaseUser user = task.getResult().getUser();
                    checkUserDatabaseLogin(user);
                }
            });
        } else {
            showGooglePlayServicesError();
        }
    }

    private void showGooglePlayServicesError() {
        Snackbar.make(loginButtonSingup, R.string.login_google_play_services_error, Snackbar.LENGTH_LONG).setAction(R.string.login_download_gps, view -> {
            try{
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.gps_download_url))));
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_download_url))));
            }
        }).show();
    }

    private void checkUserDatabaseLogin(FirebaseUser user) {
        //Dummy
        //TODO: complete
        Toast.makeText(this, "User Logged",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (firebaseDatabaseService != null && valueEventListener != null) {
            firebaseDatabaseService.getTravel("1").removeEventListener(valueEventListener);
        }
    }

    private void showErrorEmailVerify(FirebaseUser user) {
        hideLoginButton(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.login_verified_mail_error)
                .setPositiveButton( R.string.login_verified_mail_error_ok,(((dialog, which) -> {
                    user.sendEmailVerification().addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            Snackbar.make(loginEmail, getString(R.string.login_verified_mail_error_sent), Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(loginEmail, getString(R.string.login_verified_mail_error_no_sent), Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }))).setNegativeButton(R.string.login_verified_mail_error_cancel, ((dialog, which) -> {}))
                .show();
    }

    private void showErrorDialogMail() {
        hideLoginButton(false);
        Snackbar.make(singinButtonMail, getString(R.string.login_mail_access_error), Snackbar.LENGTH_SHORT).show();
    }

    private void hideLoginButton(boolean b) {
        TransitionSet transitionSet = new TransitionSet();
        Transition layoutFade = new AutoTransition();
        layoutFade.setDuration(1000);
        transitionSet.addTransition(layoutFade);

        if (b){
            TransitionManager.beginDelayedTransition(findViewById(R.id.login_main_layout), transitionSet);
            progressBar.setVisibility(View.VISIBLE);
            singinButtonMail.setVisibility(View.GONE);
            singinButtonGoogle.setVisibility(View.GONE);
            loginButtonSingup.setVisibility(View.GONE);
            loginEmailParent.setEnabled(false);
            loginPassParent.setEnabled(false);
        } else {
            TransitionManager.beginDelayedTransition(findViewById(R.id.login_main_layout), transitionSet);
            progressBar.setVisibility(View.GONE);
            singinButtonMail.setVisibility(View.VISIBLE);
            singinButtonGoogle.setVisibility(View.VISIBLE);
            loginButtonSingup.setVisibility(View.VISIBLE);
            loginEmailParent.setEnabled(true);
            loginPassParent.setEnabled(true);
        }
    }
}
