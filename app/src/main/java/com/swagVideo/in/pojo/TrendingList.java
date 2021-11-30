package com.swagVideo.in.pojo;

import com.swagVideo.in.data.models.Clip;

import java.util.ArrayList;

public class TrendingList {
    String heading;
    String totalViewCount;
    ArrayList<NearBylIst> items = new ArrayList();
    ArrayList<NearBylIst> itemsLatest = new ArrayList();

    public TrendingList(String heading) {
        this.heading = heading;
    }

    public TrendingList(String heading, ArrayList<NearBylIst> items) {
        this.heading = heading;
        this.items = items;
    }

    public TrendingList(String heading, ArrayList<NearBylIst> items, ArrayList<NearBylIst> itemsLatest) {
        this.heading = heading;
        this.items = items;
        this.itemsLatest = itemsLatest;
    }

    public TrendingList(String heading,String totalViewCount, ArrayList<NearBylIst> items, ArrayList<NearBylIst> itemsLatest) {
        this.heading = heading;
        this.totalViewCount = totalViewCount;
        this.items = items;
        this.itemsLatest = itemsLatest;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getTotalViewCount() {
        return totalViewCount;
    }

    public void setTotalViewCount(String totalViewCount) {
        this.totalViewCount = totalViewCount;
    }

    public ArrayList<NearBylIst> getItems() {
        return items;
    }

    public void setItems(ArrayList<NearBylIst> items) {
        this.items = items;
    }

    public ArrayList<NearBylIst> getItemsLatest() {
        return itemsLatest;
    }

    public void setItemsLatest(ArrayList<NearBylIst> itemsLatest) {
        this.itemsLatest = itemsLatest;
    }
}
