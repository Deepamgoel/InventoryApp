package com.android.deepamgoel.inventoryapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.deepamgoel.inventoryapp.data.InventoryContract.InventoryEntry;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PET_LOADER = 2;

    @BindView(R.id.edit_product_name)
    AutoCompleteTextView mProductNameEditText;
    @BindView(R.id.edit_product_price)
    AutoCompleteTextView mProductPriceEditText;
    @BindView(R.id.edit_product_quantity_inc)
    Button mProductQuantityIncrease;
    @BindView(R.id.edit_product_quantity_dec)
    Button mProductQuantityDecrease;
    @BindView(R.id.edit_product_quantity)
    TextView mProductQuantityTextView;
    @BindView(R.id.edit_supplier_name)
    AutoCompleteTextView mSupplierNameEditText;
    @BindView(R.id.edit_supplier_phone)
    AutoCompleteTextView mSupplierPhoneEditText;

    private int quantity;
    private Uri mCurrentItemUri;
    private boolean mDataHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            view.performClick();
            mDataHasChanged = true;
            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();


        if (mCurrentItemUri == null) {
            getSupportActionBar().setTitle(R.string.editor_activity_title_new_item);
            invalidateOptionsMenu();
        } else {
            getSupportActionBar().setTitle(R.string.editor_activity_title_edit_item);
            getSupportLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);
        }

        mProductNameEditText.setOnTouchListener(mTouchListener);
        mProductPriceEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneEditText.setOnTouchListener(mTouchListener);

        mProductQuantityDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity >= 1) {
                    quantity--;
                    mProductQuantityTextView.setText(String.format(Locale.getDefault(), "%d", quantity));
                }
            }
        });

        mProductQuantityIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                mProductQuantityTextView.setText(String.format(Locale.getDefault(), "%d", quantity));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveItem();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mDataHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                showUnsavedChangesDialog();
                return true;
        }
        return true;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_PHONE,
        };

        return new CursorLoader(this,
                mCurrentItemUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int productNameIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
            int productPriceIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
            int productQuantityIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);

            String productName = cursor.getString(productNameIndex);
            int productPrice = cursor.getInt(productPriceIndex);
            int productQuantity = cursor.getInt(productQuantityIndex);
            String supplierName = cursor.getString(supplierNameIndex);
            int supplierPhone = cursor.getInt(supplierPhoneIndex);

            quantity = productQuantity;
            mProductNameEditText.setText(productName);
            mProductPriceEditText.setText(String.format(Locale.getDefault(), "%d", productPrice));
            mProductQuantityTextView.setText(String.format(Locale.getDefault(), "%d", productQuantity));
            mSupplierNameEditText.setText(supplierName);
            mSupplierPhoneEditText.setText(String.format(Locale.getDefault(), "%d", supplierPhone));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mProductNameEditText.setText(null);
        mProductPriceEditText.setText(null);
        mProductQuantityTextView.setText(null);
        mSupplierNameEditText.setText(null);
        mSupplierPhoneEditText.setText(null);

    }

    @Override
    public void onBackPressed() {
        if (!mDataHasChanged) {
            super.onBackPressed();
            return;
        }

        showUnsavedChangesDialog();
    }

    private void saveItem() {
        String productName = mProductNameEditText.getText().toString().trim();
        String supplierName = mSupplierNameEditText.getText().toString().trim();
        int productPrice = Integer.parseInt(mProductPriceEditText.getText().toString().trim());
        int productQuantity = Integer.parseInt(mProductQuantityTextView.getText().toString().trim());
        int supplierPhone = Integer.parseInt(mSupplierPhoneEditText.getText().toString().trim());

        if (mCurrentItemUri == null && TextUtils.isEmpty(productName)) {
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryEntry.COLUMN_PRODUCT_NAME, productName);
        contentValues.put(InventoryEntry.COLUMN_PRODUCT_PRICE, productPrice);
        contentValues.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
        contentValues.put(InventoryEntry.COLUMN_SUPPLIER_NAME, supplierName);
        contentValues.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, supplierPhone);

        if (mCurrentItemUri == null) {
            Uri uri = getContentResolver().insert(InventoryEntry.CONTENT_URI, contentValues);
            if (uri == null) {
                Toast.makeText(this, R.string.editor_insert_item_failed, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.editor_insert_item_successful, Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().
                    update(mCurrentItemUri, contentValues, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteItem() {
        if (mCurrentItemUri != null) {
            int rowsAffected = getContentResolver().delete(mCurrentItemUri, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_item_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void showUnsavedChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.unsaved_changes_dialog_msg)
                .setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                })
                .create()
                .show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.delete_dialog_msg)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null)
                            dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}
