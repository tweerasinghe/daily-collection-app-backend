package com.tuktuk.pojo;
// Generated Feb 14, 2020 8:49:07 PM by Hibernate Tools 4.3.1



/**
 * Error generated by hbm2java
 */
public class Error  implements java.io.Serializable {


     private Integer iderror;
     private String detail;
     private String date;
     private String time;

    public Error() {
    }

    public Error(String detail, String date, String time) {
       this.detail = detail;
       this.date = date;
       this.time = time;
    }
   
    public Integer getIderror() {
        return this.iderror;
    }
    
    public void setIderror(Integer iderror) {
        this.iderror = iderror;
    }
    public String getDetail() {
        return this.detail;
    }
    
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public String getDate() {
        return this.date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return this.time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }




}

