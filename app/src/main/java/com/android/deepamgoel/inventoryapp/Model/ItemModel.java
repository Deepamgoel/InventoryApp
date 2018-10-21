package com.android.deepamgoel.inventoryapp.Model;

public class ItemModel {

    private int id;
    private String mProductName;
    private int mProductPrice;
    private int mProductQuantity;
    private String mSupplierName;
    private int mSupplierPhone;
    public ItemModel(int id, String mProductName, int mProductPrice, int mProductQuantity, String mSupplierName, int mSupplierPhone) {
        this.id = id;
        this.mProductName = mProductName;
        this.mProductPrice = mProductPrice;
        this.mProductQuantity = mProductQuantity;
        this.mSupplierName = mSupplierName;
        this.mSupplierPhone = mSupplierPhone;
    }

    public int getId() {
        return id;
    }

    public String getmProductName() {
        return mProductName;
    }

    public int getmProductPrice() {
        return mProductPrice;
    }

    public int getmProductQuantity() {
        return mProductQuantity;
    }

    public String getmSupplierName() {
        return mSupplierName;
    }

    public int getmSupplierPhone() {
        return mSupplierPhone;
    }
}
