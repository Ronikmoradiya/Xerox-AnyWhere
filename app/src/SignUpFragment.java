package com.dhruvi.dhruvisonani.usersidexa2.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dhruvi.dhruvisonani.usersidexa2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SignUpFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    EditText et_C_password, et_password, et_userName, et_fullName, et_EmailID, et_otp;
    TextView tv_terms;
    Button btn_signUp, btn_SendSignUpCode;
    ProgressDialog progressDialog;
    CheckBox cb_signup;

    RadioGroup radioGroup;
    RadioButton rb_fm;

    int k = 0;
    private String ver_id;

    String str_name, str_emailId, str_pwd, str_cpwd, str_gender, str_mobileNum;
    ;
    String UserId;
    FirebaseFirestore fstore; // storing user data in database

    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        declaration(view);
        initialization(view);
        return view;

    }

    public void declaration(View v) {

        btn_SendSignUpCode = v.findViewById(R.id.btn_SendSignUpCode);
//        et_addMin = v.findViewById(R.id.et_addMin);
        et_C_password = v.findViewById(R.id.et_C_password);
        et_EmailID = v.findViewById(R.id.et_EmailID);
        et_password = v.findViewById(R.id.et_password);
        et_fullName = v.findViewById(R.id.et_fullName);
        progressDialog = new ProgressDialog(getActivity());
        et_userName = v.findViewById(R.id.et_userName);
        btn_signUp = v.findViewById(R.id.btn_signUp);
        radioGroup = v.findViewById(R.id.radiogroup);
        et_otp = v.findViewById(R.id.et_otp);
        tv_terms = v.findViewById(R.id.tv_terms);
        cb_signup = v.findViewById(R.id.cb_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        //  Whether user register or login
//        if(firebaseAuth.getCurrentUser() != null){
//            Intent i = new Intent(getActivity(),ShopInformationBottomActivity.class);
//                startActivity(i);
//              }

    }

    public void initialization(View view) {
        tv_terms.setText("1. You are responsible for content you attach and send.\n2. You are responsible to check while collecting your stuffs while checking out shop.\n3. Your device must need GPS service available.\n4. Your device will need any online payment application without fail.\n5. Your device must be connected with internet or wi-fi network state.");
        radioGroup.setOnCheckedChangeListener(this);
        btn_signUp.setOnClickListener(this);
        btn_SendSignUpCode.setOnClickListener(this);
    }

    private void checkUser() {
        FirebaseFirestore.getInstance().collection("Users").document(et_userName.getText().toString()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    k = 1;
//                    Toast.makeText(getActivity(), "Mobile NUmber is already in use. try with another number", Toast.LENGTH_SHORT).show();
                    et_userName.setError("Mobile NUmber is already in use. try with another number");
                    et_userName.requestFocus();
                    return;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_SendSignUpCode:
                if (et_userName.length() == 10) {
                    if (TextUtils.isEmpty(et_fullName.getText().toString())) {
                        et_fullName.setError("Enter Full Name");
                        et_fullName.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(et_EmailID.getText().toString())) {
                        et_EmailID.setError("Enter Email Id");
                        et_EmailID.requestFocus();
                        return;
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(et_EmailID.getText().toString()).matches()) {
                        et_EmailID.setError("Enter Valid Email Id");
                        et_EmailID.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(str_gender)) {
                        Toast.makeText(getActivity(), "Select Gender", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(et_userName.getText().toString())) {
                        et_userName.setError("Enter Mobile Number");
                        et_userName.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(et_password.getText().toString())) {
                        et_password.setError("Enter Strong Password");
                        et_password.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(et_C_password.getText().toString())) {
                        et_C_password.setError("Re-enter Password");
                        et_C_password.requestFocus();
                        return;
                    }
                    if (!et_C_password.getText().toString().equals(et_password.getText().toString())) {
                        et_C_password.setError("Password don't match, check again.");
                        et_C_password.requestFocus();
                        return;
                    }
                    checkUser();

                    if (k == 0) {
                        sendVerificationCode();
                    }
                    else{
                        return;
                    }
//                    else {
//                        et_userName.setError("Number is already in use.");
//                        et_userName.requestFocus();
//                        k=0;
//                        return;
//                    }
                } else {
                    et_userName.setError("Enter Mobile Number Correctly");
                    et_userName.requestFocus();
                    return;
                }
                break;

            case R.id.btn_signUp:
                String verify = et_otp.getText().toString();

                if (TextUtils.isEmpty(et_fullName.getText().toString())) {
                    et_fullName.setError("Enter Full Name");
                    et_fullName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(et_EmailID.getText().toString())) {
                    et_EmailID.setError("Enter Email Id");
                    et_EmailID.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(str_gender)) {
                    Toast.makeText(getActivity(), "Select Gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_userName.getText().toString())) {
                    et_userName.setError("Enter Mobile Number");
                    et_userName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(et_password.getText().toString())) {
                    et_password.setError("Enter Strong Password");
                    et_password.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(et_C_password.getText().toString())) {
                    et_C_password.setError("Re-enter Password");
                    et_C_password.requestFocus();
                    return;
                }
                if (!et_C_password.getText().toString().equals(et_password.getText().toString())) {
                    et_C_password.setError("Password don't match, check again.");
                    et_C_password.requestFocus();
                    return;
                }
                if (verify.length() < 6) {
                    et_otp.setError("Enter Correct Code");
                    et_otp.requestFocus();
                    return;
                }
                if (cb_signup.isChecked()) {
                    Toast.makeText(getActivity(), "PLease Wait..", Toast.LENGTH_SHORT).show();
                    verifyCode(verify);
                } else {
                    Toast.makeText(getActivity(), "Accept Terms and Conditions", Toast.LENGTH_SHORT).show();
                    return;
                }

                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_male:
                str_gender = "Male";
                break;
            case R.id.rb_female:
                str_gender = "Female";
                break;
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    add_user();
//                    Toast.makeText(getActivity(), "Code Received", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void add_user() {
        str_name = et_fullName.getText().toString().trim();
        str_emailId = et_EmailID.getText().toString();
        str_pwd = et_password.getText().toString().trim();
        str_cpwd = et_C_password.getText().toString().trim();
        str_mobileNum = et_userName.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(str_emailId, str_pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Created Successfully ", Toast.LENGTH_SHORT).show();
                    UserId = et_userName.getText().toString();
                    DocumentReference documentReference = fstore.collection("Users").document(UserId);
                    Map<String, Object> User = new HashMap<>();
                    User.put("Full Name", str_name);
                    User.put("Email Id", str_emailId);
                    User.put("Mobile Number", "+91" + str_mobileNum);
                    User.put("Gender", str_gender);
                    User.put("Password", str_pwd);
                    documentReference.set(User).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                            firebaseAuth.signOut();

                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame_login_signUp, new LoginFragment());
                            fragmentTransaction.commit();
                            Toast.makeText(getActivity(), "Enter Credential to Log In", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Error !! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void verifyCode(String code) {
//        try{520782
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(ver_id, code);
            signInWithCredential(credential);
//            add_user();

        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendVerificationCode() {
        try {
            String num = et_userName.getText().toString();
            num = "+91" + num;
            PhoneAuthProvider.getInstance().verifyPhoneNumber(num, 120, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);//583473
            Toast.makeText(getActivity(), "Wait Untill You Receive 6 digit Code !!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            ver_id = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
//                et_verificationCode.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };
}


//                    firebaseAuth.createUserWithEmailAndPassword(str_emailId,str_pwd).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                //startActivity(new Intent(getActivity(), ShopInformationBottomActivity.class));
//                                //Toast.makeText(getActivity(), "SignUp Successful", Toast.LENGTH_SHORT).show();
//
//                                //FirebaseUser user = firebaseAuth.getCurrentUser();
//
//                                SignUpEntity info = new SignUpEntity(str_name, str_emailId, str_gender, str_pwd);
//
//                                FirebaseDatabase.getInstance().getReference("SignUpEntity")
//                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(info)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
//                                //updateUI(user);
//                            } else {
//                                Toast.makeText(getActivity(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });


//public class SignUpFragment<DatabaseReference, FirebaseDatabse> extends Fragment implements View.OnClickListener {
//    EditText et_C_password,et_password,et_userName,et_fullName,et_addMin,et_EmailID;
//    TextView tv_doLogIn;
//    Button btn_signUp,btn_SendSignUpCode;
//    ProgressDialog progressDialog;
//
//    private FirebaseAuth firebaseAuth;
//
//    DatabaseHelper helper;
//
//    //FireBase
//
//    private DatabaseReference databaseReference;
//    private FirebaseDatabse firebaseDatabse;
//
//
//    public SignUpFragment() {
//        // Required empty public constructor
//    }
//
//
//    //values
//    String str_name,str_emailId, str_pwd, str_cpwd,int_mobileNum;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);
//        declaration(view);
//        initialization(view);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }
//
//    public void declaration(View v){
//        btn_SendSignUpCode = v.findViewById(R.id.btn_SendSignUpCode);
//        et_addMin = v.findViewById(R.id.et_addMin);
//        et_C_password = v.findViewById(R.id.et_C_password);
//        et_EmailID = v.findViewById(R.id.et_EmailID);
//        et_password = v.findViewById(R.id.et_password);
//        et_fullName = v.findViewById(R.id.et_fullName);
//        progressDialog = new ProgressDialog(getActivity());
//        et_userName = v.findViewById(R.id.et_userName);
//        tv_doLogIn = v.findViewById(R.id.tv_doLogIn);
//        btn_signUp = v.findViewById(R.id.btn_signUp);
//        helper = new DatabaseHelper(getContext());
//    }
//    public void initialization(View v){
//
//        btn_signUp.setOnClickListener(this);
//        tv_doLogIn.setOnClickListener(this);
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        databaseReference = FirebaseDatabse.getInstance().getReference("User");
//    }
//    @Override
//    public void onClick(View v) {
////        if(v.getId() == R.id.btn_signUp){
////            if(TextUtils.isEmpty(et_addMin.getText().toString())  || TextUtils.isEmpty(et_C_password.getText().toString())||
////                    TextUtils.isEmpty(et_password.getText().toString())|| TextUtils.isEmpty(et_fullName.getText().toString())||
////                    TextUtils.isEmpty(et_userName.getText().toString())){
////                Toast.makeText(getActivity(), "Fill Fields Properly", Toast.LENGTH_SHORT).show();
////            }
////            else if(!et_C_password.getText().toString().equals(et_password.getText().toString())){
////                Toast.makeText(getActivity(), "Password don't matched", Toast.LENGTH_SHORT).show();
////            }
////            else{
////                //SQLite
////                Contact_SignUp c = new Contact_SignUp();
////                c.setFull_name(et_fullName.getText().toString());
////                c.setPass_word(et_password.getText().toString());
////                c.setPhone_number(Long.valueOf(et_userName.getText().toString()));
////
////                helper.insertContact(c);
////                Intent intent = new Intent(getActivity(), DisplaySignedUpUserActivity.class);
////                startActivity(intent);
////
////                //authentication Firebase
////
////                /*auth.createUserWithEmailAndPassword(et_userName.getText().toString(),et_password.getText().toString())
////                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
////                            @Override
////                            public void onComplete(@NonNull Task<AuthResult> task) {
////                                if(task.isSuccessful()){
////                                    startActivity(new Intent(getActivity(), ShopInformationBottomActivity.class));
////                                    Toast.makeText(getActivity(), "SignUp Successful", Toast.LENGTH_SHORT).show();
////                                }
////                                else{
////                                    Toast.makeText(getActivity(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
////                                }
////                            }
////                        });*/
////
////
////                //sqlite
////
////
////            }
////        }
//
//        if (v.getId() == R.id.btn_SendSignUpCode){
//            if(TextUtils.isEmpty(et_addMin.getText().toString())  || TextUtils.isEmpty(et_C_password.getText().toString())||
//                    TextUtils.isEmpty(et_password.getText().toString())|| TextUtils.isEmpty(et_fullName.getText().toString())||
//                    TextUtils.isEmpty(et_userName.getText().toString())){
//                  Toast.makeText(getActivity(), "Fill Fields Properly", Toast.LENGTH_SHORT).show();
//            }
//            if(!et_C_password.getText().toString().equals(et_password.getText().toString())){
//                Toast.makeText(getActivity(), "Password don't matched", Toast.LENGTH_SHORT).show();
//            }
//            else{
//
//            }
//        }
//
//        else if (v.getId()==R.id.btn_signUp) {
//            str_name = et_fullName.getText().toString().trim();
//            str_emailId = et_EmailID.getText().toString();
//            str_pwd = et_password.getText().toString().trim();
//            str_cpwd = et_C_password.getText().toString().trim();
//            int_mobileNum = et_userName.getText().toString().trim();
//
//            if (TextUtils.isEmpty(et_addMin.getText().toString()) || TextUtils.isEmpty(et_C_password.getText().toString()) || TextUtils.isEmpty(et_password.getText().toString()) || TextUtils.isEmpty(et_fullName.getText().toString()) || TextUtils.isEmpty(et_userName.getText().toString())) {
//                Toast.makeText(getActivity(), "Fill Fields Properly", Toast.LENGTH_SHORT).show();
//            }
//            if (!et_C_password.getText().toString().equals(et_password.getText().toString())) {
//                Toast.makeText(getActivity(), "Password don't matched", Toast.LENGTH_SHORT).show();
//            }
//            else if(v.getId() == R.id.tv_doLogIn){
//                getFragmentManager().beginTransaction().replace(R.id.frame_login_signUp,new LoginFragment()).addToBackStack(null).commit();
//            }
//            else {
//                progressDialog.setMessage("Please Wait for a time");
//                progressDialog.show();
//
//                firebaseAuth.createUserWithEmailAndPassword(str_emailId, str_pwd)
//                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            //registered successful
//                            Toast.makeText(getActivity(), "You Are Registered", Toast.LENGTH_SHORT).show();
//                            progressDialog.dismiss();
//                        }
//                        else{
//                            Toast.makeText(getActivity(), "sorry", Toast.LENGTH_SHORT).show();
//                            progressDialog.dismiss();
//                        }
//                    }
//                });
//            }
//        }
//
//    }
//}

