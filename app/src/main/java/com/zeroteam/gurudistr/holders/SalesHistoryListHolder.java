package com.zeroteam.gurudistr.holders;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeroteam.gurudistr.R;
import com.zeroteam.gurudistr.models.SaleModel;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class SalesHistoryListHolder extends RecyclerView.ViewHolder{
    private final NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
    private RecyclerView recyclerView;
    private TextView totalPrice;

    public SalesHistoryListHolder(@NonNull View itemView) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.sellsHistoryProductsList);
        totalPrice = itemView.findViewById(R.id.history_sale_total_price);
    }

    public void setSalesHistoryList(final SaleModel saleModel){
        class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
            private Map<String, String> productNames;
            private Map<String, Long> productQuantities;
            private Map<String, Long> productPrices;

            private MyAdapter(SaleModel saleModel) {
                productNames = saleModel.getProductNames();
                productQuantities = saleModel.getProductQuantities();
                productPrices = saleModel.getProductPrices();
            }

            String getTotalPrice(){
                if(productNames.isEmpty())
                    return "Null";
                long totalPrice = 0;
                for(Map.Entry<String, Long> entry : productQuantities.entrySet()){
                    totalPrice+=entry.getValue()*productPrices.get(entry.getKey());
                }
                return formatter.format(totalPrice);
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_list_products_sales_history, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                position+=1;
                String key = String.valueOf(position);
                long quantity = productQuantities.get(key);
                long price = productPrices.get(key);
                holder.productName.setText(productNames.get(key));
                holder.productQuantity.setText(formatter.format(quantity));
                holder.productPrice.setText(formatter.format(price));
                holder.totalPriceView.setText(formatter.format(price * quantity));
                if (position % 2 == 0)
                    holder.mainLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

            @Override
            public int getItemCount() {
                return productNames.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                private TextView productName;
                private TextView productQuantity;
                private TextView productPrice;
                private TextView totalPriceView;
                private LinearLayout mainLayout;

                ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    productName = itemView.findViewById(R.id.history_product_name_view);
                    productQuantity = itemView.findViewById(R.id.history_product_quantity_view);
                    productPrice = itemView.findViewById(R.id.history_product_price_view);
                    totalPriceView = itemView.findViewById(R.id.history_product_total_price_view);
                    mainLayout = itemView.findViewById(R.id.history_product_item_layout);
                }
            }
        }

        MyAdapter adapter = new MyAdapter(saleModel);
        recyclerView.setAdapter(adapter);
        totalPrice.setText(adapter.getTotalPrice());

    }
}
