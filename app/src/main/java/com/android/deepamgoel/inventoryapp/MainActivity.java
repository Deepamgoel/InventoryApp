package com.android.deepamgoel.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.deepamgoel.inventoryapp.Model.ItemModel;
import com.android.deepamgoel.inventoryapp.adapter.MainAdapter;
import com.android.deepamgoel.inventoryapp.data.InventoryContract.InventoryEntry;
import com.android.deepamgoel.inventoryapp.data.InventoryDbHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view_main)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_state_text_view_main)
    TextView mEmptyStateTextView;
    @BindView(R.id.content_text_view_main)
    TextView mContentTextView;
    @BindView(R.id.item_count_text_view_main)
    TextView mTotalItemTextView;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private MainAdapter adapter;
    private List<ItemModel> list;
    private InventoryDbHelper dbHelper;

    /**
     * Temporarily not using {@link #mRecyclerView} due to some error occurring when
     * adding data to {@link #list} and updating {@link #adapter}. Using a
     * temporary {@link #mContentTextView} instead to show the content of database
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        dbHelper = new InventoryDbHelper(this);

//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

//        list = new ArrayList<>();
//        adapter = new MainAdapter(this, list);
//        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseContent();
    }

    private void displayDatabaseContent() {
        // Create and/or open a database to read from it
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_PHONE};

        // Perform a query on the inventory table
        Cursor cursor = database.query(
                InventoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,
                null);

        mTotalItemTextView.setText(getString(R.string.total_items, cursor.getCount()));

        try {
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
            int productNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
            int productPriceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
            int productQuantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);

            Log.d("TAG", "displayDatabaseContent: " + productNameColumnIndex);
            if (cursor.getCount() != 0) {
                mEmptyStateTextView.setVisibility(View.GONE);
                mContentTextView.setVisibility(View.VISIBLE);
//                mRecyclerView.setVisibility(View.VISIBLE);
                while (cursor.moveToNext()) {
                    // Use that index to extract the String or Int value of the word
                    // at the current row the cursor is on.
                    int id = cursor.getInt(idColumnIndex);
                    String productName = cursor.getString(productNameColumnIndex);
                    int productPrice = cursor.getInt(productPriceColumnIndex);
                    int productQuantity = cursor.getInt(productQuantityColumnIndex);
                    String supplierName = cursor.getString(supplierNameColumnIndex);
                    int supplierPhone = cursor.getInt(supplierPhoneColumnIndex);

                    // Temporary view holder for displaying content from database
                    mContentTextView.append(getString(R.string.content,
                            id,
                            productName,
                            productPrice,
                            productQuantity,
                            supplierName,
                            supplierPhone));

//                        ItemModel model = new ItemModel(id, productName, productPrice, productQuantity, supplierName, supplierPhone);
//                        list.add(model);
//                        adapter.notifyDataSetChanged();
                }
            } else {
                mEmptyStateTextView.setVisibility(View.VISIBLE);
                mContentTextView.setVisibility(View.GONE);
//                mRecyclerView.setVisibility(View.INVISIBLE);
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert:
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
                break;
            case R.id.action_delete:
                dbHelper.getWritableDatabase().delete(InventoryEntry.TABLE_NAME,null,null);
                onStart();
                break;
        }
        return true;
    }
}
