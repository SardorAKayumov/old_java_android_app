package com.zeroteam.gurudistr.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.zeroteam.gurudistr.OneSelling;
import com.zeroteam.gurudistr.R;
import com.zeroteam.gurudistr.holders.SalesHistoryListHolder;
import com.zeroteam.gurudistr.models.SaleModel;
import com.zeroteam.gurudistr.models.StoreModel;
import com.zeroteam.gurudistr.models.TerritoryModel;

public class OneSellingFragment2 extends Fragment implements AdapterView.OnItemSelectedListener {
    private FirestoreRecyclerAdapter<SaleModel, SalesHistoryListHolder> firestoreRecyclerAdapter;

    private Spinner spinner;

    private RecyclerView recyclerView;
    private TextView emptyView;
    private ProgressBar progressBar;

    private String uid;
    private TerritoryModel territoryModel;
    private StoreModel storeModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View oneSellingViewFragment2 = inflater.inflate(R.layout.fragment_one_selling_2, container, false);

        spinner = oneSellingViewFragment2.findViewById(R.id.sellsHistoryListSpinner);
        recyclerView = oneSellingViewFragment2.findViewById(R.id.sellsHistoryList);
        emptyView = oneSellingViewFragment2.findViewById(R.id.empty_view_sales);
        progressBar = oneSellingViewFragment2.findViewById(R.id.progress_bar_sells_history);

        territoryModel = ((OneSelling) getActivity()).getTerritoryModel();
        storeModel = ((OneSelling) getActivity()).getStoreModel();
        uid = ((OneSelling) getActivity()).getUid();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(oneSellingViewFragment2.getContext(), R.array.sellsHistoryListModes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        CollectionReference productsRef = FirebaseFirestore.getInstance().collection("territories").document(territoryModel.getId()).collection("stores").document(storeModel.getId()).collection("sales");

        Query query = productsRef.orderBy("id", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<SaleModel> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<SaleModel>()
                .setQuery(query, SaleModel.class)
                .build();

        firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<SaleModel, SalesHistoryListHolder>(firestoreRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull SalesHistoryListHolder holder, int position, @NonNull SaleModel model) {
                        holder.setSalesHistoryList(model);
                    }

                    @NonNull
                    @Override
                    public SalesHistoryListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_sales_history, parent, false);
                        return new SalesHistoryListHolder(view);
                    }

                    @Override
                    public void onDataChanged() {
                        super.onDataChanged();
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
                };
        recyclerView.setAdapter(firestoreRecyclerAdapter);

        return oneSellingViewFragment2;
    }

    @Override
    public void onStart() {
        super.onStart();
        firestoreRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firestoreRecyclerAdapter != null) {
            firestoreRecyclerAdapter.stopListening();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
