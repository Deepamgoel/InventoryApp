package com.android.deepamgoel.inventoryapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.deepamgoel.inventoryapp.Model.ItemModel;
import com.android.deepamgoel.inventoryapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<ItemModel> list;
    private Context context;

    public MainAdapter(Context context, List<ItemModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_view_main, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ItemModel model = list.get(position);
        viewHolder.mProductName.setText(context.getString(R.string.product_name, model.getmProductName()));
        viewHolder.mProductQuantity.setText(context.getString(R.string.product_quantity, model.getmProductQuantity()));
        viewHolder.mProductPrice.setText(context.getString(R.string.product_price, model.getmProductPrice()));
        viewHolder.mSupplierName.setText(context.getString(R.string.supplier_name, model.getmSupplierName()));
        viewHolder.mSupplierPhone.setText(context.getString(R.string.supplier_phone, model.getmSupplierPhone()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(List<ItemModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_product_name)
        TextView mProductName;
        @BindView(R.id.item_product_price)
        TextView mProductPrice;
        @BindView(R.id.item_product_quantity)
        TextView mProductQuantity;
        @BindView(R.id.item_supplier_name)
        TextView mSupplierName;
        @BindView(R.id.item_supplier_phone)
        TextView mSupplierPhone;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(context, itemView);
        }
    }
}
