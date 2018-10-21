package com.android.deepamgoel.inventoryapp.data;

import android.provider.BaseColumns;

public class InventoryContract {


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private InventoryContract() {
    }

    public static final class InventoryEntry implements BaseColumns {

        /**
         * Name of database table for pets
         */
        public static final String TABLE_NAME = "inventory";

        /**
         * Unique ID number for the item (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * Name of product.
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_PRODUCT_NAME = "productName";

        /**
         * Price of product.
         * <p>
         * Type: INTEGER
         */
        public static final String COLUMN_PRODUCT_PRICE = "productPrice";

        /**
         * Quantity of product.
         * <p>
         * Type: INTEGER
         */
        public static final String COLUMN_PRODUCT_QUANTITY = "productQuantity";

        /**
         * Name of supplier.
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_SUPPLIER_NAME = "supplierName";

        /**
         * Phone number of Supplier.
         * <p>
         * Type: INTEGER
         */
        public static final String COLUMN_SUPPLIER_PHONE = "supplierPhone";
    }
}
