package com.zeroteam.gurudistr.holders;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeroteam.gurudistr.R;
import com.zeroteam.gurudistr.StaticHolderClass;
import com.zeroteam.gurudistr.models.ProductModel;
import com.zeroteam.gurudistr.models.StoreModel;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class ProductsListHolder extends RecyclerView.ViewHolder {
    private TextView productName;
    private EditText productQuantity;
    private TextView productPrice;
    private TextView productTotalPrice;
    private LinearLayout mainLayout;

    public ProductsListHolder(@NonNull View itemView) {
        super(itemView);
        productName = itemView.findViewById(R.id.product_name_view);
        productQuantity = itemView.findViewById(R.id.product_quantity_view);
        productPrice = itemView.findViewById(R.id.product_price_view);
        productTotalPrice = itemView.findViewById(R.id.product_total_price_view);
        mainLayout = itemView.findViewById(R.id.trade_product_item_layout);
    }

    public void setProductsList(final ProductModel productModel, StoreModel storeModel, final TextView totalPriceView) {
        final NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        final long price;
        productName.setText(productModel.getName());
        if(storeModel.getIzOptom()==null)
            price = productModel.getDefaultPrice();
        else if(storeModel.getIzOptom())
            price = productModel.getOptomPrice();
        else
            price = productModel.getDefaultPrice();
        productPrice.setText(formatter.format(price));

        if(getAdapterPosition()%2==0)
            mainLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));

        productQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                long totalPrice = 0;
                if(!productQuantity.getText().toString().isEmpty()) {
                    totalPrice = Long.parseLong(productQuantity.getText().toString()) * price;
                    StaticHolderClass.productsPrices.put(String.valueOf((getAdapterPosition() + 1L)), totalPrice);
                } else {
                    StaticHolderClass.productsPrices.remove(String.valueOf((getAdapterPosition() + 1L)));
                }
                productTotalPrice.setText(formatter.format(totalPrice));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!productQuantity.getText().toString().isEmpty()) {
                    StaticHolderClass.productQuantities.put(String.valueOf((getAdapterPosition() + 1L)), Long.parseLong(editable.toString()));
                }
                else {
                    StaticHolderClass.productQuantities.remove(String.valueOf((getAdapterPosition() + 1L)));
                }
                long totalPrice = 0;
                if(!StaticHolderClass.productsPrices.isEmpty()){
                    for(Map.Entry<String, Long> entry : StaticHolderClass.productsPrices.entrySet()){
                        totalPrice+=entry.getValue();
                    }
                }
                totalPriceView.setText(formatter.format(totalPrice));
            }
        });
    }
}
