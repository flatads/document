package com.flatads.interactive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.flatads.sdk.FlatAdSDK;
import com.flatads.sdk.builder.InterstitialAd;
import com.flatads.sdk.builder.NativeAd;
import com.flatads.sdk.builder.RewardedAd;
import com.flatads.sdk.callback.BannerAdListener;
import com.flatads.sdk.callback.InitListener;
import com.flatads.sdk.callback.InteractiveAdListener;
import com.flatads.sdk.callback.InterstitialAdListener;
import com.flatads.sdk.callback.NativeAdListener;
import com.flatads.sdk.callback.RewardedAdListener;
import com.flatads.sdk.config.SdkConfig;
import com.flatads.sdk.response.Ad;
import com.flatads.sdk.statics.ErrorCode;
import com.flatads.sdk.ui.view.BannerAdView;
import com.flatads.sdk.ui.view.InteractiveView;
import com.flatads.sdk.ui.view.MediaView;
import com.flatads.sdk.ui.view.NativeAdLayout;

import java.util.ArrayList;
import java.util.List;

public class FlatActivity extends Activity implements View.OnClickListener {
    private Spinner ad_text;
    private Spinner ad_id;
    private BannerAdView bannerAdView;
    private InterstitialAd interstitialAd;
    private RewardedAd rewardedAd;
    private NativeAd nativeAd;
    private ViewGroup container;
    private InteractiveView interactiveView;
    private NativeAdLayout adView;
    private FrameLayout frameLayout;
    private boolean isBiddingNative;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.flat_activity);
        String token;
        String appId;

        token = "0cn8j59up3jj1088";
        appId = "MYVLAHVE";

        FlatAdSDK.initialize(getApplication(), appId, token, new InitListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(FlatActivity.this, "init success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });

        Button bidding = findViewById(R.id.bidding_btn);
        Button load = findViewById(R.id.load_btn);
        Button show = findViewById(R.id.show_btn);
        Button remove = findViewById(R.id.remove_btn);
        ad_text = findViewById(R.id.ad_text_spinner);
        ad_id = findViewById(R.id.ad_id_spinner);
        container = findViewById(R.id.container);
        interactiveView = findViewById(R.id.interactive_view);
        load.setOnClickListener(this);
        bidding.setOnClickListener(this);
        show.setOnClickListener(this);
        remove.setOnClickListener(this);
        handleSpinner();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bidding_btn:
                if (getSelectItem(ad_text).equals("banner")) {
                    bannerAdView = new BannerAdView(this);
                    bannerAdView.setAdUnitId(getSelectItem(ad_id));
                    bannerAdView.bidding((isGetAd, price) -> {
                        if (!isGetAd) {
                            Toast.makeText(FlatActivity.this, "not get ad", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        bannerAdView.winBidding();
                        Toast.makeText(FlatActivity.this, "price:" + price, Toast.LENGTH_SHORT).show();
                        bannerAdView.setAdListener(new BannerAdListener() {
                            @Override
                            public void onAdExposure() {
                                Toast.makeText(FlatActivity.this, "show success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdClick() {
                                Toast.makeText(FlatActivity.this, "click", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdClose() {
                                Toast.makeText(FlatActivity.this, "close", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdLoadSuc() {
                                Toast.makeText(FlatActivity.this, "load success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdLoadFail(ErrorCode errorCode) {
                                Toast.makeText(FlatActivity.this, "load fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                        bannerAdView.setBannerSize(BannerAdView.SMALL_LAYOUT);
                        FrameLayout.LayoutParams layoutParams =
                                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                        bannerAdView.setLayoutParams(layoutParams);
                        container.addView(bannerAdView);
                        bannerAdView.loadAd();
                    });
                } else if (getSelectItem(ad_text).equals("native")) {
                    nativeAd = new NativeAd(FlatActivity.this, getSelectItem(ad_id));
                    nativeAd.bidding((isGetAd, price) -> {
                        if (!isGetAd) {
                            Toast.makeText(FlatActivity.this, "not get ad", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        isBiddingNative = true;
                        nativeAd.winBidding();
                        Toast.makeText(FlatActivity.this, "price:" + price, Toast.LENGTH_SHORT).show();
                        nativeAd.setAdListener(new NativeAdListener() {
                            @Override
                            public void onAdLoadSuc(Ad ad) {
                                Toast.makeText(FlatActivity.this, "load success", Toast.LENGTH_SHORT).show();
                                inflateAd(ad);
                            }

                            @Override
                            public void onAdLoadFail(ErrorCode errorCode) {
                                Toast.makeText(FlatActivity.this, "load fail", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdExposure() {
                                Toast.makeText(FlatActivity.this, "onAdExposure", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdClick() {
                                Toast.makeText(FlatActivity.this, "click", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdDestroy() {
                                Toast.makeText(FlatActivity.this, "destroy", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                } else if (getSelectItem(ad_text).equals("interstitial")) {
                    interstitialAd = new InterstitialAd(this, getSelectItem(ad_id));
                    interstitialAd.bidding((isGetAd, price) -> {
                        if (!isGetAd) {
                            Toast.makeText(FlatActivity.this, "not get ad", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        interstitialAd.winBidding();
                        Toast.makeText(FlatActivity.this, "price:" + price, Toast.LENGTH_SHORT).show();
                        interstitialAd.setAdListener(new InterstitialAdListener() {
                            @Override
                            public void onAdLoadSuc() {
                                Toast.makeText(FlatActivity.this, "load success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdClose() {
                                Toast.makeText(FlatActivity.this, "close", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdLoadFail(ErrorCode errorCode) {
                                Toast.makeText(FlatActivity.this, "load fail", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdExposure() {
                                Toast.makeText(FlatActivity.this, "show success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdClick() {
                                Toast.makeText(FlatActivity.this, "click", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                } else if (getSelectItem(ad_text).equals("rewarded")) {
                    rewardedAd = new RewardedAd(this, getSelectItem(ad_id));
                    rewardedAd.bidding((isGetAd, price) -> {
                        if (!isGetAd) {
                            Toast.makeText(FlatActivity.this, "not get ad", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        rewardedAd.winBidding();
                        Toast.makeText(FlatActivity.this, "price:" + price, Toast.LENGTH_SHORT).show();
                        rewardedAd.setAdListener(new RewardedAdListener() {
                            @Override
                            public void onAdClose() {
                                Toast.makeText(FlatActivity.this, "close", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onUserEarnedReward() {
                                Toast.makeText(FlatActivity.this, "获取奖励", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdFailedToShow() {
                                Toast.makeText(FlatActivity.this, "播放失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdExposure() {
                                Toast.makeText(FlatActivity.this, "open", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdClick() {
                                Toast.makeText(FlatActivity.this, "click", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdLoadSuc() {
                                Toast.makeText(FlatActivity.this, "load success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdLoadFail(ErrorCode errorCode) {
                                Toast.makeText(FlatActivity.this, "load fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                }
                break;
            case R.id.load_btn:
                if (getSelectItem(ad_text).equals("banner")) {
                    bannerAdView = new BannerAdView(this);
                    bannerAdView.setBannerSize(BannerAdView.SMALL_LAYOUT);
                    FrameLayout.LayoutParams layoutParams =
                            new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                    bannerAdView.setLayoutParams(layoutParams);
                    container.addView(bannerAdView);
                    bannerAdView.setAdListener(new BannerAdListener() {
                        @Override
                        public void onAdExposure() {
                            Toast.makeText(FlatActivity.this, "show success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdClick() {
                            Toast.makeText(FlatActivity.this, "click", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdClose() {
                            Toast.makeText(FlatActivity.this, "close", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdLoadSuc() {
                            Toast.makeText(FlatActivity.this, "load success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdLoadFail(ErrorCode errorCode) {
                            Toast.makeText(FlatActivity.this, "load fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                    bannerAdView.setAdUnitId(getSelectItem(ad_id));
                    bannerAdView.loadAd();

                } else if (getSelectItem(ad_text).equals("native")) {
                    nativeAd = new NativeAd(FlatActivity.this, getSelectItem(ad_id));
                    nativeAd.setAdListener(new NativeAdListener() {
                        @Override
                        public void onAdLoadSuc(Ad ad) {
                            Toast.makeText(FlatActivity.this, "load success", Toast.LENGTH_SHORT).show();
                            inflateAd(ad);
                        }

                        @Override
                        public void onAdLoadFail(ErrorCode errorCode) {
                            Toast.makeText(FlatActivity.this, "load fail", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdExposure() {
                            Toast.makeText(FlatActivity.this, "onAdExposure", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdClick() {
                            Toast.makeText(FlatActivity.this, "click", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdDestroy() {
                            Toast.makeText(FlatActivity.this, "destroy", Toast.LENGTH_SHORT).show();
                        }
                    });
                    nativeAd.loadAd();

                } else if (getSelectItem(ad_text).equals("interstitial")) {
                    interstitialAd = new InterstitialAd(this, getSelectItem(ad_id));
                    interstitialAd.setAdListener(new InterstitialAdListener() {
                        @Override
                        public void onAdLoadSuc() {
                            Toast.makeText(FlatActivity.this, "load success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdClose() {
                            Toast.makeText(FlatActivity.this, "close", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdLoadFail(ErrorCode errorCode) {
                            Toast.makeText(FlatActivity.this, "load fail", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdExposure() {
                            Toast.makeText(FlatActivity.this, "show success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdClick() {
                            Toast.makeText(FlatActivity.this, "click", Toast.LENGTH_SHORT).show();
                        }
                    });
                    interstitialAd.loadAd();

                } else if (getSelectItem(ad_text).equals("rewarded")) {
                    rewardedAd = new RewardedAd(this, getSelectItem(ad_id));
                    rewardedAd.setAdListener(new RewardedAdListener() {
                        @Override
                        public void onAdClose() {
                            Toast.makeText(FlatActivity.this, "close", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onUserEarnedReward() {
                            Toast.makeText(FlatActivity.this, "获取奖励", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdFailedToShow() {
                            Toast.makeText(FlatActivity.this, "播放失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdExposure() {
                            Toast.makeText(FlatActivity.this, "open", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdClick() {
                            Toast.makeText(FlatActivity.this, "click", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdLoadSuc() {
                            Toast.makeText(FlatActivity.this, "load success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdLoadFail(ErrorCode errorCode) {
                            Toast.makeText(FlatActivity.this, "load fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                    rewardedAd.loadAd();
                } else {
                    interactiveView.setAdUnitId(getSelectItem(ad_id));
//                    interactiveView.setCacheTime(1000 * 10);
//                    interactiveView.setIconView(getResources().getDrawable(R.mipmap.flat_ic_info));
                    interactiveView.setAdListener(new InteractiveAdListener() {
                        @Override
                        public void onRenderSuccess() {
                            Toast.makeText(FlatActivity.this, "onRenderSuccess", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRenderFail(String msg, int code) {
                            Toast.makeText(FlatActivity.this, "onRenderFail" + msg + code, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdClick() {
                            Toast.makeText(FlatActivity.this, "onAdClick", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdClose() {
                            Toast.makeText(FlatActivity.this, "onAdClose", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdLoadSuc() {
                            Toast.makeText(FlatActivity.this, "onAdLoadSuc", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdLoadFail(ErrorCode errorCode) {
                            Toast.makeText(FlatActivity.this, "onAdLoadFail" + errorCode.getCode() + errorCode.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    interactiveView.loadAd();
                }
                break;
            case R.id.show_btn:
                if (getSelectItem(ad_text).equals("native")) {
                    frameLayout = findViewById(R.id.native_ad);
                    if (isBiddingNative && nativeAd != null) nativeAd.loadAd();
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                } else if (getSelectItem(ad_text).equals("interstitial")) {
                    if (interstitialAd == null) return;
                    interstitialAd.show();
                } else if (getSelectItem(ad_text).equals("rewarded")) {
                    if (rewardedAd == null) return;
                    rewardedAd.show();
                }
                break;
            case R.id.remove_btn:
                if (bannerAdView != null) {
                    container.removeView(bannerAdView);
                }
                if (frameLayout != null && adView != null) {
                    frameLayout.removeView(adView);
                }
                Toast.makeText(this, "remove ad", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void inflateAd(Ad ad) {

        adView = (NativeAdLayout) getLayoutInflater().inflate(R.layout.native_layout, null);


        TextView tvTitle = adView.findViewById(R.id.flat_ad_tv_title);
        TextView tvDesc = adView.findViewById(R.id.flat_ad_tv_desc);
        TextView tvAdBtn = adView.findViewById(R.id.flat_ad_button);
        View view = adView.findViewById(R.id.flat_ad_container);
        ImageView icon = adView.findViewById(R.id.flat_ad_iv_icon);
        MediaView mediaView = adView.findViewById(R.id.flat_ad_media_big);

        tvTitle.setText(ad.getTitle());
        tvDesc.setText(ad.getDesc());
        tvAdBtn.setText(ad.getAdBtn());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(tvAdBtn);
        clickableViews.add(view);

        nativeAd.registerViewForInteraction(adView, mediaView, icon, clickableViews);

    }

    private void handleSpinner() {
        setAdapter(ad_text, R.array.ad_text);
        ad_text.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        setAdapter(ad_id, R.array.banner_id);
                        break;
                    case 1:
                        setAdapter(ad_id, R.array.native_id);
                        break;
                    case 2:
                        setAdapter(ad_id, R.array.interstitial_id);
                        break;
                    case 3:
                        setAdapter(ad_id, R.array.rewarded_id);
                        break;
                    case 4:
                        setAdapter(ad_id, R.array.interactive_id);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void setAdapter(Spinner spinner, int textId) {
        SpinnerAdapter adapter = ArrayAdapter.createFromResource(this, textId, R.layout.item_select);
        spinner.setAdapter(adapter);
    }

    private String getSelectItem(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bannerAdView != null) {
            bannerAdView.resume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bannerAdView != null) {
            bannerAdView.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bannerAdView != null) {
            bannerAdView.destroy();
        }
        if (adView != null) {
            adView.destroy();
        }
        if (interactiveView != null) {
            interactiveView.onDestroy();
        }
    }
}