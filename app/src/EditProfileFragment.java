package com.dhruvi.dhruvisonani.usersidexa2.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dhruvi.dhruvisonani.usersidexa2.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import static com.dhruvi.dhruvisonani.usersidexa2.Activity.LoginSignUpActivity.MyPREFERENCES;

public class EditProfileFragment extends Fragment {

    TextView tv_et_Name, tv_ep_mobileNumber, tv_ep_mailId, tv_et_gender;
    EditText et_et_pwd, et_et_rpwd;
    ImageView iv_et_update;

    DocumentReference databaseReference;

    public EditProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        declaration(view);
        initialization();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initialization() {
        iv_et_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog();
                Toast.makeText(getActivity(), "Update", Toast.LENGTH_SHORT).show();
            }
        });

        setValue();

    }

    private void setValue() {

        databaseReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    et_et_pwd.setText(snapshot.getString("Password"));
                    et_et_rpwd.setText(snapshot.getString("Password"));
                    tv_ep_mailId.setText(snapshot.getString("Email Id"));
                    tv_et_Name.setText(snapshot.getString("Full Name"));
                    tv_ep_mobileNumber.setText(snapshot.getString("Mobile Number"));
                    tv_et_gender.setText(snapshot.getString("Gender"));
                }
            }
        });
    }

    private void UpdateValue() {
        Map<String, Object> User = new HashMap<>();

        User.put("Full Name", tv_et_Name.getText().toString());
        User.put("Email Id", tv_ep_mailId.getText().toString());
        User.put("Mobile Number", tv_ep_mobileNumber.getText().toString());
        User.put("Gender", tv_et_gender.getText().toString());
        if (et_et_pwd.getText().toString().equals(et_et_rpwd.getText().toString())) {
            User.put("Password", et_et_pwd.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Password don't matched", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.set(User).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void declaration(View view) {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username = sharedpreferences.getString("uname", "");


        databaseReference = FirebaseFirestore.getInstance().collection("Users").document(username);

        iv_et_update = view.findViewById(R.id.iv_et_update);

        et_et_pwd = view.findViewById(R.id.et_et_pwd);
        et_et_rpwd = view.findViewById(R.id.et_et_rpwd);

        tv_et_gender = view.findViewById(R.id.tv_et_gender);
        tv_et_Name = view.findViewById(R.id.tv_et_Name);
        tv_ep_mobileNumber = view.findViewById(R.id.tv_ep_mobileNumber);
        tv_ep_mailId = view.findViewById(R.id.tv_ep_mailId);

    }

    private void OpenDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(true);

        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog_update, null);
        dialog.setContentView(view);

        Button edit = view.findViewById(R.id.btn_cancel_popup);
        Button delete = view.findViewById(R.id.btn_delete_data);
        delete.setText("Update");
        dialog.setCanceledOnTouchOutside(false);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateValue();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
