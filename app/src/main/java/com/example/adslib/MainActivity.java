package com.example.adslib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adsadmobandfan.utils.Ads;
import com.example.moreapp.MoreAppConfig;
import com.example.ratedialog.RatingDialog;

import java.io.IOException;
import java.io.InputStream;

import static com.example.adslib.R.id;
import static com.example.adslib.R.layout;

public class MainActivity extends AppCompatActivity implements RatingDialog.RatingDialogInterFace {
    FrameLayout frmNative, frmBanner;
    String data;

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getResources().getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        initView();
        data =  loadJSONFromAsset();
        Log.e("TAG", "onCreate: " + data );
        MoreAppConfig.setMoreAppConfigs(data);
        Ads.getInstance(this).initAds();

    }

    private void initView() {
        findViewById(R.id.btnInter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ads.getInstance(MainActivity.this).inter(new Ads.CallBackInter() {
                    @Override
                    public void adClose() {

                    }

                    @Override
                    public void adLoadFailed(int i) {

                    }
                });
            }
        });
        findViewById(id.btnInterNative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ads.getInstance(MainActivity.this).interNative(new Ads.CallBackInter() {
                    @Override
                    public void adClose() {

                    }

                    @Override
                    public void adLoadFailed(int i) {

                    }
                });
            }
        });
        findViewById(R.id.btnRate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateManual(MainActivity.this);
            }
        });
        frmBanner = findViewById(id.frmBanner);
        frmNative = findViewById(id.frmNative);
        Ads.getInstance(this).banner(frmBanner, Ads.AdsSize.LARGE);
        Ads.getInstance(this).bannerNative(frmNative, Ads.AdsSize.MEDIUM);
    }

    void rateManual(Context context) {
        RatingDialog ratingDialog = new RatingDialog(context);
        ratingDialog.setRatingDialogListener((RatingDialog.RatingDialogInterFace) this);
        ratingDialog.showDialog();
    }

    @Override
    public void onDismiss() {

    }

    @Override
    public void onSubmit(float rating) {
        if (rating > 3) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onRatingChanged(float rating) {

    }
}