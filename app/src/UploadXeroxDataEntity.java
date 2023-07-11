package com.dhruvi.dhruvisonani.usersidexa2.Entity;

public class UploadXeroxDataEntity {
    public String str_page,str_spiral,str_side,str_color,str_bw,str_note,str_sum,str_MobileNum,str_total,str_shopNumber,shopName,str_date;
    public UploadXeroxDataEntity(String str_page, String str_spiral, String str_side, String str_color, String str_bw, String str_note, String str_sum,String str_mobileNum,String str_total) {
        this.str_page = str_page;
        this.str_spiral = str_spiral;
        this.str_MobileNum = str_mobileNum;
        this.str_side = str_side;
        this.str_color = str_color;
        this.str_bw = str_bw;
        this.str_total = str_total;
        this.str_note = str_note;
        this.str_sum = str_sum;
    }

    public UploadXeroxDataEntity(String str_page, String str_spiral, String str_side, String str_color, String str_bw, String str_note, String str_sum,String str_mobileNum,String str_total,String str_shopNumber,String shopName,String str_date) {
        this.str_shopNumber = str_shopNumber;
        this.str_page = str_page;
        this.str_date = str_date;
        this.shopName = shopName;
        this.str_spiral = str_spiral;
        this.str_MobileNum = str_mobileNum;
        this.str_side = str_side;
        this.str_color = str_color;
        this.str_bw = str_bw;
        this.str_total = str_total;
        this.str_note = str_note;
        this.str_sum = str_sum;
    }
}
