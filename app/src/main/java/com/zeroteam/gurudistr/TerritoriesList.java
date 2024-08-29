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
import com.zeroteam.gurudistr.holders.TerritoriesListHolder;
import com.zeroteam.gurudistr.models.TerritoryModel;

public class TerritoriesList extends AppCompatActivity {
    private String uid;
    private CollectionReference territoriesRef;

    private FirestoreRecyclerAdapter<TerritoryModel, TerritoriesListHolder> firestoreRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_territories_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uid = getIntent().getStringExtra("uid");

        territoriesRef = FirebaseFirestore.getInstance().collection("territories");


        final RecyclerView recyclerView = findViewById(R.id.territoriesRecyclerView);
        final TextView emptyView = findViewById(R.id.empty_view_territories);
        final ProgressBar progressBar = findViewById(R.id.progress_bar_territories_list);

        Query query = territoriesRef.orderBy("id", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<TerritoryModel> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<TerritoryModel>()
                .setQuery(query, TerritoryModel.class)
                .build();

        firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<TerritoryModel, TerritoriesListHolder>(firestoreRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull TerritoriesListHolder holder, int position, @NonNull TerritoryModel model) {
                        holder.setTerritoriesList(model, uid);
                    }

                    @NonNull
                    @Override
                    public TerritoriesListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_territories, parent, false);
                        return new TerritoriesListHolder(view);
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
