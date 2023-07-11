package com.dhruvi.dhruvisonani.usersidexa2.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dhruvi.dhruvisonani.usersidexa2.Fragment.AvailableBookFragment;
import com.dhruvi.dhruvisonani.usersidexa2.Fragment.PrintFragment;
import com.dhruvi.dhruvisonani.usersidexa2.Fragment.XeroxFragment;
import com.dhruvi.dhruvisonani.usersidexa2.JavaClass.Constants;
import com.dhruvi.dhruvisonani.usersidexa2.JavaClass.Paytm;
import com.dhruvi.dhruvisonani.usersidexa2.R;
import com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity;
import com.google.firebase.firestore.DocumentReference;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.dhruvi.dhruvisonani.usersidexa2.Activity.LoginSignUpActivity.MyPREFERENCES;
import static com.dhruvi.dhruvisonani.usersidexa2.Fragment.PrintFragment.int_total;

public class OnlinePaymentActivity extends AppCompatActivity implements PaymentResultListener, PaytmPaymentTransactionCallback {

    private static final String TAG = "";
    TextView et_MobileNUmber, et_name, et_amount;
    Button btn_payWithUpi, btn_payWithPaytm, btn_payWithRazerpay;

    String str_name, str_upiId, str_note, str_amount;
    final int UPI_PAYMENT = 0;
    public static int payment = 0;

    DocumentReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_payment);

        Checkout.preload(getApplicationContext());
        if (ContextCompat.checkSelfPermission(OnlinePaymentActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OnlinePaymentActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

        declaration();
        initialization();
    }

    public void declaration() {

        et_name = findViewById(R.id.et_name);
        et_amount = findViewById(R.id.et_amount);
        et_MobileNUmber = findViewById(R.id.et_MobileNUmber);
        btn_payWithUpi = findViewById(R.id.btn_payWithUpi);
        btn_payWithPaytm = findViewById(R.id.btn_payWithPaytm);
        btn_payWithRazerpay = findViewById(R.id.btn_payWithRazerpay);
        btn_payWithPaytm.setVisibility(View.GONE);
    }

    public void initialization() {
        SharedPreferences sharedpreferences = OnlinePaymentActivity.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String username = sharedpreferences.getString("uname", "");
//        databaseReference = FirebaseFirestore.getInstance().collection("Users").document(username);
//
//        databaseReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    return;
//                }
//                if (snapshot != null && snapshot.exists()) {
//                    et_name.setText(snapshot.getString("Full Name"));
//                    et_MobileNUmber.setText(ShopInformationBottomActivity.Str_shopkeeperNumber);
//
//
//                    et_amount.setText(String.valueOf(int_total));
//                    if (et_amount.getText().toString().equals("0")) {
////                        String[] aa = AvailableBookFragment.MyViewHolderForAvailableBook.tv_bookPrice.getText().toString().split(":");
//                        et_amount.setText(String.valueOf(AvailableBookFragment.b));
//                        if (et_amount.getText().toString().equals("0")) {
//                            et_amount.setText(String.valueOf(XeroxFragment.int_total));
//                        }
//                    }
//
//                }
//            }
//        });

                    et_name.setText(ShopInformationBottomActivity.str_shopkeeperName);
                    et_MobileNUmber.setText(ShopInformationBottomActivity.Str_shopkeeperNumber);


                    et_amount.setText(String.valueOf(int_total));
                    if (et_amount.getText().toString().equals("0")) {
//                        String[] aa = AvailableBookFragment.MyViewHolderForAvailableBook.tv_bookPrice.getText().toString().split(":");
                        et_amount.setText(String.valueOf(AvailableBookFragment.b));
                        if (et_amount.getText().toString().equals("0")) {
                            et_amount.setText(String.valueOf(XeroxFragment.int_total));
                        }
                    }

        btn_payWithPaytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_amount.getText().toString()) && !TextUtils.isEmpty(et_name.getText().toString()) && !TextUtils.isEmpty(et_MobileNUmber.getText().toString())) {
//                    paytm();
                } else {
                    Toast.makeText(OnlinePaymentActivity.this, "Fill empty fields !!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_payWithRazerpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_amount.getText().toString()) && !TextUtils.isEmpty(et_name.getText().toString()) && !TextUtils.isEmpty(et_MobileNUmber.getText().toString())) {

                    Razerpay();
                } else {
                    Toast.makeText(OnlinePaymentActivity.this, "Something went wrong !!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_payWithUpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_amount.getText().toString()) && !TextUtils.isEmpty(et_name.getText().toString()) && !TextUtils.isEmpty(et_MobileNUmber.getText().toString())) {


                    str_amount = et_amount.getText().toString();
                    str_name = et_name.getText().toString();

                    str_upiId = et_MobileNUmber.getText().toString();

                    payUsingApi(str_amount, str_name, str_upiId);
                } else {
                    Toast.makeText(OnlinePaymentActivity.this, "Something went wrong !!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Razerpay() {
        startPayment();
    }


    private void payUsingApi(String str_amount, String str_name, String str_upiId) {

        Uri uri = Uri.parse("upi://pay").buildUpon().appendQueryParameter("pa", str_upiId).appendQueryParameter("pn", str_name).appendQueryParameter("pa", str_amount).appendQueryParameter("cu", "INR").build();

        Intent upi_payment = new Intent(Intent.ACTION_VIEW);
        upi_payment.setData(uri);

        Intent chooser = Intent.createChooser(upi_payment, "Pay With");
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(this, "No UPI Applocation Found in Devide", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String txt = data.getStringExtra("response");
                        Log.d("UPI ", "onActivityResult" + txt);
                        ArrayList<String> datalist = new ArrayList<>();
                        datalist.add("Nothing");
                        upiPaymentOperation(datalist);
                    } else {
                        Log.d("UPI", "onActivityResult : " + "Return data is null");
                        ArrayList<String> datalist = new ArrayList<>();
                        datalist.add("Nothing");
                        upiPaymentOperation(datalist);
                    }
                } else {
                    Log.d("UPI", "onActivityResult : " + "Return data is null");
                    ArrayList<String> datalist = new ArrayList<>();
                    datalist.add("Nothing");
                    upiPaymentOperation(datalist);
                }
                break;
        }
    }

    private void upiPaymentOperation(ArrayList<String> data) {
        if (isConnectionAvailable(OnlinePaymentActivity.this)) {
            String str = data.get(0);
            Log.d("UPI PAY", "Operation  : " + str);
            String PaymentCancel = "";

            if (str == null) {
                str = "Discard";
            }
            String status = "";
            String approvalRef = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalSTR[] = response[i].split("=");
                if (equalSTR.length >= 2) {
                    if (equalSTR[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalSTR[1].toLowerCase();
                    } else if (equalSTR[0].toLowerCase().equals("approval Ref".toLowerCase()) || equalSTR[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRef = equalSTR[1];


                    }
                } else {
                    PaymentCancel = "You've canecel payment!!";
                    if (status.equals("Success")) {
                        Toast.makeText(this, "Transaction Successful", Toast.LENGTH_SHORT).show();
                        Log.d("UPI", "responsestr : " + approvalRef);

                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new PrintFragment()).addToBackStack(null).commit();

                    } else if ("Payment Cancel By User !!".equals(PaymentCancel)) {
                        Toast.makeText(this, "Payment Cancel By User !!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            Toast.makeText(this, "No Internet Connection !!", Toast.LENGTH_SHORT).show();
        }
    }

//    private void Upload() {
//        if (pdfUri != null) {
//            Date c = Calendar.getInstance().getTime();
//            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//            final String formattedDate = df.format(c);
//
//
////            Toast.makeText(getActivity(), "Please wait, request in sending", Toast.LENGTH_SHORT).show();
//            SharedPreferences sharedpreferences = OnlinePaymentActivity.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//            final String username = sharedpreferences.getString("uname", "");
//            StorageReference sRef = mStorageReference.child("RequestPrintAttachment/" + username + "_" + str_numpage + "_" + str_color_count + ".pdf");
//            uploadTask = sRef.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @SuppressWarnings("VisibleForTests")
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
//                    while (!uri.isComplete()) ;
//                    Uri url = uri.getResult();
//                    UploadPDF upload = new UploadPDF(url.toString(), str_rb_spiral, str_rb_side, str_rb_bound, str_bw_count, str_cb_pagesize, str_color_count, str_numpage, str_note, username, String.valueOf(int_total));
//                    UploadPDF Userupload = new UploadPDF(url.toString(), str_rb_spiral, str_rb_side, str_rb_bound, str_bw_count, str_cb_pagesize, str_color_count, str_numpage, str_note, username, String.valueOf(int_total),Str_shopkeeperNumber,shopName,formattedDate);
//
//                    mDatabaseReference.child(ShopInformationBottomActivity.Str_shopkeeperNumber).child(username + "_" + str_numpage + "_" + str_color_count).setValue(upload);
//                    mUserreference.child(username).child("Print" + "_" + str_numpage + "_" + str_color_count).setValue(Userupload);
//
////                    Toast.makeText(getClass(), "File Upload successfull", Toast.LENGTH_SHORT).show();
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
////                    Toast.makeText(getActivity(), "Uploading fail", Toast.LENGTH_SHORT).show();
//
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @SuppressWarnings("VisibleForTests")
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                }
//            });
//
//        } else {
////            Toast.makeText(getActivity(), "select a file", Toast.LENGTH_SHORT).show();
//        }
//    }

    private boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() && networkInfo.isConnectedOrConnecting() && networkInfo.isAvailable()) {
                return true;
            }

        }
        return false;
    }

//    private void paytm() {
//        //getting the tax amount first.
//        str_amount = et_amount.getText().toString();
//        str_name = et_name.getText().toString();
//        str_upiId = et_MobileNUmber.getText().toString();
//
//        String txnAmount = et_amount.getText().toString().trim();
//
//        //creating a retrofit object.
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
//
//        //creating the retrofit api service
//        Api apiService = retrofit.create(Api.class);
//
//        //creating paytm object
//        //containing all the values required
//        final Paytm paytm = new Paytm(Constants.M_ID, Constants.CHANNEL_ID, txnAmount, Constants.WEBSITE, Constants.CALLBACK_URL, Constants.INDUSTRY_TYPE_ID);
//
//        //creating a call object from the apiService
//        Call<Checksum> call = apiService.getChecksum(paytm.getmId(), paytm.getOrderId(), paytm.getCustId(), paytm.getChannelId(), paytm.getTxnAmount(), paytm.getWebsite(), paytm.getCallBackUrl(), paytm.getIndustryTypeId());
//
//        //making the call to generate checksum
//        call.enqueue(new Callback<Checksum>() {
//            @Override
//            public void onResponse(Call<Checksum> call, Response<Checksum> response) {
//
//                //once we get the checksum we will initiailize the payment.
//                //the method is taking the checksum we got and the paytm object as the parameter
//                initializePaytmPayment(response.body().getChecksumHash(), paytm);
//            }
//
//            @Override
//            public void onFailure(Call<Checksum> call, Throwable t) {
//
//            }
//        });
//    }

    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getStagingService();

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(this, true, true, this);

    }

    //all these overriden method is to detect the payment result accordingly
    @Override
    public void onTransactionResponse(Bundle bundle) {

        Toast.makeText(this, bundle.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Successfulll by razorpay", Toast.LENGTH_SHORT).show();
        payment = 1;
        setResult(1);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Errror  : " + s, Toast.LENGTH_SHORT).show();
    }

    // Razorpay
    public void startPayment() {
        str_amount = et_amount.getText().toString();
        str_name = et_name.getText().toString();
        str_upiId = et_MobileNUmber.getText().toString();

        Checkout checkout = new Checkout();
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();
            options.put("name", str_name);
            options.put("description", str_note);
            options.put("currency", "INR");
            int i = Integer.valueOf(str_amount) * 100;
            options.put("amount", String.valueOf(i));
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }
}

// PayTm
//https://github.com/Sameer-Jani-201/PayTMDemo/blob/master/app/libs/PGSDK_V2.1.jar
//    Test Merchant ID        TevleX02760798318789
//    Test Merchant Key        j%70NJSNJ&Q#VcF5
//        Website        WEBSTAGING
//        Industry Type        Retail
//        Channel ID (For Website)        WEB
//        Channel ID (For Mobile Apps)          WAP
//        Transaction URL        https://securegw-stage.paytm.in/order/process
//        Transaction Status URL https://securegw-stage.paytm.in/order/status

