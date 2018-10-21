package com.android.deepamgoel.inventoryapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.deepamgoel.inventoryapp.data.InventoryContract.InventoryEntry;
import com.android.deepamgoel.inventoryapp.data.InventoryDbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorActivity extends AppCompatActivity {

    @BindView(R.id.edit_product_name)
    EditText mProductNameEditText;
    @BindView(R.id.edit_product_price)
    EditText mProductPriceEditText;
    @BindView(R.id.edit_product_quantity)
    EditText mProductQuantityEditText;
    @BindView(R.id.edit_supplier_name)
    EditText mSupplierNameEditText;
    @BindView(R.id.edit_supplier_phone)
    EditText mSupplierPhoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                insertItem();
                finish();
                break;
            case R.id.action_cancel:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }

    /**
     * Get user input from editor and save new item into database.
     */
    private void insertItem() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String productName = mProductNameEditText.getText().toString().trim();
        int productPrice = Integer.parseInt(mProductPriceEditText.getText().toString().trim());
        int productQuantity = Integer.parseInt(mProductQuantityEditText.getText().toString().trim());
        String supplierName = mSupplierNameEditText.getText().toString().trim();
        int supplierPhone = Integer.parseInt(mSupplierPhoneEditText.getText().toString().trim());

        // Create database helper
        InventoryDbHelper dbHelper = new InventoryDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryEntry.COLUMN_PRODUCT_NAME, productName);
        contentValues.put(InventoryEntry.COLUMN_PRODUCT_PRICE, productPrice);
        contentValues.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
        contentValues.put(InventoryEntry.COLUMN_SUPPLIER_NAME, supplierName);
        contentValues.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, supplierPhone);

        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = database.insert(InventoryEntry.TABLE_NAME, null, contentValues);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving item", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Item saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }
}
