package com.example.final_project;

public class poolData {
    private  String pooltitle;
    private String poolmembername;
    private String pooltime;
    private String id;


    public poolData(String pooltitle, String poolmembername, String pooltime,String id) {
        this.pooltitle = pooltitle;
        this.poolmembername = poolmembername;
        this.pooltime=pooltime;
        this.id=id;

    }

    public String getPooltime() {
        return pooltime;
    }

    public void setPooltime(String pooltime) {
        this.pooltime = pooltime;
    }

    public String getPooltitle() {
        return pooltitle;
    }

    public void setPooltitle(String pooltitle) {
        this.pooltitle = pooltitle;
    }

    public String getPoolmembername() {
        return poolmembername;
    }

    public void setPoolmembername(String poolmembername) {
        this.poolmembername = poolmembername;
    }


    public void setid(String id) {
        this.id = id;
    }

    public String getid() {
        return id;
    }


}
