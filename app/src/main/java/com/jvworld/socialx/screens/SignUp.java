package com.jvworld.socialx.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.jvworld.socialx.MainActivity;
import com.jvworld.socialx.R;
import com.jvworld.socialx.helper.SignUpHelper;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    EditText nameEd,numberEd,emailEd,passwordEd;
    private FirebaseAuth mAuth;
    private CountryCodePicker countryCodePicker;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        inItUI();
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

    }

    private void inItUI() {

        TextView loginTv = findViewById(R.id.app_login_text);
        TextView loginBtn = findViewById(R.id.login_btn);
        TextView appTextName = findViewById(R.id.app_name_id);
        nameEd = findViewById(R.id.name_sign_up);
        numberEd = findViewById(R.id.phone_input_filled);
        countryCodePicker = findViewById(R.id.ccp);
        emailEd = findViewById(R.id.email_sign_up);
        passwordEd = findViewById(R.id.password_sign_up);
        CheckBox isAgree = findViewById(R.id.agree_check_box);
        TextView registerTvBtn = findViewById(R.id.app_register);

        SpannableString ss1=  new SpannableString("SocialX");
        ss1.setSpan(new RelativeSizeSpan(1.5f), 6,7, 0); // set size

//        set color
//        ss1.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 5, 0);

        appTextName.setText(ss1);

        registerTvBtn.setOnClickListener(v -> {
            valueChecker();
        });

        loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignIn.class));
            finish();
        });

        loginTv.setOnClickListener(v -> {
            startActivity(new Intent(this, SignIn.class));
            finish();
        });

        String text = "<font color=#000>Already have an Account ?</font> <font color=#DE1900>Sign In!</font>";
        String checkText = "<font color=#000>I agree with </font> <font color=#DE1900>term & condition</font>";

        isAgree.setText(Html.fromHtml(checkText));
        loginTv.setText(Html.fromHtml(text));
    }

    private void valueChecker() {
        String nameStr = nameEd.getEditableText().toString().trim();
        String numberStr = numberEd.getEditableText().toString().trim();
        String emailStr = emailEd.getEditableText().toString().trim();
        String passwordStr = passwordEd.getEditableText().toString().trim();
        if (TextUtils.isEmpty(nameStr)){
            nameEd.setError("Field required");
            nameEd.requestFocus();
        }else if (TextUtils.isEmpty(emailStr)){
            emailEd.setError("Field required");
            emailEd.requestFocus();
        }else if (TextUtils.isEmpty(numberStr)){
            numberEd.setError("Field required");
            numberEd.requestFocus();
        }else if (TextUtils.isEmpty(passwordStr)){
            passwordEd.setError("Field required");
            passwordEd.requestFocus();
        }else if (passwordStr.length() <= 7||passwordStr.length() >= 15) {
            passwordEd.setError("Password must be 8-15");
            passwordEd.requestFocus();
        }
        else{
            signUp(emailStr,passwordStr,numberStr,nameStr);
        }
    }

    private void signUp(String email,String password,String number, String name){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        SignUpHelper signUpHelper = new SignUpHelper(name,email,password,
                                countryCodePicker.getSelectedCountryCodeWithPlus()+ number);
                        reference = firebaseDatabase.getReference("Users");
                        reference.child(countryCodePicker
                                .getSelectedCountryCodeWithPlus()+ number).setValue(signUpHelper);
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                        Toast.makeText(SignUp.this, "Authentication Successful.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUp.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    String error = e.getMessage();
                    switch(Objects.requireNonNull(error)){
                        case "The email address is badly formatted.":
                            emailEd.setError("Invalid Email");
                            emailEd.requestFocus();
                            break;
                        case "The email address is already in use by another account.":
                            Toast.makeText(SignUp.this, "Already a user.", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(SignUp.this, e.toString(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
    }

}