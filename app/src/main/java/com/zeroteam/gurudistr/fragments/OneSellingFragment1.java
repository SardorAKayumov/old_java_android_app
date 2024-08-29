package com.zeroteam.gurudistr.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.instacart.library.truetime.TrueTime;
import com.zeroteam.gurudistr.OneSelling;
import com.zeroteam.gurudistr.R;
import com.zeroteam.gurudistr.StaticHolderClass;
import com.zeroteam.gurudistr.holders.ProductsListHolder;
import com.zeroteam.gurudistr.models.ProductModel;
import com.zeroteam.gurudistr.models.SaleModel;
import com.zeroteam.gurudistr.models.StoreModel;
import com.zeroteam.gurudistr.models.TerritoryModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OneSellingFragment1 extends Fragment implements View.OnClickListener {
    private FirestoreRecyclerAdapter<ProductModel, ProductsListHolder> firestoreRecyclerAdapter;
    private Button debtButton;
    private Button paidButton;
    private String uid;
    private TerritoryModel territoryModel;
    private StoreModel storeModel;

    private RecyclerView recyclerView;
    private TextView emptyView;
    private ProgressBar progressBar;
    private View fog;

    private Map<String, String> productNames;
    private Map<String, Long> productPrices;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View oneSellingViewFragment1 = inflater.inflate(R.layout.fragment_one_selling_1, container, false);

        recyclerView = oneSellingViewFragment1.findViewById(R.id.store_trade_list);
        emptyView = oneSellingViewFragment1.findViewById(R.id.empty_view_trade_product);
        progressBar = oneSellingViewFragment1.findViewById(R.id.progress_bar_trades_list);
        fog = oneSellingViewFragment1.findViewById(R.id.fog_trades_list);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(TrueTime.now().getTime());
        String dayOfWeek="";
        switch (calendar.get(Calendar.DAY_OF_WEEK)-1){
            case 1:
                dayOfWeek="Пн";
                break;
            case 2:
                dayOfWeek="Вт";
                break;
            case 3:
                dayOfWeek="Ср";
                break;
            case 4:
                dayOfWeek="Чт";
                break;
            case 5:
                dayOfWeek="Пт";
                break;
            case 6:
                dayOfWeek="Сб";
                break;
            case 7:
                dayOfWeek="Вс";
                break;
        }
        String month=String.valueOf(calendar.get(Calendar.MONTH)+1);
        if(month.length()==1){
            month = "0"+month;
        }
        String dayOfMonth=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if(dayOfMonth.length()==1){
            dayOfMonth = "0"+dayOfMonth;
        }
        String date = dayOfWeek+" "+dayOfMonth+"."+month+"."+calendar.get(Calendar.YEAR);
        TextView dateView = oneSellingViewFragment1.findViewById(R.id.dateOneSelling);
        dateView.setText(date);

        debtButton = oneSellingViewFragment1.findViewById(R.id.debt_btn);
        paidButton = oneSellingViewFragment1.findViewById(R.id.paid_btn);

        debtButton.setOnClickListener(this);
        paidButton.setOnClickListener(this);

        territoryModel = ((OneSelling) getActivity()).getTerritoryModel();
        storeModel = ((OneSelling) getActivity()).getStoreModel();
        uid = ((OneSelling) getActivity()).getUid();

        CollectionReference productsRef = FirebaseFirestore.getInstance().collection("products");

        Query query = productsRef.orderBy("id", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ProductModel> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(query, ProductModel.class)
                .build();

        productNames = new HashMap<>();
        productPrices = new HashMap<>();

        final TextView totalPriceView = oneSellingViewFragment1.findViewById(R.id.totalPriceOneSelling);

        firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<ProductModel, ProductsListHolder>(firestoreRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductsListHolder holder, int position, @NonNull ProductModel model) {
                        holder.setProductsList(model, storeModel, totalPriceView);
                    }

                    @NonNull
                    @Override
                    public ProductsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_store_trade, parent, false);
                        return new ProductsListHolder(view);
                    }

                    @Override
                    public void onDataChanged() {
                        super.onDataChanged();
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                            fog.setVisibility(View.GONE);
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

        return oneSellingViewFragment1;
    }

    @Override
    public void onStart(){
        super.onStart();
        firestoreRecyclerAdapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        if (firestoreRecyclerAdapter != null) {
            firestoreRecyclerAdapter.stopListening();
        }
    }

    private void setEnabled(boolean enabled){
        debtButton.setEnabled(enabled);
        paidButton.setEnabled(enabled);
        recyclerView.setEnabled(enabled);
        if(enabled) {
            progressBar.setVisibility(View.GONE);
            fog.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            fog.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if(StaticHolderClass.productQuantities.isEmpty())
            return;
        if(view==debtButton || view==paidButton){
            setEnabled(false);
            final Boolean izAllPaid;
            izAllPaid = view != debtButton;

            final DocumentReference storeRef = FirebaseFirestore.getInstance().collection("territories").document(territoryModel.getId()).collection("stores").document(storeModel.getId());
            storeRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        final long lastId;
                        StoreModel tempStoreModel = Objects.requireNonNull(task.getResult()).toObject(StoreModel.class);
                        if (Objects.requireNonNull(tempStoreModel).getLastSaleId() != null) {
                            lastId = tempStoreModel.getLastSaleId();
                        } else {
                            lastId = 0;
                        }
                        ObservableSnapshotArray<ProductModel> productModels = firestoreRecyclerAdapter.getSnapshots();
                        Map<String, Long> productQuantities = new HashMap<>();
                        long someI = 1;
                        for(Map.Entry<String, Long> entry : StaticHolderClass.productQuantities.entrySet()){
                            productQuantities.put(String.valueOf(someI), entry.getValue());
                            someI++;
                        }
                        long i = 1;
                        long someI2 = 1;
                        for(ProductModel productModel : productModels) {
                            String index = String.valueOf(i);
                            String someIndex2 = String.valueOf(someI2);
                            if(!StaticHolderClass.productQuantities.containsKey(index)) {
                                i++;
                                continue;
                            }
                            productNames.put(someIndex2, productModel.getName());
                            if(storeModel.getIzOptom()!=null && storeModel.getIzOptom()) {
                                productPrices.put(someIndex2, productModel.getOptomPrice());
                            }
                            else {
                                productPrices.put(someIndex2, productModel.getDefaultPrice());
                            }
                            i++;
                            someI2++;
                        }
                        SaleModel saleModel = new SaleModel(lastId+1, TrueTime.now().getTime(), productNames, productPrices, productQuantities,null, izAllPaid, izAllPaid);
                        storeRef.collection("sales").add(saleModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                String lastTradeStatus;
                                if(izAllPaid)
                                    lastTradeStatus="Paid";
                                else
                                    lastTradeStatus="Debt";
                                Map<String, Object> fieldsList = new HashMap<>();
                                fieldsList.put("lastSaleId", lastId+1);
                                fieldsList.put("lastTradeStatus", lastTradeStatus);
                                storeRef.update(fieldsList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        setEnabled(true);
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
    }
}
