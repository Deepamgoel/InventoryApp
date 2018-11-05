package com.android.deepamgoel.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class InventoryContract {

    static final String CONTENT_AUTHORITY = "com.android.deepamgoel.inventoryapp";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_INVENTORY = "inventory";

    private InventoryContract() {
    }

    public static final class InventoryEntry implements BaseColumns {

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * The content URI to access the pet data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

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
