package com.example.sebas.hotdogadmin;

public class HotDogItem {


    @com.google.gson.annotations.SerializedName("hotdog")
    private String mHotdog;

    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    @com.google.gson.annotations.SerializedName("bbqsauce")
    private boolean mIsBbqSauce;

    @com.google.gson.annotations.SerializedName("ketchup")
    private boolean mIsKetchup;

    @com.google.gson.annotations.SerializedName("mayonnaise")
    private boolean mIsMayonnaise;

    @com.google.gson.annotations.SerializedName("curry")
    private boolean mIsCurry;

    @com.google.gson.annotations.SerializedName("onion")
    private boolean mIsOnion;

    @com.google.gson.annotations.SerializedName("cheese")
    private boolean mIsCheese;

    @com.google.gson.annotations.SerializedName("totalprice")
    private double mTotalPrice;

    public HotDogItem() {

    }

    @Override
    public String toString() {
        return getHotdog();
    }


    public HotDogItem(String hotdog, String id, boolean bbqSauce, boolean ketchup, boolean mayonnaise, boolean curry, boolean onion, boolean cheese, int totalPrice) {
        this.setHotdog (hotdog);
        this.setId(id);
        this.setBbqSauce(bbqSauce);
        this.setKetchup(ketchup);
        this.setMayonnaise(mayonnaise);
        this.setCurry(curry);
        this.setOnion(onion);
        this.setCheese(cheese);
        this.setTotalPrice(totalPrice);
    }


    public String getHotdog() {
        return mHotdog;
    }
    public final void setHotdog(String hotdog) {
        mHotdog = hotdog;
    }

    public String getId() {
        return mId;
    }
    public final void setId(String id) {
        mId = id;
    }

    public boolean isBbqSauce() {
        return mIsBbqSauce;
    }
    public void setBbqSauce(boolean bbqSauce) {
        this.mIsBbqSauce = bbqSauce;
    }

    public boolean isKetchup() {
        return mIsKetchup;
    }
    public void setKetchup(boolean ketchup) {
        this.mIsKetchup = ketchup;
    }
    public boolean isMayonnaise() {
        return mIsMayonnaise;
    }
    public void setMayonnaise(boolean mayonnaise) {
        this.mIsMayonnaise = mayonnaise;
    }

    public boolean isCurry() {
        return mIsCurry;
    }
    public void setCurry (boolean curry) {
        this.mIsCurry = curry;
    }
    public boolean isOnion() {
        return mIsOnion;
    }
    public void setOnion (boolean onion) {
        this.mIsOnion = onion;
    }
    public boolean isCheese() {
        return mIsCheese;
    }
    public void setCheese (boolean cheese) {
        this.mIsCheese = cheese;
    }
    public double getTotalPrice() {
        return mTotalPrice;
    }
    public void setTotalPrice (double totalPrice) {
        this.mTotalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof HotDogItem && ((HotDogItem) o).mId == mId;
    }
}


