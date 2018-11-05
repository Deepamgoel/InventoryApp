/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.deepamgoel.inventoryapp.adapter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.deepamgoel.inventoryapp.R;
import com.android.deepamgoel.inventoryapp.data.InventoryContract.InventoryEntry;


public class InventoryCursorAdapter extends CursorAdapter {
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.item_recycler_view_main, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView productNameTextView = view.findViewById(R.id.item_product_name);
        TextView productPriceTextView = view.findViewById(R.id.item_product_price);
        TextView productQuantityTextView = view.findViewById(R.id.item_product_quantity);
        final Button productBuyButton = view.findViewById(R.id.item_product_buy_button);

        // Find the columns of pet attributes that we're interested in
        int productIdIndex = cursor.getColumnIndex(InventoryEntry._ID);
        int productNameIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
        int productPriceIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
        int productQuantityIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY);

        // Read the pet attributes from the Cursor for the current pet
        int productId = cursor.getInt(productIdIndex);
        String productName = cursor.getString(productNameIndex);
        int productPrice = cursor.getInt(productPriceIndex);
        final int productQuantity = cursor.getInt(productQuantityIndex);

        // Update the TextViews with the attributes for the current pet
        productNameTextView.setText(productName);
        productPriceTextView.setText(context.getString(R.string.product_price, productPrice));
        productQuantityTextView.setText(context.getString(R.string.product_quantity, productQuantity));
        if (productQuantity > 0) {
            productBuyButton.setText(R.string.item_button_buy);
            productBuyButton.setEnabled(true);
        } else {
            productBuyButton.setText(R.string.item_button_out_of_stock);
            productBuyButton.setEnabled(false);
        }

        final Uri currentProductUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, productId);

        productBuyButton.setEnabled(true);

        productBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = productQuantity - 1;
                if (quantity >= 0) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, quantity);

                    context.getContentResolver().update(
                            currentProductUri
                            , contentValues
                            , null
                            , null);

                    context.getContentResolver().notifyChange(
                            currentProductUri
                            , null);
                } else {
                    Toast.makeText(context, "Item out of stock", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        });

    }

}