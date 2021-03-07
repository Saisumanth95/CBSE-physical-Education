package com.CBSEPhysicalEducationSolutions.cbsepe11thand12th;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import static android.content.ContentValues.TAG;

public class DetailsActivity extends AppCompatActivity {

    TextView title,desc,link,grade,created;
    ImageView imageView;
    ImageButton imageButton;
    ProgressBar progressBar;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().hide();


        mAdView = (AdView) this.findViewById(R.id.adView);
        mAdView.loadAd(SplashActivity.adRequest);

        title = findViewById(R.id.title2);
        desc = findViewById(R.id.desc2);
        link = findViewById(R.id.link);
        grade = findViewById(R.id.grade);
        created = findViewById(R.id.created);
        imageView = findViewById(R.id.imageView);
        imageButton = findViewById(R.id.pdf);
        progressBar = findViewById(R.id.progress_load_photo1);

        Intent intent = getIntent();

        final int position = intent.getIntExtra("pos",0);

        progressBar.setVisibility(View.VISIBLE);

        title.setText(MainActivity.item.get(position).getTitle());
        desc.setText(MainActivity.item.get(position).getDesc());

        String youtubelink = MainActivity.item.get(position).getLink();

        if(youtubelink == null || youtubelink == ""){
            youtubelink = "---- NO LINK ----";
        }


        link.setText(youtubelink);
        grade.setText(MainActivity.item.get(position).getGrade() + "th Standard");

        CharSequence dateChar = DateFormat.format("EEEE, MMM d,yyyy h:mm a",MainActivity.item.get(position).getCreated().toDate());

        created.setText("Created - " + dateChar);
        Picasso.with(this).load(MainActivity.item.get(position).getImage()).error(R.drawable.pelogo).fit().into(imageView, new Callback() {

            @Override
            public void onSuccess() {


                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onError() {

                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onError: download failed");

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.item.get(position).getPdf().isEmpty()){

                    Toast.makeText(getApplicationContext(),"No PDF for this topic",Toast.LENGTH_SHORT).show();

                }else{

                    Intent intent1 = new Intent(getApplicationContext(),PDFActivity.class);

                    intent1.putExtra("s",1);

                    intent1.putExtra("pos",position);

                    startActivity(intent1);


                }

            }
        });


    }

}