package com.jvworld.socialx.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jvworld.socialx.MainActivity;
import com.jvworld.socialx.R;
import com.jvworld.socialx.helper.SignUpHelper;

import java.util.Locale;

public class SignIn extends AppCompatActivity {

    private TextView registerTv, signUpBtnTv, appLogin;
    private EditText emailEd, passwordEd;
    private FirebaseAuth auth;
    private ImageView loginBtnWithGoogle, loginBtnWithFacebook;

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        inItUI();
        startCheck();
        clickHandler();
        createRequest();
    }

    private void inItUI() {
        registerTv = findViewById(R.id.app_register_text);
        signUpBtnTv = findViewById(R.id.sign_up_btn);
        appLogin = findViewById(R.id.app_login);
        passwordEd = findViewById(R.id.app_login_password);
        emailEd = findViewById(R.id.app_login_email);
        TextView appTextName = findViewById(R.id.app_name_id);

        SpannableString ss1=  new SpannableString("SocialX");
        ss1.setSpan(new RelativeSizeSpan(1.5f), 6,7, 0); // set size

        appTextName.setText(ss1);

        loginBtnWithFacebook = findViewById(R.id.login_btn_with_facebook);
        loginBtnWithGoogle = findViewById(R.id.login_btn_with_google);

        auth = FirebaseAuth.getInstance();
    }

    private void clickHandler() {
        signUpBtnTv.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUp.class));
            finish();
        });
        registerTv.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUp.class));
            finish();
        });

        appLogin.setOnClickListener(v -> {
            valueChecker();
        });

        loginBtnWithFacebook.setOnClickListener(v -> {
            Toast.makeText(this, "Please login with google", Toast.LENGTH_SHORT).show();
        });

        loginBtnWithGoogle.setOnClickListener(v -> {
            signIn();
        });

    }


    private void valueChecker() {
        String emailStr = emailEd.getText().toString();
        String passwordStr = passwordEd.getText().toString();
        if (TextUtils.isEmpty(emailStr)) {
            emailEd.setError("Field is required");
            emailEd.requestFocus();
        } else if (passwordStr.length() <= 7 || passwordStr.length() >= 15) {
            passwordEd.setError("Password must be 8-15");
            passwordEd.requestFocus();
        } else {
            loginUser(emailStr, passwordStr);
        }
    }

    private void startCheck() {
        String text = "<font color=#000>Don't have an Account ?</font> <font color=#DE1900>Register Now</font>";
        registerTv.setText(Html.fromHtml(text));
    }


    private void loginUser(String mail, String pass) {
        auth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(SignIn.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(SignIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void createRequest() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        assert user != null;
                        String user_email = user.getEmail();
                        SignUpHelper signUpHelper = new SignUpHelper("",user_email,"",
                                "");
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                        assert user_email != null;
                        reference.child("Google").child(user_email.substring(0,user_email.length()-10)).child("email").setValue(user_email);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(SignIn.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


}