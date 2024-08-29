package com.zeroteam.gurudistr;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.zeroteam.gurudistr.holders.StoresListHolder;
import com.zeroteam.gurudistr.models.StoreModel;
import com.zeroteam.gurudistr.models.TerritoryModel;

public class StoresList extends AppCompatActivity {
    private String uid;
    private TerritoryModel territoryModel;
    private CollectionReference storesRef;

    private FirestoreRecyclerAdapter<StoreModel, StoresListHolder> firestoreRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uid = getIntent().getStringExtra("uid");
        territoryModel = (TerritoryModel) getIntent().getSerializableExtra("territoryModel");

        storesRef = FirebaseFirestore.getInstance().collection("territories").document(territoryModel.getId()).collection("stores");

        final RecyclerView recyclerView = findViewById(R.id.storesRecyclerView);
        final TextView emptyView = findViewById(R.id.empty_view_stores);
        final ProgressBar progressBar = findViewById(R.id.progress_bar_stores_list);

        Query query = storesRef.orderBy("id", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<StoreModel> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<StoreModel>()
                .setQuery(query, StoreModel.class)
                .build();

        firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<StoreModel, StoresListHolder>(firestoreRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull StoresListHolder holder, int position, @NonNull StoreModel model) {
                        holder.setStoresList(model, territoryModel, uid);
                    }

                    @NonNull
                    @Override
                    public StoresListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_stores, parent, false);
                        return new StoresListHolder(view);
                    }

                    @Override
                    public void onDataChanged() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }

                        if (getItemCount() == 0) {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public int getItemCount() {
                        return super.getItemCount();
                    }
                };
        recyclerView.setAdapter(firestoreRecyclerAdapter);
    }

    @Override
    protected void onStart(){
        super.onStart();
        firestoreRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if (firestoreRecyclerAdapter != null) {
            firestoreRecyclerAdapter.stopListening();
        }
    }
}

