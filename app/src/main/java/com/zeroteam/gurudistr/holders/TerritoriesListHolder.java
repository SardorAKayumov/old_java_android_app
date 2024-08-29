package com.zeroteam.gurudistr.holders;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeroteam.gurudistr.R;
import com.zeroteam.gurudistr.StoresList;
import com.zeroteam.gurudistr.models.TerritoryModel;

import java.io.Serializable;

public class TerritoriesListHolder extends RecyclerView.ViewHolder {
    private TextView territoryName;

    public TerritoriesListHolder(@NonNull View itemView) {
        super(itemView);
        territoryName = itemView.findViewById(R.id.territoryNameItem);
    }

    public void setTerritoriesList(final TerritoryModel territoryModel, final String uid) {
        territoryName.setText(territoryModel.getName());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StoresList.class);
                intent.putExtra("territoryModel", (Serializable) territoryModel);
                intent.putExtra("uid", uid);
                view.getContext().startActivity(intent);
            }
        });
    }
}
