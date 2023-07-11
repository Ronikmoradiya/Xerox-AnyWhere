package com.dhruvi.dhruvisonani.usersidexa2.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dhruvi.dhruvisonani.usersidexa2.Activity.OnlinePaymentActivity;
import com.dhruvi.dhruvisonani.usersidexa2.Activity.PdfShowActivity;
import com.dhruvi.dhruvisonani.usersidexa2.Helper.UploadPDF;
import com.dhruvi.dhruvisonani.usersidexa2.R;
import com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.dhruvi.dhruvisonani.usersidexa2.Activity.LoginSignUpActivity.MyPREFERENCES;
import static com.dhruvi.dhruvisonani.usersidexa2.Activity.LoginSignUpActivity.shopName;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.Str_shopkeeperNumber;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_a1;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_a2;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_a3;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_a4;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_bwps;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_cb;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_ciwb;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_cps;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_hard;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_jb;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_ls;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_ms;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_pb;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_ss;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_ssb;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.str_sseb;

public class PrintFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    TextView tv_color, tv_bw, tv_setAttachmentPath;
    EditText et_NoteToSend;
    TextView et_NumofPages;
    Button btn_color_decrement, btn_color_increment, btn_bw_decrement, btn_bw_increment, btn_submit_print;
    ImageView btn_attachment;
    RadioGroup rg_spiral, rg_side, rg_type_bind;
    RadioButton rb_spiral_bw, rb_side, rb_bound;
    CheckBox cb_color, cb_bW, cb_A1, cb_A2, cb_A3, cb_A4, cb_page;
    LinearLayout ll_spinner;
    Spinner spinner_bound;

    StorageTask uploadTask;
    DatabaseReference mDatabaseReference, mUserreference; //to store url of uploaded file
    StorageReference mStorageReference;

    int int_rb_spiral, int_rb_side, int_rb_bound, int_bound, count = 0;
    public static int int_total = 0;
    String str_rb_spiral, str_rb_side, str_rb_bound, str_bw_count, str_cb_pagesize, str_color_count, str_numpage, str_note, a, idStr, str_doBound;
    ArrayList<String> str_spinnerList;
    ArrayList<Integer> int_boundPrice;

    UploadPDF uploadPDFde;

    public PrintFragment() {
    }

    Uri pdfUri;  //meant for local storage


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_print, container, false);

//        getFragmentManager().beginTransaction().detach(new PrintFragment()).attach(new PrintFragment()).commit();
//        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        list();

        declaration(view);
        initialization();
        return view;
    }

//    private void list() {
//        FirebaseFirestore.getInstance().collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    List<String> list = new ArrayList<>();
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        list.add(document.getId());
//                    }
//
//                } else {
//                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public void declaration(View v) {

        tv_setAttachmentPath = v.findViewById(R.id.tv_setAttachmentPath);
        btn_attachment = v.findViewById(R.id.btn_attachment);
        et_NumofPages = v.findViewById(R.id.et_NumofPages);
        rg_spiral = v.findViewById(R.id.rg_spiral);
        tv_color = v.findViewById(R.id.tv_color);

        spinner_bound = v.findViewById(R.id.spinner_bound);
        tv_bw = v.findViewById(R.id.tv_bw);
        btn_bw_decrement = v.findViewById(R.id.btn_bw_decrement);
        btn_bw_increment = v.findViewById(R.id.btn_bw_increment);
        btn_color_decrement = v.findViewById(R.id.btn_color_decrement);
        btn_submit_print = v.findViewById(R.id.btn_submit_print);
        btn_color_increment = v.findViewById(R.id.btn_color_increment);
        cb_bW = v.findViewById(R.id.cb_bW);
        rg_side = v.findViewById(R.id.rg_side);
        rg_type_bind = v.findViewById(R.id.rg_type_bind);
        cb_color = v.findViewById(R.id.cb_color);
        cb_page = v.findViewById(R.id.cb_page);
        et_NoteToSend = v.findViewById(R.id.et_NoteToSend);
        ll_spinner = v.findViewById(R.id.ll_spinner);
        cb_A1 = v.findViewById(R.id.cb_A1);
        cb_A2 = v.findViewById(R.id.cb_A2);
        cb_A3 = v.findViewById(R.id.cb_A3);
        cb_A4 = v.findViewById(R.id.cb_A4);


        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("RequestPrintAttachment");
        mUserreference = FirebaseDatabase.getInstance().getReference("User Record");
    }

    public void initialization() {


        btn_color_increment.setOnClickListener(this);
        btn_color_decrement.setOnClickListener(this);
        btn_bw_increment.setOnClickListener(this);
        btn_bw_decrement.setOnClickListener(this);
        btn_submit_print.setOnClickListener(this);
        btn_attachment.setOnClickListener(this);
        rg_type_bind.setOnCheckedChangeListener(this);
        tv_setAttachmentPath.setOnClickListener(this);


    }

    private void Spinner_string() {
        str_spinnerList = new ArrayList<String>();
        int_boundPrice = new ArrayList<Integer>();
        int_boundPrice.add(0);
        str_spinnerList.add("----Select----");
        if (!"".equals(str_hard)) {
            str_spinnerList.add("Hard Bound");
            int_boundPrice.add(Integer.parseInt(str_hard));
        }
        if (!"".equals(str_ssb)) {
            str_spinnerList.add("Sadle Stiching Bound");
            int_boundPrice.add(Integer.parseInt(str_ssb));
        }
        if (!"".equals(ShopInformationBottomActivity.str_sseb)) {
            str_spinnerList.add("Section Sewn Bound");
            int_boundPrice.add(Integer.parseInt(str_sseb));
        }
        if (!"".equals(ShopInformationBottomActivity.str_cb)) {
            str_spinnerList.add("Coptic Bound");
            int_boundPrice.add(Integer.parseInt(str_cb));
        }
        if (!"".equals(ShopInformationBottomActivity.str_jb)) {
            str_spinnerList.add("Japanese Bound");
            int_boundPrice.add(Integer.parseInt(str_jb));
        }
        if (!"".equals(ShopInformationBottomActivity.str_ciwb)) {
            str_spinnerList.add("Cased in wiro Bound");
            int_boundPrice.add(Integer.parseInt(str_ciwb));
        }
        if (!"".equals(str_pb)) {
            str_spinnerList.add("Pamplet Bound");
            int_boundPrice.add(Integer.parseInt(str_pb));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onClick(View v) {
        int a;
        switch (v.getId()) {

            case R.id.btn_submit_print:

                SubmitPrint();
                break;
            case R.id.tv_setAttachmentPath:

                if (!"Set Your Path Here".equals(tv_setAttachmentPath.getText().toString())) {
                    Intent i = new Intent(getActivity(), PdfShowActivity.class);
                    i.putExtra("uri", pdfUri.toString());
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "Select a file", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_bw_decrement:
                a = Integer.valueOf(tv_bw.getText().toString());
                if (a > 0) {
                    tv_bw.setText(String.valueOf(a - 1));
                }
                break;
            case R.id.btn_attachment:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    getFragmentManager().beginTransaction().detach(new PrintFragment()).attach(new PrintFragment()).commit();
                    SelectFile();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
                break;
            case R.id.btn_bw_increment:
                a = Integer.valueOf(tv_bw.getText().toString());
                tv_bw.setText(String.valueOf(a + 1));
                break;
            case R.id.btn_color_decrement:
                a = Integer.valueOf(tv_color.getText().toString());
                if (a > 0) {
                    tv_color.setText(String.valueOf(a - 1));
                }
                break;
            case R.id.btn_color_increment:
                a = Integer.valueOf(tv_color.getText().toString());
                tv_color.setText(String.valueOf(a + 1));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    void UploadPdfToFirebase(Uri pdfUri) {

        if (pdfUri != null) {

            Toast.makeText(getActivity(), "Please Wait a moment.", Toast.LENGTH_SHORT).show();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            final String formattedDate = df.format(c);
            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            final String username = sharedpreferences.getString("uname", "");
            a = username;
            StorageReference sRef = mStorageReference.child("RequestPrintAttachment/" + username + "_" + str_numpage + "_" + str_color_count + ".pdf");
            uploadTask = sRef.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @SuppressWarnings("VisibleForTests")
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uri.isComplete()) ;
                    Uri url = uri.getResult();
                    UploadPDF upload = new UploadPDF(url.toString(), str_rb_spiral, str_rb_side, str_rb_bound, str_bw_count, str_cb_pagesize, str_color_count, str_numpage, str_note, username, String.valueOf(int_total));
                    UploadPDF Userupload = new UploadPDF(url.toString(), str_rb_spiral, str_rb_side, str_rb_bound, str_bw_count, str_cb_pagesize, str_color_count, str_numpage, str_note, username, String.valueOf(int_total), Str_shopkeeperNumber, shopName, formattedDate);

                    mDatabaseReference.child(ShopInformationBottomActivity.Str_shopkeeperNumber).child(username + "_" + str_numpage + "_" + str_color_count).setValue(upload);
                    mUserreference.child(username).child("Print" + "_" + str_numpage + "_" + str_color_count).setValue(Userupload);

                    Toast.makeText(getActivity(), "Request Sent to " + shopName + ", You will get their acknowledgment soon !!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getActivity(), "Uploading fail", Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @SuppressWarnings("VisibleForTests")
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                }
            });

        } else {
//            Toast.makeText(getActivity(), "select a file", Toast.LENGTH_SHORT).show();
        }
    }

    private void SubmitPrint() {
        int k = 0;

        if (et_NumofPages.getText().toString().equals("0") || et_NumofPages.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Give approximate page count of document", Toast.LENGTH_SHORT).show();
            k = 1;
            return;
        } else {
            str_numpage = et_NumofPages.getText().toString();
        }
        if (tv_setAttachmentPath.getText().toString().equals("Set Your Path Here")) {
            Toast.makeText(getActivity(), "Please Select file", Toast.LENGTH_SHORT).show();
            k = 1;
            return;
        }
        try {
            str_rb_spiral = getCheckedRbSpiral();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Select Spiral", Toast.LENGTH_SHORT).show();
            k = 1;
            return;
        }
        try {
            str_rb_side = getCheckedRbSide();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Select Side", Toast.LENGTH_SHORT).show();
            k = 1;
            return;
        }
        try {
            str_rb_bound = getCheckedRbBound();
            if (str_rb_bound.equals("Yes")) {
                str_rb_bound = str_doBound;
            }

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Select Bound", Toast.LENGTH_SHORT).show();
            k = 1;
            return;
        }
        if (!cb_color.isChecked() && !cb_bW.isChecked()) {
            k = 1;
            Toast.makeText(getActivity(), "Please Select Type of print", Toast.LENGTH_SHORT).show();
            return;
        } else {
            str_bw_count = "0";
            str_color_count = "0";

            if (cb_color.isChecked()) {
                if (tv_color.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), "Please Select count for Color print", Toast.LENGTH_SHORT).show();
                    k = 1;
                    return;
                } else {
                    str_color_count = tv_color.getText().toString();

                }
            }

            if (cb_bW.isChecked()) {
                if (tv_bw.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), "Please Select count for B&W print", Toast.LENGTH_SHORT).show();
                    k = 1;
                    return;
                } else {
                    str_bw_count = tv_bw.getText().toString();

                }
            }
        }
        if (!cb_A2.isChecked() && !cb_A4.isChecked() && !cb_A3.isChecked() && !cb_A1.isChecked() && !cb_page.isChecked()) {
            Toast.makeText(getActivity(), "Please give page size", Toast.LENGTH_SHORT).show();
            k = 1;
            return;
        } else {
            str_cb_pagesize = "";
            if (cb_A1.isChecked()) {
                str_cb_pagesize += cb_A1.getText().toString() + " ";
            }
            if (cb_A2.isChecked()) {
                str_cb_pagesize += cb_A2.getText().toString() + " ";
            }
            if (cb_A3.isChecked()) {
                str_cb_pagesize += cb_A3.getText().toString() + " ";
            }
            if (cb_A4.isChecked()) {
                str_cb_pagesize += cb_A4.getText().toString() + " ";
            }
            if (cb_page.isChecked()) {
                str_cb_pagesize += cb_page.getText().toString() + " ";
            }
        }

        if (et_NoteToSend.getText().toString().equals("")) {
            str_note = "No Note";
        } else {
            str_note = et_NoteToSend.getText().toString();
        }
        if (k != 1) {

            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getActivity(), "Uploading", Toast.LENGTH_LONG).show();
            } else {
                SendPrintRequest();

            }

        }
    }


//    @Override
//    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        Toast.makeText(getActivity(), str_spinnerList.get(position), Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> arg0) {
//        Toast.makeText(getActivity(), "nothing", Toast.LENGTH_SHORT).show();
//    }

    private void SendPrintRequest() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(true);

        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog, null);
        dialog.setContentView(view);

        countTotal();
        LinearLayout ll_totalpage = view.findViewById(R.id.ll_totalpage);
        TextView tv_cd_amountTopay = view.findViewById(R.id.tv_cd_amountTopay);
        TextView tv_cd_doc = view.findViewById(R.id.tv_cd_doc);
        TextView tv_cd_note = view.findViewById(R.id.tv_cd_note);
        Button edit = view.findViewById(R.id.btn_cancel_popup);
        TextView tv_cd_total = view.findViewById(R.id.tv_cd_total);
        Button delete = view.findViewById(R.id.btn_delete_data);
        TextView tv_cd_pageNumber = view.findViewById(R.id.tv_cd_pageNumber);
        TextView tv_cd_spiral = view.findViewById(R.id.tv_cd_spiral);
        TextView tv_cd_side = view.findViewById(R.id.tv_cd_side);
        TextView tv_cd_bound = view.findViewById(R.id.tv_cd_bound);
        TextView tv_cd_typeNnumber = view.findViewById(R.id.tv_cd_typeNnumber);
        TextView tv_cd_pageSize = view.findViewById(R.id.tv_cd_pageSize);

        ll_totalpage.setVisibility(View.VISIBLE);
        delete.setText("Proceed to checkout");
        tv_cd_total.setText(String.valueOf(count));
        tv_cd_bound.setText(str_rb_bound);
        if (et_NoteToSend.getText().toString().equals("")) {
            tv_cd_note.setText("No Note");
        } else {
            tv_cd_note.setText(et_NoteToSend.getText().toString());
        }
        tv_cd_amountTopay.setText(String.valueOf(int_total) + " INR");

        tv_cd_pageNumber.setText(str_numpage);
        tv_cd_pageSize.setText(str_cb_pagesize);
        tv_cd_side.setText(str_rb_side);
        tv_cd_spiral.setText(str_rb_spiral);
        if (cb_bW.isChecked() && cb_color.isChecked()) {
            tv_cd_typeNnumber.setText(str_bw_count + " B&W Print and " + str_color_count + " Color Print");
        } else if (cb_bW.isChecked()) {
            tv_cd_typeNnumber.setText(str_bw_count + " B&W Print");
        } else if (cb_color.isChecked()) {
            tv_cd_typeNnumber.setText(str_color_count + " Color Print");
        }
        tv_cd_doc.setText(pdfUri.getLastPathSegment());
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
//                UploadPdfToFirebase(pdfUri);
                Toast.makeText(getActivity(), "Towards Payment...", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(getActivity(), OnlinePaymentActivity.class), 10001);

            }
        });
        dialog.show();
    }

    private void countTotal() {
        int int_numPage = 0, color = 0, bw = 0;
        int_total = 0;
        int_numPage = Integer.valueOf(et_NumofPages.getText().toString());
        if (str_rb_side.equals("One Side")) {
            count = int_numPage;

        } else {
            if (Integer.valueOf(et_NumofPages.getText().toString()) % 2 == 0) {
                count = Integer.valueOf(et_NumofPages.getText().toString()) / 2;
            } else {
                count = (Integer.valueOf(et_NumofPages.getText().toString()) / 2) + 1;
            }
        }

        if (cb_page.isChecked()) {
            if (Integer.valueOf(str_bw_count) > 0) {
                if (Integer.valueOf(str_bw_count) > 0 || int_numPage < 5) {
                    bw += Math.round(Float.parseFloat(String.valueOf(Integer.valueOf(str_bwps))) * 0.01 * int_numPage);
                    if (bw < 1) {
                        bw = 1;
                    }
                } else {
                    bw += Math.round(Float.parseFloat(String.valueOf(Integer.valueOf(str_bwps))) * 0.01 * int_numPage);
                }
            }

            if (Integer.valueOf(str_color_count) > 0) {
                if (Integer.valueOf(str_color_count) > 0 || int_numPage < 5) {
                    color += Math.round(Float.parseFloat(str_cps) * 0.01 * int_numPage);
                    if (color < 1) {
                        color = 1;
                    }

                } else {
                    color += Math.round(Float.parseFloat(str_cps) * 0.01 * int_numPage);
                }
            }
        }
        if (!"No".equals(str_rb_bound)) {
            if (cb_color.isChecked()) {
                color += int_bound;
            }
            if (cb_bW.isChecked()) {
                bw += int_bound;
            }
        }
        if (!"No".equals(str_rb_spiral)) {
            if (cb_bW.isChecked()) {
                if (int_numPage < 36) {
                    bw += Integer.parseInt(str_ss);
                } else if (int_numPage < 71) {
                    bw += Integer.parseInt(str_ms);
                } else {
                    bw += Integer.parseInt(str_ls);
                }
            }
            if (cb_color.isChecked()) {
                if (int_numPage < 36) {
                    color += Integer.parseInt(str_ss);
                } else if (int_numPage < 71) {
                    color += Integer.parseInt(str_ms);
                } else {
                    color += Integer.parseInt(str_ls);
                }
            }
        }
        if (cb_A1.isChecked()) {
            if (cb_color.isChecked()) {
                color += Integer.parseInt(str_a1) * int_numPage;
            }
            if (cb_bW.isChecked()) {
                bw += Integer.parseInt(str_a1) * int_numPage;
            }
        }
        if (cb_A2.isChecked()) {
            if (cb_color.isChecked()) {
                color += Integer.parseInt(str_a2) * int_numPage;
            }
            if (cb_bW.isChecked()) {
                bw += Integer.parseInt(str_a2) * int_numPage;
            }
        }
        if (cb_A3.isChecked()) {
            if (cb_bW.isChecked()) {
                bw += Integer.parseInt(str_a3) * int_numPage;
            }
            if (cb_color.isChecked()) {
                color = Integer.parseInt(str_a3) * int_numPage;
            }
        }
        if (cb_A4.isChecked()) {
            if (cb_color.isChecked()) {
                color += Integer.parseInt(str_a4) * int_numPage;
            }
            if (cb_bW.isChecked()) {
                bw += Integer.parseInt(str_a4) * int_numPage;
            }
        }


        if (cb_bW.isChecked() && cb_color.isChecked()) {

            if (Integer.valueOf(str_bw_count) > 0 && Integer.valueOf(str_color_count) > 0) {
                int_total = bw * Integer.valueOf(str_bw_count) + color * Integer.valueOf(str_color_count);
            }

        } else if (cb_bW.isChecked()) {
            if ((Integer.parseInt(str_bw_count)) > 0) {
                int_total = bw * Integer.parseInt(str_bw_count);
            }
        } else if (cb_color.isChecked()) {
            if ((Integer.parseInt(str_color_count)) > 0) {
                int_total = color * Integer.parseInt(str_color_count);
            }
        }


    }

    private String getCheckedRbBound() {
        String str = " ";
        int_rb_bound = rg_type_bind.getCheckedRadioButtonId();
        rb_bound = getView().findViewById(int_rb_bound);
        str = rb_bound.getText().toString();
        return str;
    }

    private String getCheckedRbSide() {
        String str1 = " ";
        int_rb_side = rg_side.getCheckedRadioButtonId();
        rb_side = getView().findViewById(int_rb_side);
        str1 = rb_side.getText().toString();

        return str1;
    }

    private String getCheckedRbSpiral() {
        String str_spiral = " ";
        int_rb_spiral = rg_spiral.getCheckedRadioButtonId();
        rb_spiral_bw = getView().findViewById(int_rb_spiral);
        str_spiral = rb_spiral_bw.getText().toString();
        return str_spiral;
    }

    private void SelectFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
            startActivity(intent);
            return;
        }
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT); //to fetch files
        startActivityForResult(Intent.createChooser(intent, "Select file"), 86);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 10001) && (resultCode == 1)) {
            // recreate your fragment here
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(new PrintFragment()).attach(new PrintFragment()).commit();
            UploadPdfToFirebase(pdfUri);


        } else if ((requestCode == 15) && (resultCode == 12)) {
            // recreate your fragment here
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(new PrintFragment()).attach(new PrintFragment()).commit();
//            Toast.makeText(getActivity(), ">>>>>>>" + String.valueOf(str_a4), Toast.LENGTH_SHORT).show();
            if (!"".equals(str_a1)) {
                cb_A1.setVisibility(View.VISIBLE);
            }
            if (!"".equals(str_a2)) {
                cb_A2.setVisibility(View.VISIBLE);
            }
            if (!"".equals(str_a3)) {
                cb_A3.setVisibility(View.VISIBLE);
            }
            if (!"".equals(str_a4)) {
                cb_A4.setVisibility(View.VISIBLE);
            }

            et_NumofPages.setText(String.valueOf(PdfShowActivity.PG));
        } else if (requestCode == 86 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData(); //uri of selected file
            String[] segments = pdfUri.getPath().split("/");
            idStr = segments[segments.length - 1];
            tv_setAttachmentPath.setText(idStr);
            Intent i = new Intent(getActivity(), PdfShowActivity.class);
            i.putExtra("uri", pdfUri.toString());
//            startActivity(i);
            startActivityForResult(i, 15);

        } else {
            Toast.makeText(getActivity(), "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSMS() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
//            case 0:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage("9913106087", "7698071241", "Your Request for Print is received", null, null);
//                    Toast.makeText(getActivity(), "Request Sent", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), "SMS Failed", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                break;
            case 9:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SelectFile();
                } else {
                    Toast.makeText(getActivity(), "Please Provide Permission For Reading Storage", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_hardbound) {

            ll_spinner.setVisibility(View.VISIBLE);
            Spinner_string();
            spinner_bound.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), str_spinnerList.get(position), Toast.LENGTH_SHORT).show();
                    str_doBound = str_spinnerList.get(position);
                    int_bound = int_boundPrice.get(position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, str_spinnerList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_bound.setAdapter(adapter);


        } else {
            ll_spinner.setVisibility(View.GONE);
        }
    }
}

//                openPdf(getActivity(),pdfUri.getPath());
//
//                try {
//
//
//                    final String docId = DocumentsContract.getDocumentId(pdfUri);
//                    final String[] split = docId.split(":");
//                    final String type = split[0];
//
//                    String path = null;
//                    if ("primary".equalsIgnoreCase(type)) {
//                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
//                    }
//                    File file = new File(path);
//                    if (file.exists()) {
//                        Toast.makeText(getContext(), "file do exists", Toast.LENGTH_SHORT).show();
//                        Intent intent1 = new Intent(Intent.ACTION_VIEW);
//                        intent1.setDataAndType(Uri.fromFile(file), "application/pdf");
//                        PackageManager pm = getContext().getPackageManager();
//                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//                        sendIntent.setType("application/pdf");
//                        Intent openInChooser = Intent.createChooser(intent1, "Choose");
//                        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
//                        if (resInfo.size() > 0) {
//                            try {
//                                getContext().startActivity(openInChooser);
//                            } catch (Throwable throwable) {
//                                Toast.makeText(getContext(), "PDF apps are not installed", Toast.LENGTH_SHORT).show();
//                                // PDF apps are not installed
//                            }
//                        } else {
//                            Toast.makeText(getContext(), "PDF apps are not installed", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(getContext(), "no file", Toast.LENGTH_SHORT).show();
//                    }
//                    Toast.makeText(getActivity(), "fsd", Toast.LENGTH_SHORT).show();
//                }
//                catch (Exception e){
//                    Toast.makeText(getActivity(), "Select a file to appear", Toast.LENGTH_SHORT).show();
//                }

//opening in fragment
//                Toast.makeText(getActivity(), "hh", Toast.LENGTH_SHORT).show();
//                OpenPdfFragment pdfFragment = new OpenPdfFragment();
//                Bundle args = new Bundle();
//                final String docId = DocumentsContract.getDocumentId(pdfUri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                String path = null;
//                if ("primary".equalsIgnoreCase(type)) {
//                    path =  Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//                Toast.makeText(getActivity(), path, Toast.LENGTH_SHORT).show();
////                args.putString("pdf", tv_setAttachmentPath.getText().toString());
//                args.putString("pdf",path);
//                Log.i(">>>>>>>>>>>>",tv_setAttachmentPath.getText().toString());
//                pdfFragment.setArguments(args);
//                getFragmentManager().beginTransaction().add(R.id.nav_host_fragment, pdfFragment).addToBackStack(null).commit();


//    private void pageCount(Uri pdfUri) {
////        try {
//
//
//            ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(new File(pdfUri.getPath()), ParcelFileDescriptor.MODE_READ_ONLY);
//            PdfRenderer pdfRenderer = null;
////            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                int totalpages;
//                pdfRenderer = new PdfRenderer(parcelFileDescriptor);
//                totalpages = pdfRenderer.getPageCount();
//                Toast.makeText(getActivity(), ""+totalpages, Toast.LENGTH_SHORT).show();
////            }
////        } catch (FileNotFoundException e) {
////            Toast.makeText(getActivity(), "xb", Toast.LENGTH_SHORT).show();
////        }
//        }


//    private void UploadPDFFile(Uri data) {
//        if(data!=null) {
//            storageReference = FirebaseStorage.getInstance().getReference();
//            databaseReference = FirebaseDatabase.getInstance().getReference();
//            StorageReference reference = storageReference.child("UploadPDFFile/" + System.currentTimeMillis() + ".pdf");
//            reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
//                    while (!uri.isComplete()) ;
//                    Uri url = uri.getResult();
//
//                    UploadPDF uploadPDF = new UploadPDF(url.toString());
//                    databaseReference.child(databaseReference.push().getKey()).setValue(uploadPDF);
//                    Toast.makeText(getActivity(), "File Uploaded", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getActivity(), "Sending failed", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//        else{
//            Toast.makeText(getActivity(), "Please select document tp print", Toast.LENGTH_SHORT).show();
//        }
//    }


//    public void upload() {
//        if (pdfUri != null) {
//
//            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//            String username = sharedpreferences.getString("uname", "");
//
//            uploadPDFde.setUrl(pdfUri.toString().trim());
//            databaseReference1.child(username + "_" ).setValue(uploadPDFde);
//            final String key = databaseReference1.push().getKey();
//            final StorageReference ref = storageReference1.child(key + ".pdf");
//
//            uploadTask = ref.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(getActivity(), "Uploaded Successfully!!", Toast.LENGTH_SHORT).show();
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getActivity(), "Error in Upload", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//
//                }
//            });
//        } else {
//            Toast.makeText(getActivity(), "No File Selected", Toast.LENGTH_SHORT).show();
//        }
//    }
//    private void uploadpdf1(Uri pdfUri) {
//        if(pdfUri!=null){
//            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//            String username = sharedpreferences.getString("uname", "");
//
//            uploadPDFde.setUrl(pdfUri.toString());
//            databaseReference1.child(username+"_").setValue(uploadPDFde);
//            final  StorageReference ref = storageReference1.child(databaseReference1.push().getKey()+".pdf");
//            ref.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//        else
//        {
//            Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
//        }
//    }
