package com.dhruvi.dhruvisonani.usersidexa2.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhruvi.dhruvisonani.usersidexa2.Activity.OnlinePaymentActivity;
import com.dhruvi.dhruvisonani.usersidexa2.Entity.BooksAvailableEntity;
import com.dhruvi.dhruvisonani.usersidexa2.Helper.UploadPDF;
import com.dhruvi.dhruvisonani.usersidexa2.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.dhruvi.dhruvisonani.usersidexa2.Activity.LoginSignUpActivity.MyPREFERENCES;
import static com.dhruvi.dhruvisonani.usersidexa2.Activity.LoginSignUpActivity.shopName;
import static com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity.Str_shopkeeperNumber;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvailableBookFragment extends Fragment {

    RecyclerView rv_availablebbokk;
    DatabaseReference databaseReference, mDatabaseReference, mUserreference;
    DocumentReference documentReference;
    FirebaseRecyclerOptions<BooksAvailableEntity> options;
    FirebaseRecyclerAdapter<BooksAvailableEntity, MyViewHolderForAvailableBook> adapter;

    String username,fullname;
    String  str_imageuri;
    public String a;
    public static int b;


    public AvailableBookFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_available_book, container, false);
        rv_availablebbokk = v.findViewById(R.id.rv_availablebbokk);
        rv_availablebbokk.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("RequestPrintAttachment");


        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        username = sharedpreferences.getString("uname", "");

        mUserreference = FirebaseDatabase.getInstance().getReference("User Record");
        documentReference = FirebaseFirestore.getInstance().collection("Users").document(username);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    fullname = snapshot.getString("Full Name");
                }
            }
        });

        loadData();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("AddNewBookEntity").child(Str_shopkeeperNumber);
        options = new FirebaseRecyclerOptions.Builder<BooksAvailableEntity>().setQuery(databaseReference, BooksAvailableEntity.class).build();
        adapter = new FirebaseRecyclerAdapter<BooksAvailableEntity, MyViewHolderForAvailableBook>(options) {
            @Override
            protected void onBindViewHolder(final MyViewHolderForAvailableBook holder, final int i, final BooksAvailableEntity entity) {


                holder.tv_bookName.setText("Name : " + entity.getStr_bookName());
                holder.tv_bookPrice.setText("Price : " + String.valueOf(entity.getstr_bookPrice()));
                Picasso.with(getContext()).load(entity.getStr_imageId()).into(holder.iv_book);

                holder.btn_buyBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrintFragment.int_total = 0;
                        XeroxFragment.int_total = 0;
                        a = entity.getStr_bookName();
                        b = entity.getstr_bookPrice();
                        str_imageuri = entity.getStr_imageId();
                        startActivityForResult(new Intent(getActivity(), OnlinePaymentActivity.class), 10001);
                    }
                });
            }

            @Override
            public MyViewHolderForAvailableBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_for_available_book_inactivity, parent, false);
                return new MyViewHolderForAvailableBook(v);
            }
        };
        adapter.startListening();
        rv_availablebbokk.setAdapter(adapter);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 10001) && (resultCode == 1)) {
            // recreate your fragment here
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(new PrintFragment()).attach(new AvailableBookFragment()).commit();

            sendBuyRequest();
            Toast.makeText(getActivity(), "Book Buying Request for " + a + " Sent to " + shopName + ", You will get their acknowledgment soon !!", Toast.LENGTH_LONG).show();
        }

    }

    private void sendBuyRequest() {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String formattedDate = df.format(c);

        UploadPDF upload = new UploadPDF(a,b,username,fullname,str_imageuri,formattedDate);
        UploadPDF uploadUser = new UploadPDF(a,String.valueOf(b),formattedDate,shopName, username);
        mDatabaseReference.child(Str_shopkeeperNumber).child(Str_shopkeeperNumber + "_" + a + "_"  ).setValue(upload);
        mUserreference.child(username).child("Book" + "_" + a + "_" + b).setValue(uploadUser);

//        Toast.makeText(getActivity(), "Request Sent to " + shopName + ", You will get their acknowledgment soon !!", Toast.LENGTH_LONG).show();
    }
    public static class MyViewHolderForAvailableBook extends RecyclerView.ViewHolder {
        public static TextView tv_bookName, tv_bookPrice;
        Button btn_buyBook;
        ImageView iv_book;

        public MyViewHolderForAvailableBook(@NonNull View itemView) {
            super(itemView);
            tv_bookName = itemView.findViewById(R.id.tv_bookName1);
            tv_bookPrice = itemView.findViewById(R.id.tv_bookPrice);
            iv_book = itemView.findViewById(R.id.iv_book);
            btn_buyBook = itemView.findViewById(R.id.btn_buyBook);
        }
    }


}


//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
//                    BooksAvailableEntity p = dataSnapshot1.getValue(BooksAvailableEntity.class);
//                    arrayList.add(p);
//
//                }
//                adapter = new BookAvailableAdapter(getActivity(),arrayList);
//                rv_availablebbokk.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "Something Went Wrong !!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
////        adapter = new BookAvailableAdapter(getActivity(), getMyList());
////        rv_availablebbokk.setAdapter(adapter);


//    @Override
//    public void onStart() {
//        super.onStart();
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                  arrayList.clear();
//                  for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
//                      BooksAvailableEntity  entity = dataSnapshot1.getValue(BooksAvailableEntity.class);
//                      arrayList.add(entity);
//                  }
//
//                  adapter = new BookAvailableAdapter(getActivity(),arrayList);
//                  rv_availablebbokk.setAdapter(adapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "Oops!!!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//
//    private ArrayList<BooksAvailableEntity> getMyList(){
//        ArrayList<BooksAvailableEntity> booksAvailableEntities = new ArrayList<>();
////        String[] bookName = {"How Successful People Think","Motivation","8 Mintes in Morning"};
////        int[] bookPrice = {100,200,300};
////        int[] bookAvailable = {1,2,3};
////        int[] images = {R.drawable.b1,R.drawable.b2,R.drawable.b3};
////
////        BooksAvailableEntity m;
////        for(int i=0;i<bookName.length;i++){
////            m = new BooksAvailableEntity();
////            m.setTv_bookName(bookName[i]);
////            m.setTv_bookPrice(bookPrice[i]);
////            m.setIv_book(images[i]);
////            m.setTv_NumOfCopy(bookAvailable[i]);
////            booksAvailableEntities.add(m);
////        }
//
//        return booksAvailableEntities;
//    }
//}


//package com.dhruvi.dhruvisonani.usersidexa2.Adapter;
//
//        import android.content.Context;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.Button;
//        import android.widget.ImageView;
//        import android.widget.TextView;
//        import android.widget.Toast;
//
//        import androidx.annotation.NonNull;
//        import androidx.recyclerview.widget.RecyclerView;
//
//        import com.dhruvi.dhruvisonani.usersidexa2.Entity.BooksAvailableEntity;
//        import com.dhruvi.dhruvisonani.usersidexa2.R;
//
//        import java.util.ArrayList;
//
//public class BookAvailableAdapter extends RecyclerView.Adapter<BookAvailableAdapter.MyHolder>  {
//
//    Context c;
//    ArrayList<BooksAvailableEntity> mDataset;
//
//    public BookAvailableAdapter(Context c, ArrayList<BooksAvailableEntity> mDataset) {
//        this.c = c;
//        this.mDataset = mDataset;
//    }
//
//    @NonNull
//    @Override
//    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_for_available_book_inactivity,null);
//        return new MyHolder(view);
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull MyHolder myHolder,final int i) {
//        //BooksAvailableEntity entity = mDataset.get(i);
//        myHolder.tv_bookName.setText(String.valueOf(mDataset.get(i).getstr_bookName()));
//        myHolder.tv_bookPrice.setText(String.valueOf(mDataset.get(i).getstr_bookPrice()));
//        myHolder.iv_book.setImageResource(Integer.valueOf(mDataset.get(i).getstr_imageId()));
//        myHolder.btn_buyBook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(c, "buy : "+(i+1), Toast.LENGTH_SHORT).show();
//            }
//        });
//        //Picasso.get().load(mDataset.get(i).getIv_book().into(myHolder.iv_book));
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return mDataset.size();
//    }
//
//    public class MyHolder extends RecyclerView.ViewHolder{
//
//        TextView tv_bookName,tv_bookPrice;
//        Button btn_buyBook;
//        ImageView iv_book;
//        public MyHolder(@NonNull View itemView) {
//            super(itemView);
//            this.tv_bookName = itemView.findViewById(R.id.tv_bookName1);
//            this.tv_bookPrice = itemView.findViewById(R.id.tv_bookPrice);
//            this.iv_book = itemView.findViewById(R.id.iv_book);
//            btn_buyBook = itemView.findViewById(R.id.btn_buyBook);
//        }
//
//    }
//}
