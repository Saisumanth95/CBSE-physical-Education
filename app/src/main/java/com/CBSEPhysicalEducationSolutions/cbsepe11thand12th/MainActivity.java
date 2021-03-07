package com.CBSEPhysicalEducationSolutions.cbsepe11thand12th;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.*;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import hotchemi.android.rate.AppRate;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static ArrayList<Item> item;
    public int garde;
    TextView titlebar,error;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        AppRate.with(this)
                .setInstallDays(3)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);

        mAdView = (AdView) this.findViewById(R.id.adView);
        mAdView.loadAd(SplashActivity.adRequest);

        item = new ArrayList<>();


        titlebar = findViewById(R.id.titlebar);
        recyclerView = findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        adapter = new Adapter(item,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        error = findViewById(R.id.errorView);
        error.setVisibility(View.GONE);

        Intent i = getIntent();

        garde = i.getIntExtra("grade",11);

        titlebar.setText(garde + "th Standard");



        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);

                intent.putExtra("pos",position);

                startActivity(intent);


            }
        });


        onLoadingRefresh();

    }



    public void retrieveData()
    {

        swipeRefreshLayout.setRefreshing(true);

        FirebaseFirestore.getInstance().collection("notes")
                .whereEqualTo("grade",garde)
                .orderBy("created", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                item.add(new Item(document.getString("title"),
                                        document.getString("desc"),
                                        document.getString("link"),
                                        document.getString("image"),
                                        document.getString("pdf"),
                                        Math.toIntExact(document.getLong("grade")),
                                        document.getTimestamp("created")));

                                adapter.notifyDataSetChanged();

                            }

                            if(item.size() == 0){

                                error.setVisibility(View.VISIBLE);

                            }

                            swipeRefreshLayout.setRefreshing(false);


                        } else {
                            Log.d("This is test", "Error getting documents: ", task.getException());
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });

    }

    @Override
    public void onRefresh() {

        item.clear();
        retrieveData();
    }

    private void onLoadingRefresh() {

        swipeRefreshLayout.post(

                new Runnable() {
                    @Override
                    public void run() {
                        item.clear();
                        retrieveData();
                    }
                }
        );

    }


}