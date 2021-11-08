package com.example.showpdf;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PDFView pdfView;
    private Button selectPdf;
    private final int PICK_PDF = 100;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pdfView = findViewById(R.id.pdfView);
        selectPdf = findViewById(R.id.selectPdf);
        getSupportActionBar().hide();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

  /*      List<String> testDeviceIds = Arrays.asList("37D6CEA4193FB625");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //         .setTestDeviceIds(Arrays.asList("37D6CEA4193FB625"))
                //      .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //      .addTestDevice("")
                .build();
*/
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        selectPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    checkP();
                Intent intent    = new Intent(MainActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        });

        onIntentView();
     //   mAdView.loadAd(adRequest);
    }

    private void checkP() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/pdf");
                        startActivityForResult(intent,PICK_PDF);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_PDF&&resultCode==RESULT_OK){
            if (data!=null){
         //       loadPDF(data.getData());

                Intent intent    = new Intent(MainActivity.this, ResultActivity.class);
                intent.setData(data.getData());
                startActivity(intent);
            }
        }
    }

    private void loadPDF(Uri data) {
        pdfView.fromUri(data).load();

    }
    private void onIntentView(){

 /*           Intent intent    = new Intent(MainActivity.this, ResultActivity.class);
            intent.setData(data.getData());
            startActivity(intent);
*/
        Intent intent = getIntent();
        String intentType = intent.getType();
        String intentAction = intent.getAction();
        if (intent!=null){
            if (Intent.ACTION_VIEW.equals(intentAction)){
                if (intentType.startsWith("application/pdf")){
                    Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                    loadPDF(uri);
                }
            }
        }
    }
}