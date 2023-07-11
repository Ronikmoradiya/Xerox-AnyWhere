package com.dhruvi.dhruvisonani.usersidexa2.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.dhruvi.dhruvisonani.usersidexa2.Fragment.LoginFragment;
import com.dhruvi.dhruvisonani.usersidexa2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LoginSignUpActivity extends AppCompatActivity {

    FirebaseAuth  firebaseAuth;

    public static String MyPREFERENCES = "dhruvi";
    DocumentReference databaseReference;
    public static String shopName;
    public static List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        declaration();
        initialization();
    }
    public void declaration(){
        firebaseAuth = FirebaseAuth.getInstance();
        try {
            shopName = getIntent().getStringExtra("markerTitle").toString();
        }
        catch (Exception e){
        }
    }
    public void initialization(){


        //getSupportFragmentManager().beginTransaction().add(R.id.frame_login_signUp,new LoginFragment()).addToBackStack(null).commit();

        FirebaseFirestore.getInstance().collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
//                    Toast.makeText(LoginSignUpActivity.this, list.toString(), Toast.LENGTH_SHORT).show();
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//
//                    fragmentTransaction.add(R.id.frame_login_signUp,new LoginFragment());
//
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();

                } else {
                    Toast.makeText(LoginSignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

        });
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.frame_login_signUp,new LoginFragment());

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
//


    }
}


//                    for (String value : list) {
//                        databaseReference = FirebaseFirestore.getInstance().collection("Shopkeeper").document(value);
//                        databaseReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                            @Override
//                            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
//                                if (e != null) {
//                                    return;
//                                }
//                                if (snapshot != null && snapshot.exists()) {
//                                    if (ShopkeeperNumber.equals(snapshot.getString("Mobile Number"))) {
//
//                                        Toast.makeText(getActivity(), snapshot.getString("Full Name"), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                        });
//                    }





//        FirebaseFirestore.getInstance().collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    List<String> list = new ArrayList<>();
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        list.add(document.getId());
//                    }
//
//                    Toast.makeText(getActivity(), list.toString(), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), "eror", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
