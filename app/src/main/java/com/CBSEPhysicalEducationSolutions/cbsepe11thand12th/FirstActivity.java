package com.CBSEPhysicalEducationSolutions.cbsepe11thand12th;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirstActivity extends AppCompatActivity {

    ImageView imageView;
    private AdView mAdView;
    Button b1,b2,s;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        getSupportActionBar().hide();


        mAdView = (AdView) this.findViewById(R.id.adView);
        mAdView.loadAd(SplashActivity.adRequest);

        imageView = findViewById(R.id.imageView2);
        b1 = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);
        s = findViewById(R.id.syllabus);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("grade",11);
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("grade",12);
                startActivity(intent);
            }
        });

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance()
                        .collection("syllabus")
                        .document("syllab")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                url = documentSnapshot.getString("sUrl");
                                Intent intent = new Intent(FirstActivity.this,PDFActivity.class);
                                intent.putExtra("url",url);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Unknown error occured",Toast.LENGTH_SHORT).show();
                            }
                        });



            }
        });


    }
}