package com.dhruvi.dhruvisonani.usersidexa2.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dhruvi.dhruvisonani.usersidexa2.Activity.OnlinePaymentActivity;
import com.dhruvi.dhruvisonani.usersidexa2.Entity.UploadXeroxDataEntity;
import com.dhruvi.dhruvisonani.usersidexa2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.dhruvi.dhruvisonani.usersidexa2.Activity.LoginSignUpActivity.MyPREFERENCES;
import static com.dhruvi.dhruvisonani.usersidexa2.Activity.LoginSignUpActivity.shopName;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.Str_shopkeeperNumber;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_bwxs;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_cxs;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_ls;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_ms;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_ss;

public class XeroxFragment extends Fragment implements View.OnClickListener {

    RadioButton rb_spiral, rb_side;
    CheckBox cb_color_xerox, cb_bW_xerox;
    Button btn_color_decrement_xerox, btn_color_increment_xerox, btn_bw_decrement_xerox, btn_bw_increment_xerox, btn_xerox_manually, btn_xerox_COD;
    TextView tv_color_xerox, tv_bw_xerox;
    RadioGroup rg_spiral_xerox, rg_side;
    EditText tv_xerox_Manually, et_NoteToSendXerox;

    String str_page, str_spiral, str_side, str_color, str_bw, str_note;


    int int_rb_spiral, sum, sepe1,payment;
    public static int int_total;
    String number_list[], sepe[];
    int count = 1;
    private StorageTask uploadTask;
    DatabaseReference mDatabaseReference, mUserreference; //to store url of uploaded file


    public XeroxFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_xerox, container, false);
        declaration(view);
        initialization();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void declaration(View v) {


        rg_spiral_xerox = v.findViewById(R.id.rg_spiral_xerox);
        rg_side = v.findViewById(R.id.rg_side_xerox);
        cb_bW_xerox = v.findViewById(R.id.cb_bW_xerox);
        cb_color_xerox = v.findViewById(R.id.cb_color_xerox);
        btn_bw_increment_xerox = v.findViewById(R.id.btn_bw_increment_xerox);
        btn_bw_decrement_xerox = v.findViewById(R.id.btn_bw_decrement_xerox);
        btn_color_increment_xerox = v.findViewById(R.id.btn_color_increment_xerox);
        btn_xerox_manually = v.findViewById(R.id.btn_xerox_manually);
        btn_xerox_COD = v.findViewById(R.id.btn_xerox_COD);
        tv_xerox_Manually = v.findViewById(R.id.tv_xerox_Manually);
        tv_bw_xerox = v.findViewById(R.id.tv_bw_xerox);
        tv_color_xerox = v.findViewById(R.id.tv_color_xerox);
        et_NoteToSendXerox = v.findViewById(R.id.et_NoteToSendXerox);
        btn_color_decrement_xerox = v.findViewById(R.id.btn_color_decrement_xerox);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("RequestPrintAttachment");
        mUserreference = FirebaseDatabase.getInstance().getReference("User Record");
    }

    public void initialization() {
        btn_xerox_manually.setOnClickListener(this);
        btn_bw_decrement_xerox.setOnClickListener(this);
        btn_bw_increment_xerox.setOnClickListener(this);
        btn_color_increment_xerox.setOnClickListener(this);
        btn_color_decrement_xerox.setOnClickListener(this);
        btn_xerox_COD.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_xerox_manually:
                payment = 1;
                submitXerox();
                break;

            case R.id.btn_xerox_COD:
                payment = 0;
                submitXerox();
                break;
            case R.id.btn_bw_decrement_xerox:
                if (Integer.valueOf(tv_bw_xerox.getText().toString()) > 0) {
                    tv_bw_xerox.setText(String.valueOf(Integer.valueOf(tv_bw_xerox.getText().toString()) - 1));
                }
                break;
            case R.id.btn_bw_increment_xerox:
                tv_bw_xerox.setText(String.valueOf(Integer.valueOf(tv_bw_xerox.getText().toString()) + 1));
                break;
            case R.id.btn_color_decrement_xerox:
                if (Integer.valueOf(tv_color_xerox.getText().toString()) > 0) {
                    tv_color_xerox.setText(String.valueOf(Integer.valueOf(tv_color_xerox.getText().toString()) - 1));
                }
                break;
            case R.id.btn_color_increment_xerox:
                tv_color_xerox.setText(String.valueOf(Integer.valueOf(tv_color_xerox.getText().toString()) + 1));
                break;
        }
    }

    private void submitXerox() {
        int k = 0;

        if (tv_xerox_Manually.getText().toString().equals("0") || tv_xerox_Manually.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Give page numbers of document", Toast.LENGTH_SHORT).show();
            k = 1;
            return;
        } else {
            int[] checklist = new int[100];
            str_page = tv_xerox_Manually.getText().toString();
            sum = 0;
            int j = 0;
            number_list = str_page.split(",");
            try {
                for (int i = 0; i < number_list.length; i++) {
                    if (number_list[i].contains("-")) {
                        sepe = number_list[i].split("-");

                        if (Integer.parseInt(sepe[1]) > Integer.parseInt(sepe[0])) {

                            for (int kk = 0; kk < checklist.length; kk++) {
                                if (checklist[kk] == Integer.parseInt(sepe[0]) || checklist[kk] == Integer.parseInt(sepe[1])) {
                                    Toast.makeText(getActivity(), "Please Check, You repeated page number.", Toast.LENGTH_SHORT).show();
                                    k = 1;
                                    return;
                                } else if (checklist[kk] > Integer.parseInt(sepe[0]) || checklist[kk] > Integer.parseInt(sepe[1])) {
                                    Toast.makeText(getActivity(), "Please check,You gave nested page.", Toast.LENGTH_SHORT).show();
                                    k = 1;
                                    return;
                                }
                            }
                            checklist[j] = Integer.parseInt(sepe[0]);
                            checklist[j + 1] = Integer.parseInt(sepe[1]);
                            j += 2;
                            sum += Integer.parseInt(sepe[1]) + 1 - Integer.parseInt(sepe[0]);
                        } else {
                            Toast.makeText(getActivity(), "Please Check Your Page number Range(s)", Toast.LENGTH_SHORT).show();
                            k = 1;
                            return;
                        }
                    } else {
                        sepe1 = Integer.parseInt(number_list[i]);
                        sum += 1;
                        for (int kk = 0; kk < checklist.length; kk++) {
                            if (checklist[kk] == sepe1) {
                                Toast.makeText(getActivity(), "Please Check, You repeated page number.", Toast.LENGTH_SHORT).show();
                                k = 1;
                                return;
                            }

                        }
                        checklist[j] = sepe1;
                        j += 1;

                    }
                }
            } catch (Exception e) {
                k = 1;
                Toast.makeText(getActivity(), "Please Check Your Page number Range(s)", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        try {
            str_spiral = getCheckedRbSpiral();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Select Spiral or not", Toast.LENGTH_SHORT).show();
            k = 1;
            return;
        }
        try {
            str_side = getCheckedRbSide();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Select Side", Toast.LENGTH_SHORT).show();
            k = 1;
            return;
        }
        if (!cb_bW_xerox.isChecked() && !cb_color_xerox.isChecked()) {
            Toast.makeText(getActivity(), "Please Select Type of Xerox", Toast.LENGTH_SHORT).show();
            k = 1;
            return;
        } else {
            str_color = "0";
            str_bw = "0";
            if (cb_color_xerox.isChecked()) {
                if (tv_color_xerox.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), "Please Select count for Color xerox", Toast.LENGTH_SHORT).show();
                    k = 1;
                    return;
                } else {
                    str_color = tv_color_xerox.getText().toString();

                }
            }

            if (cb_bW_xerox.isChecked()) {
                if (tv_bw_xerox.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), "Please Select count for B&W xerox", Toast.LENGTH_SHORT).show();
                    k = 1;
                    return;
                } else {
                    str_bw = tv_bw_xerox.getText().toString();
                }
            }
        }

        if (et_NoteToSendXerox.getText().toString().equals("")) {
            str_note = "No Note";
        } else {
            str_note = et_NoteToSendXerox.getText().toString();
        }
        if (k != 1) {
            SendXeroxRequest();
//            UploadXeroxData();
        }
    }

    private void UploadXeroxData() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String formattedDate = df.format(c);

        Toast.makeText(getActivity(), "Sending Your Request, Please wait.", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username = sharedpreferences.getString("uname", "");
        String a = username;
        if (str_side.equals("One Side")) {
            sum *= 2;
        }
        if(payment == 1) {
            UploadXeroxDataEntity entity = new UploadXeroxDataEntity(str_page, str_spiral, str_side, str_color, str_bw, str_note, String.valueOf(sum), username, String.valueOf(int_total)+" INR Online Paid");
            UploadXeroxDataEntity userEntity = new UploadXeroxDataEntity(str_page, str_spiral, str_side, str_color, str_bw, str_note, String.valueOf(sum), username, String.valueOf(int_total)+" INR Online Paid", Str_shopkeeperNumber, shopName, formattedDate);
            mDatabaseReference.child(Str_shopkeeperNumber).child(username + "_" + str_page + "_" + str_color).setValue(entity);
            mUserreference.child(username).child("Xerox" + "_" + str_page + "_" + str_color).setValue(userEntity);
        }
        else {
            UploadXeroxDataEntity entity = new UploadXeroxDataEntity(str_page, str_spiral, str_side, str_color, str_bw, str_note, String.valueOf(sum), username, String.valueOf(int_total) + " INR CoC");
            UploadXeroxDataEntity userEntity = new UploadXeroxDataEntity(str_page, str_spiral, str_side, str_color, str_bw, str_note, String.valueOf(sum), username, String.valueOf(int_total) + " INR CoC", Str_shopkeeperNumber, shopName, formattedDate);
            mDatabaseReference.child(Str_shopkeeperNumber).child(username + "_" + str_page + "_" + str_color).setValue(entity);
            mUserreference.child(username).child("Xerox" + "_" + str_page + "_" + str_color).setValue(userEntity);
        }
        Toast.makeText(getActivity(), "Request Sent to " + shopName + ", You will get their acknowledgment soon !!", Toast.LENGTH_LONG).show();
    }

    private void getCount(String a) {
        final Query query1 = FirebaseDatabase.getInstance().getReference().child("RequestXerox").child(Str_shopkeeperNumber).child(a + "_1585044034253");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String dhu = (String) child.child("count").getValue();
                    Toast.makeText(getActivity(), dhu, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        });

    }

//    private void countTotal(){
//        int int_numpage = 0,color,bw;
//        int_total=0;
//        int_numpage = sum;
//        if(Integer.parseInt(str_bw)>1 && cb_bW_xerox.isChecked()){
//            if(int_numpage<5 || Integer.parseInt(str_bwxs)<1){
//
//            }
//        }
//
//    }

    private void countTotal() {
        int int_numPage = 0, color = 0, bw = 0;
        int_total = 0;
        int_numPage = sum;
//        if(str_side.equals("One Side")){
//            int_numPage = sum;
//        }
//        else{
//            if(sum%2 == 0){
//                int_numPage = sum/2;}
//            else{
//                int_numPage = (sum/2)+1;
//            }
//        }
        if (Integer.valueOf(str_bw) > 0) {
            if (int_numPage < 5 || Integer.parseInt(str_bw) > 1) {

                bw += Math.round(Float.parseFloat(String.valueOf(Integer.parseInt(str_bwxs) * 0.01 * int_numPage)));
                if (bw < 1) {
                    bw = 1;
                }
            } else {
                bw += Math.round(Float.parseFloat(String.valueOf(Integer.parseInt(str_bwxs) * 0.01 * int_numPage)));
            }
        }
        if (Integer.valueOf(str_color) > 0) {
            if (int_numPage < 5 || Integer.parseInt(str_color) > 1) {
                color += Math.round(Float.parseFloat(String.valueOf(Integer.parseInt(str_cxs) * 0.01 * int_numPage)));
                if (color < 1) {
                    color = 1;
                }
            } else {
                color += Math.round(Float.parseFloat(String.valueOf(Integer.parseInt(str_cxs) * 0.01 * int_numPage)));
            }
        }

        if (!"No".equals(str_spiral) && cb_bW_xerox.isChecked()) {
            if (int_numPage < 36) {
                bw += Integer.parseInt(str_ss);
            } else if (int_numPage < 71) {
                bw += Integer.parseInt(str_ms);
            } else {
                bw += Integer.parseInt(str_ls);
            }
        }
        if (!"No".equals(str_spiral) && cb_color_xerox.isChecked()) {
            if (int_numPage < 36) {
                color += Integer.parseInt(str_ss);
            } else if (int_numPage < 71) {
                color += Integer.parseInt(str_ms);
            } else {
                color += Integer.parseInt(str_ls);
            }
        }

        if (cb_color_xerox.isChecked() && cb_bW_xerox.isChecked()) {
            if (Integer.parseInt(str_color) > 0 && Integer.parseInt(str_bw) > 0) {
                int_total = bw * Integer.parseInt(str_bw) + color * Integer.parseInt(str_color);
            }
        } else if (cb_bW_xerox.isChecked()) {
            if (Integer.parseInt(str_bw) > 0) {
                int_total = bw * Integer.parseInt(str_bw);
            }
        } else if (cb_color_xerox.isChecked()) {
            if (Integer.parseInt(str_color) > 0) {
                int_total = color * Integer.parseInt(str_color);
            }
        }
    }

    private void SendXeroxRequest() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(true);
        countTotal();
        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog, null);
        dialog.setContentView(view);
        LinearLayout ll_document, ll_bound, ll_pagesize, ll_totalpage;
        ll_document = view.findViewById(R.id.ll_document);
        ll_pagesize = view.findViewById(R.id.ll_pagesize);
        ll_totalpage = view.findViewById(R.id.ll_totalpage);
        ll_bound = view.findViewById(R.id.ll_bound);
        TextView tv_cd_note = view.findViewById(R.id.tv_cd_note);
        Button edit = view.findViewById(R.id.btn_cancel_popup);
        Button delete = view.findViewById(R.id.btn_delete_data);
        TextView tv_cd_pageNumber = view.findViewById(R.id.tv_cd_pageNumber);
        TextView tv_cd_spiral = view.findViewById(R.id.tv_cd_spiral);
        TextView tv_cd_side = view.findViewById(R.id.tv_cd_side);
        TextView tv_cd_typeNnumber = view.findViewById(R.id.tv_cd_typeNnumber);
        TextView tv_cd_total = view.findViewById(R.id.tv_cd_total);
        TextView tv_cd_amountTopay = view.findViewById(R.id.tv_cd_amountTopay);
        ll_document.setVisibility(View.GONE);
        ll_totalpage.setVisibility(View.VISIBLE);
        ll_bound.setVisibility(View.GONE);
        ll_pagesize.setVisibility(View.GONE);
        delete.setText("Proceed to checkout");
        if (et_NoteToSendXerox.getText().toString().equals("")) {
            tv_cd_note.setText("No Note");
        } else {
            tv_cd_note.setText(et_NoteToSendXerox.getText().toString());
        }

        tv_cd_pageNumber.setText(str_page);
//        if (int_total < 1) {
//            int_total = 1;
//            tv_cd_amountTopay.setText(String.valueOf(int_total) + " INR");
//        } else {
        tv_cd_amountTopay.setText(String.valueOf(int_total) + " INR");
//        }
        tv_cd_side.setText(str_side);
        tv_cd_total.setText(String.valueOf(sum));
        tv_cd_spiral.setText(str_spiral);
        if (cb_bW_xerox.isChecked() && cb_color_xerox.isChecked()) {
            tv_cd_typeNnumber.setText(str_bw + " B&W Xerox and " + str_color + " Color Xerox");
        } else if (cb_bW_xerox.isChecked()) {
            tv_cd_typeNnumber.setText(str_bw + " B&W Xerox");
        } else if (cb_color_xerox.isChecked()) {
            tv_cd_typeNnumber.setText(str_color + " Color Xerox");
        }
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
                dialog.dismiss();
                if(payment == 1) {
                    PrintFragment.int_total = 0;
                    AvailableBookFragment.b = 0;
                    Toast.makeText(getActivity(), "Towards Payment...", Toast.LENGTH_SHORT).show();
                    startActivityForResult(new Intent(getActivity(), OnlinePaymentActivity.class), 10001);
                }
                else{
                    UploadXeroxData();
                }
            }

        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 10001) && (resultCode == 1)) {
            // recreate your fragment here
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(new PrintFragment()).attach(new XeroxFragment()).commit();
            UploadXeroxData();
//            Toast.makeText(getActivity(), "Loaded", Toast.LENGTH_SHORT).show();

        }
    }

    private String getCheckedRbSide() {
        String str1 = " ";
        int_rb_spiral = rg_side.getCheckedRadioButtonId();
        rb_side = getView().findViewById(int_rb_spiral);
        str1 = rb_side.getText().toString();

        return str1;
    }

    private String getCheckedRbSpiral() {
        String str_spiral = " ";
        int_rb_spiral = rg_spiral_xerox.getCheckedRadioButtonId();
        rb_spiral = getView().findViewById(int_rb_spiral);
        str_spiral = rb_spiral.getText().toString();
        return str_spiral;
    }

}
