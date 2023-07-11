package com.dhruvi.dhruvisonani.usersidexa2.Entity;

public class SignUpEntity {

    int int_mobileNum;
    String str_name,str_emailId,str_gender,str_pwd;

    public SignUpEntity(){
    }
    public SignUpEntity(String str_name, String str_emailId, String str_gender,String str_pwd) {
        //this.int_mobileNum = int_mobileNum;
        this.str_name = str_name;
        this.str_emailId = str_emailId;
        this.str_gender = str_gender;
        this.str_pwd = str_pwd;
    }
}
