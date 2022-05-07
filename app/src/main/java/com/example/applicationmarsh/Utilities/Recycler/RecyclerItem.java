package com.example.applicationmarsh.Utilities.Recycler;

public class RecyclerItem {

    private int claimImage;
    private String claimHead;
    private String claimBody;

    public RecyclerItem(int imageResource, String head, String body){
        setClaimImage(imageResource);
        setClaimHead(head);
        setClaimBody(body);
    }


    //GET & SET
    public int getClaimImage() {
        return claimImage;
    }

    public void setClaimImage(int claimImage) {
        this.claimImage = claimImage;
    }

    public String getClaimHead() {
        return claimHead;
    }

    public void setClaimHead(String claimHead) {
        this.claimHead = claimHead;
    }

    public String getClaimBody() {
        return claimBody;
    }

    public void setClaimBody(String claimBody) {
        this.claimBody = claimBody;
    }
}
