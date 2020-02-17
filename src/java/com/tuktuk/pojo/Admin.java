package com.tuktuk.pojo;
// Generated Feb 14, 2020 8:49:07 PM by Hibernate Tools 4.3.1



/**
 * Admin generated by hbm2java
 */
public class Admin  implements java.io.Serializable {


     private Integer idadmin;
     private String name;
     private String contact;
     private String nic;
     private String username;
     private String password;
     private Integer status;
     private int level;
     private int av;
     private int btn;
     private int vh;
     private int co;
     private int pa;
     private int st;
     private int ad;

    public Admin() {
    }

	
    public Admin(int level, int av, int btn, int vh, int co, int pa, int st, int ad) {
        this.level = level;
        this.av = av;
        this.btn = btn;
        this.vh = vh;
        this.co = co;
        this.pa = pa;
        this.st = st;
        this.ad = ad;
    }
    public Admin(String name, String contact, String nic, String username, String password, Integer status, int level, int av, int btn, int vh, int co, int pa, int st, int ad) {
       this.name = name;
       this.contact = contact;
       this.nic = nic;
       this.username = username;
       this.password = password;
       this.status = status;
       this.level = level;
       this.av = av;
       this.btn = btn;
       this.vh = vh;
       this.co = co;
       this.pa = pa;
       this.st = st;
       this.ad = ad;
    }
   
    public Integer getIdadmin() {
        return this.idadmin;
    }
    
    public void setIdadmin(Integer idadmin) {
        this.idadmin = idadmin;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getContact() {
        return this.contact;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getNic() {
        return this.nic;
    }
    
    public void setNic(String nic) {
        this.nic = nic;
    }
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    public int getLevel() {
        return this.level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    public int getAv() {
        return this.av;
    }
    
    public void setAv(int av) {
        this.av = av;
    }
    public int getBtn() {
        return this.btn;
    }
    
    public void setBtn(int btn) {
        this.btn = btn;
    }
    public int getVh() {
        return this.vh;
    }
    
    public void setVh(int vh) {
        this.vh = vh;
    }
    public int getCo() {
        return this.co;
    }
    
    public void setCo(int co) {
        this.co = co;
    }
    public int getPa() {
        return this.pa;
    }
    
    public void setPa(int pa) {
        this.pa = pa;
    }
    public int getSt() {
        return this.st;
    }
    
    public void setSt(int st) {
        this.st = st;
    }
    public int getAd() {
        return this.ad;
    }
    
    public void setAd(int ad) {
        this.ad = ad;
    }




}


