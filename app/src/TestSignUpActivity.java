package com.dhruvi.dhruvisonani.usersidexa2.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dhruvi.dhruvisonani.usersidexa2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class TestSignUpActivity extends AppCompatActivity {

    EditText et_C_password, et_password, et_userName, et_fullName, et_addMin, et_EmailID;
    TextView tv_doLogIn;
    ProgressBar progress_circular;
    RadioGroup radiogroup;
    Button btn_signUp, btn_SendSignUpCode;
    RadioButton rb_male, rb_female;
    //Firebase
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sign_up);

        declaration();
        initialization();
    }

    public void declaration() {
        progress_circular = findViewById(R.id.progress_circular);
        rb_female = findViewById(R.id.rb_female);
        rb_male = findViewById(R.id.rb_male);
        radiogroup = findViewById(R.id.radiogroup);
        et_addMin = findViewById(R.id.et_addMin);
        et_C_password = findViewById(R.id.et_C_password);
        et_EmailID = findViewById(R.id.et_EmailID);
        et_fullName = findViewById(R.id.et_fullName);
        et_password = findViewById(R.id.et_password);
        et_userName = findViewById(R.id.et_userName);
        btn_SendSignUpCode = findViewById(R.id.btn_SendSignUpCode);
        btn_signUp = findViewById(R.id.btn_signUp);

        // databaseReference = FirebaseDatabase.getInstance().getReference("TestSignUp");
        //firebaseAuth = FirebaseAuth.getInstance();


    }

    public void initialization() {
//        if(firebaseAuth.getCurrentUser() != null){
//            Toast.makeText(this, "HEYEYY", Toast.LENGTH_SHORT).show();
//            finishAffinity();
//        }

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress_circular.setVisibility(View.VISIBLE);
                String gender = "";
                if (rb_male.isChecked()) {
                    gender = "Male";
                }
                if (rb_female.isChecked()) {
                    gender = "Female";
                }
                final String fullname = et_fullName.getText().toString();
                final String eamil = et_EmailID.getText().toString();
                //int username = Integer.parseInt(et_userName.getText().toString());
                final String username = et_userName.getText().toString();
                String pwd = et_password.getText().toString();

                final String finalGender = gender;
                firebaseAuth.createUserWithEmailAndPassword(eamil, pwd)
                        .addOnCompleteListener(TestSignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Authenticated Successfully", Toast.LENGTH_SHORT).show();              } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                                    }

                                // ...
                            }
                        });

            }
        });
    }
}

/////Cusotm WHILE SUCCESS


//                                          TestSignUpHelper a = new TestSignUpHelper(fullname,eamil, finalGender,username);
////                                FirebaseDatabase.getInstance().getReference("TestSignUp")
////                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
////                                        .setValue(a).addOnCompleteListener(new OnCompleteListener<Void>() {
////                                    @Override
////                                    public void onComplete(@NonNull Task<Void> task) {
////                                        Toast.makeText(TestSignUpActivity.this, "Successful !! ", Toast.LENGTH_SHORT).show();
////                                    }
////                                })//