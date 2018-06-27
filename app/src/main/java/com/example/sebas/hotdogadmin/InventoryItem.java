package com.example.sebas.hotdogadmin;

public class InventoryItem {

    @com.google.gson.annotations.SerializedName("hotdogstock")
    private int mHotdogStock;

    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    @com.google.gson.annotations.SerializedName("bbqsaucestock")
    private int mBbqSauceStock;

    @com.google.gson.annotations.SerializedName("ketchupstock")
    private int mKetchupStock;

    @com.google.gson.annotations.SerializedName("mayonnaisestock")
    private int mMayonnaiseStock;

    @com.google.gson.annotations.SerializedName("currystock")
    private int mCurryStock;

    @com.google.gson.annotations.SerializedName("onionstock")
    private int mOnionStock;

    @com.google.gson.annotations.SerializedName("cheesestock")
    private int mCheeseStock;

    public InventoryItem() {

    }

    @Override
    public String toString() {
        return String.valueOf(getHotdogStock());
    }


    public InventoryItem(int hotdog, String id, int bbqSauce, int ketchup, int mayonnaise, int curry, int onion, int cheese) {
        this.setHotdogStock (hotdog);
        this.setId(id);
        this.setBbqSauceStock(bbqSauce);
        this.setKetchupStock(ketchup);
        this.setMayonnaiseStock(mayonnaise);
        this.setCurryStock(curry);
        this.setOnionStock(onion);
        this.setCheeseStock(cheese);

    }


    public int getHotdogStock () {
        return mHotdogStock;
    }
    public final void setHotdogStock(int hotdog) {
        mHotdogStock = hotdog;
    }

    public String getId() {
        return mId;
    }
    public final void setId(String id) {
        mId = id;
    }

    public int getBbqStock() {
        return mBbqSauceStock;
    }
    public void setBbqSauceStock(int bbqSauce) {
        this.mBbqSauceStock = bbqSauce;
    }

    public int getKetchupStock() {
        return mKetchupStock;
    }
    public void setKetchupStock(int ketchup) {
        this.mKetchupStock = ketchup;
    }

    public int getMayonnaiseStock() {
        return mMayonnaiseStock;
    }
    public void setMayonnaiseStock(int mayonnaise) {
        this.mMayonnaiseStock = mayonnaise;
    }

    public int getCurryStock() {
        return mCurryStock;
    }
    public void setCurryStock (int curry) {
        this.mCurryStock = curry;
    }

    public int getOnionStock() {
        return mOnionStock;
    }
    public void setOnionStock (int onion) {
        this.mOnionStock = onion;
    }

    public int getCheeseStock() {
        return mCheeseStock;
    }
    public void setCheeseStock (int cheese) {
        this.mCheeseStock = cheese;
    }


    @Override
    public boolean equals(Object o) {
        return o instanceof InventoryItem && ((InventoryItem) o).mId == mId;
    }
}
