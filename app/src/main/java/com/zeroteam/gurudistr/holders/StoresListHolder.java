package com.zeroteam.gurudistr.holders;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeroteam.gurudistr.OneSelling;
import com.zeroteam.gurudistr.R;
import com.zeroteam.gurudistr.models.StoreModel;
import com.zeroteam.gurudistr.models.TerritoryModel;

import java.io.Serializable;

public class StoresListHolder extends RecyclerView.ViewHolder {
    private TextView storeName;
    private TextView lastSaleStatus;

    public StoresListHolder(@NonNull View itemView) {
        super(itemView);
        storeName = itemView.findViewById(R.id.storeNameItem);
        lastSaleStatus = itemView.findViewById(R.id.lastSellStatusItem);
    }

    public void setStoresList(final StoreModel storeModel, final TerritoryModel territoryModel, final String uid) {
        storeName.setText(storeModel.getName());
        lastSaleStatus.setText(storeModel.getLastTradeStatus());
        switch (storeModel.getLastTradeStatus()) {
            case "Debt":
                lastSaleStatus.setTextColor(Color.parseColor("#FFCC0000"));
                break;
            case "Paid":
                lastSaleStatus.setTextColor(Color.parseColor("#FF669900"));
                break;
            case "No Sales":
                lastSaleStatus.setTextColor(Color.parseColor("#FFAAAAAA"));
                break;
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), OneSelling.class);
                intent.putExtra("territoryModel", (Serializable) territoryModel);
                intent.putExtra("storeModel", (Serializable) storeModel);
                intent.putExtra("uid", uid);
                view.getContext().startActivity(intent);
            }
        });
    }
}

