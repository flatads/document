# Flat Ads SDK入门指南
欢迎您选择Flat Ads对接合作，这篇文档将指引您如何操作开发者后台并对接我们的SDK开始您的广告变现，如果您有任何问题请联系您的客户经理。
### 创建账号
请联系您的客户经理为您创建Flat Ads 开发者账号。
### 创建APP和广告位
1. 进入开发者后台后，您需要先为您的账号创建APP/Site及其Placement.
![Alt text](./flat_image/1620963109747.png)
2. 进入新建弹窗，输入您APP的相关信息, 填写的字段释义如下
![Alt text](./flat_image/1620964293507.png)


> Type: 流量类型是APP流量还是Web Site流量；
> 
> Platform：APP的系统平台，是Android还是IOS，如果是Windows之类的请选择其他；
> 
> Bundle：APP的包名；（如果您的Android APP已上架，填写包名后点击Look up，将自动填充Store URL和APP Category）
> 
> Store URL：APP上架商店的详情页链接；
> 
> Domain：Site的域名；
> 
> App Categroy：APP的分类；
> 
> Is app published：您的APP是否已上架；

> Status：APP/Site的状态，如果状态为关，其Appid和token将不再生效；
> 
> COPPA：如果您的APP/Site遵守COPPA，请勾选上；
> 
> TOKEN和Appid：为对接SDK时的验证密钥，新建APP后，自动生成。
> 

3. 完成APP/Site的填写后，保存继续为其创建广告位，填写的字段释义如下  
![Alt text](./flat_image/1620964462690.png)

> App/Site：当前创建广告位的归属，一个App/Site可以拥有多个广告位；
> 
> Name：广告位的名称，一般可以命名为"APPNAME_TYPE_SIZE"；
> 
> Bid Floor：广告位的底价，只有高于这个底价的广告才会填充；
> 
> Placement Type：广告位类型，Banner, Native, Interstitial, Rewarded Ad；
> 
> Size：广告位尺寸；
> 
> Allowed Ad Type：支持填充的广告类型；Display(HTML), Static, VAST Video, Playable；
> 
> Refresh Time：Banner广告自动刷新时间；
> 
> Muting：视频广告是否默认静音；
> 
> Allow Skip：允许跳过的时间，单位为秒；
> 
> CTA Popup：是否在视频播放过程中弹出CTA按钮，一般能够提升CTR，但是可能遮罩视频元素；
> 
> Status：广告位状态，状态为关时，广告位将不再请求广告；
> 

### 集成Flat Ads SDK
保存广告位后，就可以进入集成SDK的环节。Flat Ads SDK目前仅支持Android应用接入。同时如果您对接了Mopub聚合SDK，我们也只支持Mopub JS Tag作为自定义网盟接入。如果你是Site流量，也可以直接部署我们的JS Tag
![Alt text](./flat_image/1620983112998.png)

### 报表说明
跑出数据后，可以通过报表查看收益数据情况，以下是维度指标的说明
![Alt text](./flat_image/1620989538211.png)

> 维度
> 
> Date：日期
> 
> App/Site：APP或者是Site
> 
> Placement：广告位
> 
> Country：国家
> 
> 指标
> 
> Impression：广告展示
> 
> Clicks：广告点击
> 
> CTR：广告点击率，Clicks/Impression
> 
> eCPM：每千次展示单价，Revenue/Impression*1000
> 
> Revenue：广告收益
> 

# Android SDK
### 添加依赖和初始化
添加依赖

```
dependencies {
    implementation 'com.flatads.sdk:flatads:1.4.18.2-Flat' // 具体最新版本号请咨询商务
}


allprojects {
    repositories {
        maven {url "https://maven-pub.flat-ads.com/repository/maven-public/"}
        maven { url  "https://jitpack.io" }
    }
}

//代码混淆
如果您需要使用proguard混淆代码，需确保不要混淆SDK的代码
**注意**: SDK代码被混淆后会导致广告无法展现或者其它异常

-keep class com.flatads.sdk.** {*;} 
```

初始化SDK
```Java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        String appId = "xxxxxxxx"; //申请时的appid
        String token = "xxxxxxxxxxxxxxxx"; //申请时的token
        
        FlatAdSDK.initialize(getApplication(), appId, token, new InitListener(){
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int code, String msg) {
            }
        }, new SdkConfig());
    }
    
}
```

广告必须在初始化完成后调用！

初始化SDK时，需要传入APP的appid和token，这两个值可以在你的账户后台Placement管理页面中找到
 ![Alt text](./flat_image/1621243376897.png)

### 广告位类型
#### Banner 
* 使用Banner广告，需要向布局中添加BannerAdView
```
# main_activity.xml
    ···
    <com.flatads.sdk.ui.view.BannerAdView
        android:id="@+id/banner"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:banner_size="small_size"/>
    ···
```
app:banner_size：将此属性设置为要使用的广告尺寸。提供了2种类型的banner尺寸：small_size (320x50) 和 big_size (300x250)。不设置默认为big_size (300x250)。
布局中需给广告位设置 match_parent。提供了setBannerSize方法，该方法对布局进行操作，所以必须在主线程中执行。

MainActivity中，需要设置广告的unitid，并且调用loadAd()，展示广告。
```
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String appId = "xxxxxxxx"; //申请时的appid
        String token = "xxxxxxxxxxxxxxxx"; //申请时的token

        FlatAdSDK.initialize(getApplication(), appId, token, new InitListener(){
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int code, String msg) {
            }
        }, new SdkConfig());
        
        BannerAdView bannerAdView = findViewById(R.id.banner);
        bannerAdView.setAdUnitId("xxxxxxx");
        bannerAdView.loadAd();
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
    }

}
```
* 需要监听广告相关回调事件，在相关的AdView添加Listener。
```
    bannerAdView.setAdListener(new BannerAdListener() {
        @Override
        public void onAdExposure() {
            // 广告曝光
        }

        @Override
        public void onRenderFail(int code, String msg) {
            // 广告渲染失败
        }


        @Override
        public void onAdClick() {
            // 点击广告
        }

        @Override
        public void onAdClose() {
            // 广告关闭
        }

        @Override
        public void onAdLoadSuc() {
            // 广告请求成功
        }

        @Override
        public void onAdLoadFail(int code, String msg) {
            // 广告请求失败
        }

        @Override
        public void onRefresh() {
            //  自动刷新回调
        }
    });
```

#### Native
native显示样式由用户自定义，但需要调用NativeAdLayout使用做处理，其中媒体用MediaView加载。NativeAdLayout为FrameLayout。
* 向布局中添加NativeAdLayout和MediaView

```
# main_activity.xml
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <FrameLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintTop_toTopOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```
```
# native_layout.xml
<?xml version="1.0" encoding="utf-8"?>
<com.flatads.sdk.ui.view.NativeAdLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/flat_ad_container"
    android:layout_width="match_parent"
    android:layout_height="300dp"
    android:background="@color/white"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintTop_toTopOf="parent">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <com.flatads.sdk.ui.view.MediaView
            android:id="@+id/flat_ad_media_big"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            app:layout_constraintDimensionRatio="w,9:16"
            app:layout_constraintTop_toTopOf="parent" />

        <ImageView
            android:id="@+id/flat_ad_iv_icon"
            android:layout_width="42dp"
            android:layout_height="42dp"
            android:layout_marginStart="8dp"
            android:layout_marginLeft="8dp"
            android:layout_marginTop="12dp"
            android:layout_marginBottom="12dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/flat_ad_media_big" />

        <TextView
            android:id="@+id/flat_ad_button"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="21dp"
            android:layout_marginEnd="12dp"
            android:layout_marginRight="12dp"
            android:layout_marginBottom="21dp"
            android:background="@drawable/shape_bg"
            android:paddingStart="10dp"
            android:paddingEnd="10dp"
            android:paddingTop="5dp"
            android:paddingBottom="5dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@id/flat_ad_media_big"
            tools:text="sdasdasdsa"/>

        <TextView
            android:id="@+id/flat_ad_tv_title"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:layout_marginLeft="8dp"
            android:layout_marginEnd="16dp"
            android:layout_marginRight="16dp"
            android:textColor="@color/text_color"
            android:textSize="14sp"
            android:textStyle="bold"
            app:layout_constraintEnd_toStartOf="@id/flat_ad_button"
            app:layout_constraintStart_toEndOf="@id/flat_ad_iv_icon"
            app:layout_constraintTop_toTopOf="@id/flat_ad_iv_icon" />

        <TextView
            android:id="@+id/flat_ad_tv_desc"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:layout_marginLeft="8dp"
            android:layout_marginTop="5dp"
            android:layout_marginEnd="16dp"
            android:layout_marginRight="16dp"
            android:textColor="@color/text_des_color"
            android:textSize="12sp"
            app:layout_constraintEnd_toStartOf="@id/flat_ad_button"
            app:layout_constraintStart_toEndOf="@id/flat_ad_iv_icon"
            app:layout_constraintTop_toBottomOf="@id/flat_ad_tv_title" />


    </androidx.constraintlayout.widget.ConstraintLayout>

</com.flatads.sdk.ui.view.NativeAdLayout>

```
MediaView需要设置宽高比例。


```
public class MainActivity extends AppCompatActivity {

    private NativeAd nativeAd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String appId = "xxxxxxxx"; //申请时的appid
        String token = "xxxxxxxxxxxxxxxx"; //申请时的token

        FlatAdSDK.initialize(getApplication(), appId, token, new InitListener(){
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int code, String msg) {
            }
        }, new SdkConfig());
        
        nativeAdView = findViewById(R.id.container);
        String adUnitId = "xxxxxxxxx";
        
        nativeAd = new NativeAd(this,adUnitId );

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onAdLoadSuc(Ad ad) {
                inflateAd(ad);
            }

            @Override
            public void onAdLoadFail(int errorCode, String msg) {
            }

            @Override
            public void onAdExposure() {
            }

            @Override
            public void onAdClick() {
            }

            @Override
            public void onAdDestroy() {
            }

            @Override
            public void onRenderFail(int code, String msg) {

            }
        });
        nativeAd.loadAd();
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
    
	@Override
    protected void onStop() {
        super.onStop();
        if (adView!=null){
            adView.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView!=null){
            adView.resume();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView!=null){
            adView.destroy();
        }
    }
}
```
布局元素获取必须在成功的时候绑定，需要在加载成功后对布局进行操作，最后调用nativeAd.registerViewForInteraction将adView, mediaView, icon, clickableViews传给nativeAd处理

> 注意： 需要在activity destroy时销毁adview


* 当广告在瀑布流列表中划出时, 需要解绑广告View并进行View对象的释放

```java
nativeAd?.destroyView()
adView?.destroy()
adView = null
```

* 当广告重新回到屏幕中时, 在Adapter的onBindViewHolder()中重新绑定即可

```java
@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    // 从缓存中获取广告对象 nativeAd
    // 从ViewHolder获取复用的AdView
    ...
    // 绑定广告View和广告对象
    nativeAd.registerViewForInteraction(adView, mediaView, icon, clickableViews);
    ...
}
```

* 单独跟页面使用时, 广告跟页面的生命周期绑定

```java

@Override
protected void onResume() {
    super.onResume();
    if (adView!=null){
        adView.resume();
    }
}

@Override
protected void onStop() {
    super.onStop();
    if (adView!=null){
        adView.stop();
    }
}

@Override
protected void onDestroy() {
    super.onDestroy();
    if (nativeAd != null){
        nativeAd?.destroyView()
        nativeAd?.destroy()
    }
    if (adView!=null){
        adView.destroy();
        adView = null
    }
}
```


布局元素获取必须在成功的时候绑定，处理完布局后需要调用nativeAd.registerViewForInteraction()方法展示广告，否则无反应。

由于native广告存在video类型的广告，需要在Activity生命周期中对应添加广告的生命周期回调，否则播放器可能会异常。


#### Interstitial

```
public class MainActivity extends AppCompatActivity {

    private InterstitialAd interstitialAd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String appId = "xxxxxxxx"; //申请时的appid
        String token = "xxxxxxxxxxxxxxxx"; //申请时的token

        FlatAdSDK.initialize(getApplication(), appId, token, new InitListener(){
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int code, String msg) {
            }
        }, new SdkConfig());
        
        String adUnitId = "xxxxxxxxx";
        interstitialAd = new InterstitialAd(this, adUnitId);
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onAdLoadSuc() {
                interstitialAd.show();
            }

            @Override
            public void onAdClose() {
            }

            @Override
            public void onAdLoadFail(int code, String msg) {
            }

            @Override
            public void onAdExposure() {
            }

            @Override
            public void onRenderFail(int code, String msg) {

            }

            @Override
            public void onAdClick() {
            }
        });
        interstitialAd.loadAd();
    }

}
```
需要在请求广告完成时再调用show方法展示广告。

#### 激励视频
```
public class RewardedActivity extends AppCompatActivity {

    RewardedAd rewardedAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rewarded_test);
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", "1234567");
        map.put("unique_id", "uihj89uijkbn7uy8");
        map.put("reward_type", "add_coins");
        map.put("reward_value", "2");
        map.put("verifier", "tyuidjkol");
        map.put("extinfo", "{'self define':'xxx'}");
        String adUnitId = "xxxxxxxxxxxxxxxx";
        rewardedAd = new RewardedAd(this,adUnitId);
        rewardedAd.setRequestParams(map);
        rewardedAd.setAdListener(new RewardedAdListener() {
            @Override
            public void onAdClose() {
            }

            @Override
            public void onUserEarnedReward() {
            }

            @Override
            public void onAdFailedToShow() {
            }

            @Override
            public void onAdExposure() {
            }

            @Override
            public void onAdClick() {
            }

            @Override
            public void onAdLoadSuc() {
            }

            @Override
            public void onAdLoadFail(int code, String msg) {
            }
        });
        rewardedAd.loadAd();

    }

}
```
* 广告展示

通过rewardedAd.show()方法展示激励广告

```java
rewardedAd.show();
```
> 注意：当请求成功后，isReady()为true，可根据此值判断广告是否准备好。
加载激励广告前。需要传入激励广告的相关信息（以上是测试数据）

|  字段名称   | 类型  |  取值（举例）  |  说明  |字段名称|
|  ----  | ----  | ----  | ----  | ----  |
| customer_id  | string |1234567|受激励的客户id|可选|
| unique_id  | string |uihj89uijkbn7uy8|激励的唯一id|可选|
|  reward_type   | 激励类型|add_coins|激励的类型，开发者自定义|可选|
|  reward_value  | 激励值  |2|激励的值，开发者自定义|可选|
| verifier  | 验证码 |tyuidjkol|接入方生产的验证码，用于回调链的验证|可选|
| extinfo  | 额外信息 |{"self define":"xxx"}|接入方自定义||


#### 互动广告
互动广告的大小由开发者自己决定，使用时可先触发loadAd，后续再把布局添加到界面上，可以更快的显示广告内容。
```
    ...
    <com.flatads.sdk.ui.view.InteractiveView
        android:id="@+id/interactive_view"
        android:layout_width="50dp"
        android:layout_height="50dp"
        />
    ...
```
```
public class MainActivity extends AppCompatActivity {

    private InteractiveView interactiveView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String appId = "xxxxxxxx"; //申请时的appid
        String token = "xxxxxxxxxxxxxxxx"; //申请时的token

        FlatAdSDK.initialize(getApplication(), appId, token, new InitListener(){
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int code, String msg) {
            }
        }, new SdkConfig());
        
        String adUnitId = "xxxxxxxxx";
        interactiveView = findViewById(R.id.interactive_view);
        interactiveView.setAdUnitId(adUnitId);
        interactiveView.setCacheTime(60 * 1000 * 60 * 24);
        interactiveView.setAdListener(new InteractiveAdListener() {
            @Override
            public void onRenderSuccess() {
            }

            @Override
            public void onRenderFail(int code, String msg) {
            }

            @Override
            public void onAdClick() {
            }

            @Override
            public void onAdClose() {
            }

            @Override
            public void onAdLoadSuc() {
            }

            @Override
            public void onAdLoadFail(int code, String msg) {
            }
        });
        interactiveView.loadAd();
    }

}
```

setCacheTime为设置缓存时间，默认为24小时。

如果开发者想指定互动广告icon图片，提供了方法设置：
```
interactiveView.setIconView(Drawable)
interactiveView.setIconView(Bitmap)
interactiveView.setIconView(url)
```

注意：Activity销毁时需要调用onDestroy方法
```
interactiveView.onDestroy()
```

> 注意：
1.互动广告需要尽早的调用，如可在进入app时进行互动广告加载，在需要展示互动广告时，把view添加到布局上面去。
2.广告回调onRenderSuccess时，则webview已经加载完成。

### 闪屏广告
在APP的启动页面Activity的onCreate方法中调用以下代码：

```

String unitid = "xxxxxxxxx";  // 申请到的unitid
// 创建 OpenScreenAd 实例，传入当前页面的context，广告unitid
OpenScreenAd openScreenAd = new OpenScreenAd(SplashActivity.this, unitid);

// 监听
openScreenAd.setAdListener(new OpenScreenAdListener() {
@Override
public void onAdExposure() {

        }

@Override
public void onRenderFail(int code, String msg) {

        }

@Override
public void onAdClick() {

        }

@Override
public void onAdClose() {
        startActivity(new Intent(SplashActivity.this,DemoActivity.class));
        finish();
        }

@Override
public void onAdLoadSuc() {
        showAdIfReady();
        }

@Override
public void onAdLoadFail(int code, String msg) {

        }
});

```


* 请求广告

通过openScreenAd.loadAd()方法使用请求广告, 由于需要网络请求和本地资源读取，属于耗时操作，可以在开屏启动页加载，建议可以在开屏广告关闭后调用，进行预加载下一次的开屏广告。  


```java
openScreenAd.loadAd();

```

* 展示开屏广告

```java
//  展示闪屏广告
private void showAdIfReady(){
   if ( openScreenAd == null || !FlatAdSDK.INSTANCE.isInit() ) return;

   if (openScreenAd.isReady() ){
       openScreenAd.show();
   }
}
```

* openScreenAd.isReady() 是判断当前广告是否准备好可以展示，当isReady返回ture时，说明已经加载好开屏广告，可以用于展示。

* openScreenAd.show() 是打开开屏广告，show的过程不需要网络。


> 注意：
> 需要在设置监听的之后调用show()方法展示广告。

### 更多说明

####  注意事项

1. 接入时需对app开启存储权限后才可以正常下载广告配置的apk，否则部分手机将无下载反应。
2. 混淆时，需添加 -keep class com.flatads.sdk.** {*;} ，否则将无数据返回。

```
<?xml version="1.0" encoding="utf-8"?>
<network-security-config xmlns:tools="http://schemas.android.com/tools"
    tools:ignore="MissingDefaultResource">
    <base-config cleartextTrafficPermitted="true">
        <trust-anchors>
            <certificates src="system" overridePins="true" />
            <certificates src="user" overridePins="true" />
        </trust-anchors>
    </base-config>
</network-security-config>
```
接着，在AndroidManifest.xml文件下的application标签增加以下属性
```

<application
...
 android:networkSecurityConfig="@xml/network_security_config"
...
    />
```

# 广告响应参数说明
### 异常状态码
|状态码（status）| 说明 |描述（msg)|
|:-----|:-----|:-----|
|40003|签名验证失败 |not validate|
|40101|广告位不存在|Not bidding:tagid not actived|
|40102|adx 流控|request next time please|
|40103|没有匹配的广告返回|no ads from all dsps|
|40201 | 参数错误|empty appid or sign|

### 客户端错误码
|状态码（status）| 说明              | 描述（msg)                                      |
|:-----|:----------------|:---------------------------------------------|
|1001| 未知错误            | Unknown error                                |
|2001| SKD初始化失败        | The SDK initialization error                 |
|2002| 模拟器环境           | is Multi Box                                 |
|4001| 广告 unitId 为空    | Ad unitId is empty                           |
|4002| 返回空广告信息数据       | No Ads                                       |
|4003 | 加载物料失败          | Load Ad res Failed                           |
|4004 | 广告未准备好          | Ads not ready                                |
|4005 | 解析response错误    | Ad parse error                               |
|4006| 加载广告异常          | Ad load too frequently or AppContext is null |
|4007| webview未安装      | WebView not install                          |
|4008| 上报webview js的日志 | WebView Error            |
|4009| 没有可用的广告数据       | There is no available data             |



