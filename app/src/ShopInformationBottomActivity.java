package com.dhruvi.dhruvisonani.usersidexa2;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dhruvi.dhruvisonani.usersidexa2.Activity.LoginSignUpActivity;
import com.dhruvi.dhruvisonani.usersidexa2.Fragment.EditProfileFragment;
import com.dhruvi.dhruvisonani.usersidexa2.Fragment.HistoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ShopInformationBottomActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth auth;
    DocumentReference databaseReference;

    public static String str_shopkeeperName, Str_shopkeeperNumber, Str_shopkeeperEmailId, Str_shopkeeperAddress, Str_shopkeeperGender, Str_shopkeeperTime;
    public static String str_cps, str_bwps, str_cxs, str_bwxs, str_ss, str_ms, str_ls, str_a4, str_a3, str_a2, str_a1, str_ssb, str_sseb;
    public static String str_cb, str_jb, str_ciwb, str_pb, str_hard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_information_bottom);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        databaseReference = db.collection("Shopkeeper").document(LoginSignUpActivity.shopName);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_print, R.id.navigation_availableBook, R.id.navigation_xerox).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        Showinfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_Edit_profile:
//                Toast.makeText(this, "Edit Profile", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new EditProfileFragment()).addToBackStack(null).commit();
                return true;
            case R.id.action_history:
//                Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HistoryFragment()).addToBackStack(null).commit();
                return true;
            case R.id.action_rate:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent gotoMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(gotoMarket);
                Toast.makeText(this, "Rate", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_aboutShop:
                AboutShop();
                return true;
            case R.id.action_feedback:
                Showinfo();
                return true;

            case R.id.action_logout:
                auth.getInstance().signOut();
                finishAffinity();
                startActivity(new Intent(ShopInformationBottomActivity.this, LoginSignUpActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void AboutShop() {
        final TextView tv_shop_Name, tv_cd_keeperNAme, tv_cd_mailId, tv_cd_gender, tv_cd_address, tv_cd_contactNum, tv_cd_timing;
        final Button btn_cd_ok;

        final Dialog dialogInfoshop = new Dialog(ShopInformationBottomActivity.this);
        dialogInfoshop.setCancelable(true);
        View viewShop = ShopInformationBottomActivity.this.getLayoutInflater().inflate(R.layout.custom_about_shop, null);
        dialogInfoshop.setContentView(viewShop);
        dialogInfoshop.setCanceledOnTouchOutside(false);

        tv_cd_address = viewShop.findViewById(R.id.tv_cd_address);
        tv_shop_Name = viewShop.findViewById(R.id.tv_shop_Name);
        tv_cd_keeperNAme = viewShop.findViewById(R.id.tv_cd_keeperNAme);
        tv_cd_contactNum = viewShop.findViewById(R.id.tv_cd_contactNum);
        tv_cd_mailId = viewShop.findViewById(R.id.tv_cd_mailId);
        tv_cd_gender = viewShop.findViewById(R.id.tv_cd_gender);
        btn_cd_ok = viewShop.findViewById(R.id.btn_cd_ok);
        tv_cd_timing = viewShop.findViewById(R.id.tv_cd_timing);

        tv_cd_address.setText(Str_shopkeeperAddress);
        tv_shop_Name.setText(LoginSignUpActivity.shopName);
        tv_cd_keeperNAme.setText(str_shopkeeperName);
        tv_cd_timing.setText(Str_shopkeeperTime);
        tv_cd_contactNum.setText(Str_shopkeeperNumber);
        tv_cd_mailId.setText(Str_shopkeeperEmailId);
        tv_cd_gender.setText(Str_shopkeeperGender);
        tv_cd_timing.setText(Str_shopkeeperTime);

        btn_cd_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInfoshop.dismiss();
            }
        });
        dialogInfoshop.show();
    }

    public void Showinfo() {

        final Dialog dialogInfo = new Dialog(ShopInformationBottomActivity.this);
        dialogInfo.setCancelable(true);

        View view = ShopInformationBottomActivity.this.getLayoutInflater().inflate(R.layout.custom_price_dialog, null);
        dialogInfo.setContentView(view);

        final TextView tv_cd_cxps = view.findViewById(R.id.tv_cd_cxps);
        final TextView tv_cd_sadleStiching = view.findViewById(R.id.tv_cd_sadleStiching);
        final TextView tv_cd_sectionSewn = view.findViewById(R.id.tv_cd_sectionSewn);
        final TextView tv_cd_Coptic = view.findViewById(R.id.tv_cd_Coptic);
        final TextView tv_cd_Japanese = view.findViewById(R.id.tv_cd_Japanese);
        final TextView tv_cd_CasedInWiro = view.findViewById(R.id.tv_cd_CasedInWiro);
        final TextView tv_cd_pamphlet = view.findViewById(R.id.tv_cd_pamphlet);
        final TextView tv_shopName = view.findViewById(R.id.tv_shopName);
        final TextView tv_cd_cpp = view.findViewById(R.id.tv_cd_cpp);
        final TextView tv_cd_BWpp = view.findViewById(R.id.tv_cd_BWpp);
        final TextView tv_cd_bwxps = view.findViewById(R.id.tv_cd_bwxps);
        Button btn_info_ok = view.findViewById(R.id.btn_info_ok);
        final TextView tv_cd_ss = view.findViewById(R.id.tv_cd_ss);
        final TextView tv_cd_ms = view.findViewById(R.id.tv_cd_ms);
        final TextView tv_cd_ls = view.findViewById(R.id.tv_cd_ls);
        final TextView tv_cd_A4 = view.findViewById(R.id.tv_cd_A4);
        final TextView tv_cd_A3 = view.findViewById(R.id.tv_cd_A3);
        final TextView tv_cd_A2 = view.findViewById(R.id.tv_cd_A2);
        final TextView tv_cd_A1 = view.findViewById(R.id.tv_cd_A1);
        final TextView tv_cd_hardbound = view.findViewById(R.id.tv_cd_hardbound);


        tv_shopName.setText(LoginSignUpActivity.shopName);

        dialogInfo.setCanceledOnTouchOutside(false);
        databaseReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    str_shopkeeperName = snapshot.getString("Full Name");
                    Str_shopkeeperTime = snapshot.getString("Shop Time");
                    Str_shopkeeperNumber = snapshot.getString("Mobile Number");
                    Str_shopkeeperEmailId = snapshot.getString("Email Id");
                    Str_shopkeeperAddress = snapshot.getString("Address");
                    Str_shopkeeperGender = snapshot.getString("Gender");
                    str_ssb = snapshot.getString("et_sadleStiching");
                    str_cb = snapshot.getString("et_Coptic");
                    str_ls = snapshot.getString("et_LargeSpiral");
                    str_sseb = snapshot.getString("et_sectionSewn");
                    str_bwxs = snapshot.getString("et_BWxeroxPrice");
                    str_jb = snapshot.getString("et_Japanese");
                    str_ms = snapshot.getString("et_MeduimSpiral");
                    str_bwps = snapshot.getString("BW Print Price");
                    str_pb = snapshot.getString("et_Pamphlet");
                    str_ss = snapshot.getString("et_smallSpiral");

                    str_ciwb = snapshot.getString("et_CasedInWiro");
                    str_cxs = snapshot.getString("Color Xerox Price");
                    str_cps = snapshot.getString("Color Print Price");
                    str_hard = snapshot.getString("et_hard");

                    if (snapshot.getString("et_sadleStiching").equals("")) {
                        tv_cd_sadleStiching.setText("No");
                    } else {
                        tv_cd_sadleStiching.setText(snapshot.getString("et_sadleStiching") + " INR");
                    }
                    if (snapshot.getString("et_Coptic").equals("")) {
                        tv_cd_Coptic.setText("No");
                    } else {
                        tv_cd_Coptic.setText(snapshot.getString("et_Coptic") + " INR");
                    }
                    if (snapshot.getString("et_Japanese").equals("")) {
                        tv_cd_Japanese.setText("No");
                    } else {
                        tv_cd_Japanese.setText(snapshot.getString("et_Japanese") + " INR");
                    }
                    if (snapshot.getString("et_CasedInWiro").equals("")) {
                        tv_cd_CasedInWiro.setText("No");
                    } else {
                        tv_cd_CasedInWiro.setText(snapshot.getString("et_CasedInWiro") + " INR");
                    }
                    if (snapshot.getString("et_Pamphlet").equals("")) {
                        tv_cd_pamphlet.setText("No");
                    } else {
                        tv_cd_pamphlet.setText(snapshot.getString("et_Pamphlet") + " INR");
                    }
                    tv_cd_cpp.setText(snapshot.getString("Color Print Price") + " Paisa");
                    tv_shopName.setText(snapshot.getString("Shop Name"));
                    tv_cd_BWpp.setText(snapshot.getString("BW Print Price") + " Paisa");

                    tv_cd_cxps.setText(snapshot.getString("Color Xerox Price") + " Paisa");

                    if (snapshot.getString("et_sectionSewn").equals("")) {
                        tv_cd_sectionSewn.setText("No");
                    } else {
                        tv_cd_sectionSewn.setText(snapshot.getString("et_sectionSewn") + " INR");
                    }
                    if (snapshot.getString("et_A1").equals("")) {
                        str_a1 = snapshot.getString("et_A1");
                        tv_cd_A1.setText("No");
                    } else {
                        str_a1 = snapshot.getString("et_A1");
                        tv_cd_A1.setText(snapshot.getString("et_A1") + " INR");
                    }
                    tv_cd_bwxps.setText(snapshot.getString("et_BWxeroxPrice") + " Paisa");
                    tv_cd_ss.setText(snapshot.getString("et_smallSpiral") + " INR");
                    tv_cd_ls.setText(snapshot.getString("et_LargeSpiral") + " INR");
                    tv_cd_ms.setText(snapshot.getString("et_MeduimSpiral") + " INR");
                    if (snapshot.getString("et_A4").equals("")) {
                        str_a4 = snapshot.getString("et_A4");
                        tv_cd_A4.setText("No");

                    } else {
                        str_a4 = snapshot.getString("et_A4");
                        tv_cd_A4.setText(snapshot.getString("et_A4") + " INR");
                    }
                    if (snapshot.getString("et_A3").equals("")) {
                        str_a3 = snapshot.getString("et_A3");
                        tv_cd_A3.setText("No");
                    } else {
                        str_a3 = snapshot.getString("et_A3");
                        tv_cd_A3.setText(snapshot.getString("et_A3") + " INR");
                    }
                }
                if (snapshot.getString("et_A2").equals("")) {
                    str_a2 = snapshot.getString("et_A2");
                    tv_cd_A2.setText("No");
                } else {
                    str_a2 = snapshot.getString("et_A2");
                    tv_cd_A2.setText(snapshot.getString("et_A2") + " INR");
                }
                try {
                    if (snapshot.getString("et_hard").equals("")) {
                        tv_cd_hardbound.setText("No");
                    } else {
                        tv_cd_hardbound.setText(snapshot.getString("et_hard") + " INR");
                    }
                } catch (Exception ee) {
                    tv_cd_hardbound.setText("No");
                }
            }
        });
        btn_info_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInfo.dismiss();
            }
        });
        dialogInfo.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(ShopInformationBottomActivity.this, MapsActivity.class);
        startActivity(i);
        finishAffinity();
    }
}
