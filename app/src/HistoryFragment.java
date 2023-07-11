package com.dhruvi.dhruvisonani.usersidexa2.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dhruvi.dhruvisonani.usersidexa2.Entity.RequestsEntity;
import com.dhruvi.dhruvisonani.usersidexa2.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import static com.dhruvi.dhruvisonani.usersidexa2.Activity.LoginSignUpActivity.MyPREFERENCES;

public class HistoryFragment extends Fragment {

    RecyclerView rv_requestTocome;
    RequestsEntity requestsEntity;
    Query query;
    FirebaseRecyclerOptions<RequestsEntity> options, OptionsXerox;
    FirebaseRecyclerAdapter<RequestsEntity, MyViewHodlderForRequestFragment> adapterFirebase;

    String username;
    List<String> list;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        username = sharedpreferences.getString("uname", "");


        rv_requestTocome = view.findViewById(R.id.rv_history);
        requestsEntity = new RequestsEntity();

        loadRequest();

        return view;
    }

    private void loadRequest() {

        query = FirebaseDatabase.getInstance().getReference().child("User Record").child(username).orderByChild("str_MobileNum").equalTo(username);
        options = new FirebaseRecyclerOptions.Builder<RequestsEntity>().setQuery(query, RequestsEntity.class).build();
        adapterFirebase = new FirebaseRecyclerAdapter<RequestsEntity, MyViewHodlderForRequestFragment>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyViewHodlderForRequestFragment holder1, int i, @NonNull final RequestsEntity entity) {
                holder1.tv_dateHistory.setText(entity.getStr_date() + " @ " + entity.getShopName());

                holder1.iv_plusHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowInfo(holder1, entity);
                    }
                });
                holder1.iv_minusHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideInfo(holder1, entity);
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHodlderForRequestFragment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_for_history_fragment, null);
                return new MyViewHodlderForRequestFragment(v);
            }
        };
        adapterFirebase.startListening();
        rv_requestTocome.setAdapter(adapterFirebase);

    }

    private void hideInfo(MyViewHodlderForRequestFragment holder1, RequestsEntity entity) {
        holder1.iv_plusHistory.setVisibility(View.VISIBLE);
        holder1.iv_minusHistory.setVisibility(View.GONE);

        holder1.cv_history_detail.setVisibility(View.GONE);

    }

    private void ShowInfo(MyViewHodlderForRequestFragment holder1, final RequestsEntity entity) {
        holder1.cv_history_detail.setVisibility(View.VISIBLE);
        holder1.iv_plusHistory.setVisibility(View.GONE);
        holder1.iv_minusHistory.setVisibility(View.VISIBLE);
        try {
            holder1.ll_book.setVisibility(View.GONE);
            holder1.ll_xerox.setVisibility(View.GONE);
            holder1.ll_print.setVisibility(View.VISIBLE);
            holder1.cv_history_detail.setCardBackgroundColor(Color.parseColor("#81D4FA"));
            holder1.tv_requests_Side.setText(entity.getStr_rb_side());

            holder1.tv_requests_pageSize.setText(entity.getStr_cb_pagesize());
            holder1.tv_requests_ShopNAme.setText(entity.getShopName());
            holder1.tv_requests_ShopContact.setText(entity.getStr_shopNumber());
            holder1.tv_requests_pageSize.setText(entity.getStr_cb_pagesize());
            holder1.tv_requests_Note.setText(entity.getStr_note());
            holder1.tv_requests_pageNumber.setText(entity.getStr_numpage());
            holder1.tv_reuqests_spiral.setText(entity.getStr_rb_spiral());
            holder1.tv_requests_bound.setText(entity.getStr_rb_bound());
            holder1.tv_requests_totalAmount.setText(entity.getStr_total());
            holder1.tv_requests_Note.setText(entity.getStr_note());
            if (!entity.getStr_bw_count().equals("0") && !entity.getStr_color_count().equals("0")) {
                holder1.tv_requests_numberOfCopy.setText(entity.getStr_bw_count() + " Bw and " + entity.getStr_color_count() + " Color");
                holder1.tv_requests_printType.setText("Both");
                ;
            } else if (entity.getStr_color_count().equals("0")) {
                holder1.tv_requests_numberOfCopy.setText(entity.getStr_bw_count());
                holder1.tv_requests_printType.setText("Black & White");
            } else {
                holder1.tv_requests_numberOfCopy.setText(entity.getStr_color_count());
                holder1.tv_requests_printType.setText("Color");

            }
            holder1.cv_history_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setType(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(entity.getUrl()));
                    startActivity(i);

                }
            });

        } catch (Exception e) {
            try {
                holder1.ll_print.setVisibility(View.GONE);
                holder1.ll_book.setVisibility(View.GONE);
                holder1.ll_xerox.setVisibility(View.VISIBLE);
                holder1.cv_history_detail.setCardBackgroundColor(Color.parseColor("#E0F7FA"));
                holder1.tv_requests_numberOfpageXerox.setText(entity.getStr_sum());
                holder1.tv_requests_ShopNAmeXerox.setText(entity.getShopName());
                holder1.tv_requests_ShopContactXerox.setText(entity.getStr_shopNumber());
                holder1.tv_requests_SideXerox.setText(entity.getStr_side());
                holder1.tv_requests_NoteXerox.setText(entity.getStr_note());
                holder1.tv_requests_pagesXerox.setText(entity.getStr_page());
                holder1.tv_reuqests_spiralXerox.setText(entity.getStr_spiral());
                holder1.tv_requests_totalAmountXerox.setText(entity.getStr_total());
                holder1.tv_requests_Note.setText(entity.getStr_note());
                if (!entity.getStr_bw().equals("0") && !entity.getStr_color().equals("0")) {

                    holder1.tv_requests_numberOfCopyXerox.setText(entity.getStr_bw() + " Bw and " + entity.getStr_color() + " Color");
                    holder1.tv_requests_printTypeXerox.setText("Both");
                } else if (entity.getStr_color().equals("0")) {
                    holder1.tv_requests_numberOfCopyXerox.setText(entity.getStr_bw() + " ");

                    holder1.tv_requests_printTypeXerox.setText("Black & White");
                } else {
                    holder1.tv_requests_numberOfCopyXerox.setText(entity.getStr_color() + " ");
                    holder1.tv_requests_printTypeXerox.setText("Color");
                }
            } catch (Exception ee) {
                holder1.ll_xerox.setVisibility(View.GONE);
                holder1.ll_print.setVisibility(View.GONE);
                holder1.ll_book.setVisibility(View.VISIBLE);
                holder1.cv_history_detail.setCardBackgroundColor(Color.parseColor("#33AAFF"));
                holder1.tv_requests_BookNam.setText(entity.getBookName());
                holder1.tv_requests_ShopContactBook.setText(entity.getStr_MobileNum());
                holder1.tv_requests_ShopNAmebook.setText(entity.getShopName());
                holder1.tv_reuqests_BookPrice.setText(entity.getBookPrice());
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private class MyViewHodlderForRequestFragment extends RecyclerView.ViewHolder {
        TextView tv_requests_pageNumber, tv_reuqests_spiral, tv_requests_printType, tv_requests_Side, tv_requests_numberOfCopy, tv_requests_bound, tv_requests_pageSize, tv_requests_totalAmount, tv_requests_Note;

        ImageView iv_plusHistory, iv_minusHistory;

        TextView tv_dateHistory, tv_requests_ShopNAme, tv_requests_ShopContact, tv_requests_ShopContactXerox, tv_requests_ShopNAmeXerox;
        TextView tv_requests_pagesXerox, tv_reuqests_spiralXerox, tv_requests_printTypeXerox, tv_requests_SideXerox, tv_requests_numberOfpageXerox, tv_requests_numberOfCopyXerox, tv_requests_totalAmountXerox, tv_requests_NoteXerox;

        LinearLayout ll_xerox, ll_print, ll_book;
        CardView cv_history_detail, cv_history_date;

        TextView tv_requests_ShopNAmebook, tv_requests_ShopContactBook, tv_requests_BookNam, tv_reuqests_BookPrice;

        public MyViewHodlderForRequestFragment(@NonNull View itemView) {
            super(itemView);


            ll_print = itemView.findViewById(R.id.ll_print);
            ll_xerox = itemView.findViewById(R.id.ll_xerox);
            ll_book = itemView.findViewById(R.id.ll_book);
            cv_history_detail = itemView.findViewById(R.id.cv_history_detail);
            cv_history_date = itemView.findViewById(R.id.cv_history_date);
            iv_plusHistory = itemView.findViewById(R.id.iv_plusHistory);
            iv_minusHistory = itemView.findViewById(R.id.iv_minusHistory);

            tv_dateHistory = itemView.findViewById(R.id.tv_dateHistory);
            tv_requests_ShopNAme = itemView.findViewById(R.id.tv_requests_ShopNAme);
            tv_requests_ShopContact = itemView.findViewById(R.id.tv_requests_ShopContact);
            tv_requests_ShopContactXerox = itemView.findViewById(R.id.tv_requests_ShopContactXerox);
            tv_requests_ShopNAmeXerox = itemView.findViewById(R.id.tv_requests_ShopNAmeXerox);

            tv_requests_ShopNAmebook = itemView.findViewById(R.id.tv_requests_ShopNAmebook);
            tv_requests_ShopContactBook = itemView.findViewById(R.id.tv_requests_ShopContactBook);
            tv_requests_BookNam = itemView.findViewById(R.id.tv_requests_BookNam);
            tv_reuqests_BookPrice = itemView.findViewById(R.id.tv_reuqests_BookPrice);

            tv_requests_pagesXerox = itemView.findViewById(R.id.tv_requests_pagesXerox);
            tv_reuqests_spiralXerox = itemView.findViewById(R.id.tv_reuqests_spiralXerox);
            tv_requests_printTypeXerox = itemView.findViewById(R.id.tv_requests_printTypeXerox);
            tv_requests_numberOfpageXerox = itemView.findViewById(R.id.tv_requests_numberOfpageXerox);
            tv_requests_SideXerox = itemView.findViewById(R.id.tv_requests_SideXerox);
            tv_requests_numberOfCopyXerox = itemView.findViewById(R.id.tv_requests_numberOfCopyXerox);
            tv_requests_totalAmountXerox = itemView.findViewById(R.id.tv_requests_totalAmountXerox);
            tv_requests_NoteXerox = itemView.findViewById(R.id.tv_requests_NoteXerox);


            tv_requests_pageNumber = itemView.findViewById(R.id.tv_requests_pageNumber);
            tv_reuqests_spiral = itemView.findViewById(R.id.tv_reuqests_spiral);
            tv_requests_printType = itemView.findViewById(R.id.tv_requests_printType);
            tv_requests_Side = itemView.findViewById(R.id.tv_requests_Side);
            tv_requests_numberOfCopy = itemView.findViewById(R.id.tv_requests_numberOfCopy);
            tv_requests_bound = itemView.findViewById(R.id.tv_requests_bound);
            tv_requests_pageSize = itemView.findViewById(R.id.tv_requests_pageSize);
            tv_requests_totalAmount = itemView.findViewById(R.id.tv_requests_totalAmount);
            tv_requests_Note = itemView.findViewById(R.id.tv_requests_Note);

        }
    }
}
