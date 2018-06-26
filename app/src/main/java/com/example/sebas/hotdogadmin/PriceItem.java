package com.example.sebas.hotdogadmin;

public class PriceItem {


    @com.google.gson.annotations.SerializedName("hotdogprice")
    private double mHotDogPrice;

    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    @com.google.gson.annotations.SerializedName("bbqsauceprice")
    private double mBbqSaucePrice;

    @com.google.gson.annotations.SerializedName("ketchupprice")
    private double mKetchupPrice;

    @com.google.gson.annotations.SerializedName("mayonnaiseprice")
    private double mMayonnaisePrice;

    @com.google.gson.annotations.SerializedName("curryprice")
    private double mCurryPrice;

    @com.google.gson.annotations.SerializedName("onionprice")
    private double mOnionPrice;

    @com.google.gson.annotations.SerializedName("cheeseprice")
    private double mCheesePrice;


    public PriceItem() {

    }

    @Override
    public String toString() {
        return String.valueOf(getHotDog());
    }


    public PriceItem(int hotDogPrice, String id, double bbqSaucePrice, double ketchupPrice, double mayonnaisePrice, double curryPrice, double onionPrice, double cheesePrice) {
        this.setHotdog (hotDogPrice);
        this.setId(id);
        this.setBbqSauce(bbqSaucePrice);
        this.setKetchup(ketchupPrice);
        this.setMayonnaise(mayonnaisePrice);
        this.setCurry(curryPrice);
        this.setOnion(onionPrice);
        this.setCheese(cheesePrice);
    }


    public double getHotDog() {
        return mHotDogPrice;
    }
    public final void setHotdog(double hotDogPrice) {
        mHotDogPrice = hotDogPrice;
    }

    public String getId() {
        return mId;
    }
    public final void setId(String id) {
        mId = id;
    }

    public double getBbqSauce() {
        return mBbqSaucePrice;
    }
    public void setBbqSauce(double bbqSaucePrice) {
        this.mBbqSaucePrice = bbqSaucePrice;
    }

    public double getKetchup() {
        return mKetchupPrice;
    }
    public void setKetchup(double ketchup) {
        this.mKetchupPrice = ketchup;
    }

    public double getMayonnaise() {
        return mMayonnaisePrice;
    }
    public void setMayonnaise(double mayonnaisePrice) {
        this.mMayonnaisePrice = mayonnaisePrice;
    }

    public double getCurry() {
        return mCurryPrice;
    }
    public void setCurry (double curry) {
        this.mCurryPrice = curry;
    }

    public double getOnion() {
        return mOnionPrice;
    }
    public void setOnion (double onion) {
        this.mOnionPrice = onion;
    }

    public double getCheese() {
        return mCheesePrice;
    }
    public void setCheese (double cheese) {
        this.mCheesePrice = cheese;
    }


    @Override
    public boolean equals(Object o) {
        return o instanceof PriceItem && ((PriceItem) o).mId == mId;
    }
}

