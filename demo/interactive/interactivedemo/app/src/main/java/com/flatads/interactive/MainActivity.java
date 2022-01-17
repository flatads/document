package com.flatads.interactive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.flatads.sdk.FlatAdSDK;
import com.flatads.sdk.callback.InitListener;
import com.flatads.sdk.callback.InteractiveAdListener;
import com.flatads.sdk.statics.ErrorCode;
import com.flatads.sdk.ui.view.InteractiveView;

public class MainActivity extends AppCompatActivity {

    private InteractiveView interactiveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        interactiveView = findViewById(R.id.interactive_view);
        Button btn = findViewById(R.id.show_btn);
        FlatAdSDK.initialize(getApplication(), "MYVLAHVE", "0cn8j59up3jj1088", new InitListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "SDK init success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
        btn.setOnClickListener(v -> loadInteractiveAd());
    }

    private void loadInteractiveAd() {
        interactiveView.setAdUnitId("ca1976b0-5d86-11ec-ac82-2526fceba055");
        interactiveView.setAdListener(new InteractiveAdListener() {
            @Override
            public void onAdExposure() {
                Toast.makeText(MainActivity.this, "onAdExposure", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClick() {
                Toast.makeText(MainActivity.this, "onAdClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClose() {
                Toast.makeText(MainActivity.this, "onAdClose", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoadSuc() {
                Toast.makeText(MainActivity.this, "onAdLoadSuc", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoadFail(ErrorCode errorCode) {
                Toast.makeText(MainActivity.this, "onAdLoadFail: " + errorCode.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        interactiveView.loadAd();
    }
}