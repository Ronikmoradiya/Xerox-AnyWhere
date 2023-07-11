package com.dhruvi.dhruvisonani.usersidexa2.Helper;

public class UploadPDF {
    public String Url, str_rb_spiral, str_rb_side, str_rb_bound, str_bw_count, str_cb_pagesize, str_color_count, str_numpage, str_note;
    public String str_MobileNum, str_total, str_shopNumber, shopName;
    public String str_date, str_imageId;

    public String bookName, bookPrice, username, fullname;


    public UploadPDF(String s, String str_rb_spiral, String str_rb_side, String str_rb_bound, String str_bw_count, String str_cb_pagesize, String str_color_count, String str_numpage, String str_note, String username, String str_total) {
        this.Url = s;
        this.str_rb_spiral = str_rb_spiral;
        this.str_total = str_total;
        this.str_rb_side = str_rb_side;
        this.str_MobileNum = username;
        this.str_rb_bound = str_rb_bound;
        this.str_bw_count = str_bw_count;
        this.str_cb_pagesize = str_cb_pagesize;
        this.str_color_count = str_color_count;
        this.str_numpage = str_numpage;
        this.str_note = str_note;
    }

    public UploadPDF(String s, String str_rb_spiral, String str_rb_side, String str_rb_bound, String str_bw_count, String str_cb_pagesize, String str_color_count, String str_numpage, String str_note, String username, String str_total, String str_shopNumber, String shopName, String str_date) {
        this.Url = s;
        this.str_rb_spiral = str_rb_spiral;
        this.str_date = str_date;
        this.str_total = str_total;
        this.str_rb_side = str_rb_side;
        this.str_MobileNum = username;
        this.str_rb_bound = str_rb_bound;
        this.str_bw_count = str_bw_count;
        this.str_cb_pagesize = str_cb_pagesize;
        this.str_color_count = str_color_count;
        this.str_numpage = str_numpage;
        this.str_note = str_note;
        this.str_shopNumber = str_shopNumber;
        this.shopName = shopName;
    }

    public UploadPDF(String bookName, int bookPrice, String username, String fullname, String str_imageId, String str_date) {
        this.bookName = bookName;
        this.bookPrice = String.valueOf(bookPrice);
        this.str_MobileNum = username;
        this.fullname = fullname;
        this.str_imageId = str_imageId;
        this.str_date = str_date;
    }


    public UploadPDF(String bookName, String bookPrice, String str_date, String shopName, String str_shopNumber) {
        this.bookName = bookName;
        this.bookPrice = String.valueOf(bookPrice);
        this.str_date = str_date;
        this.shopName = shopName;
        this.str_MobileNum = str_shopNumber;
    }

}