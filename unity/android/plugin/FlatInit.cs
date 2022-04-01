using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FlatInit : MonoBehaviour
{
    private AndroidJavaObject plugin;
    private AndroidJavaClass act;
    private AndroidJavaObject actObj;

    public void initSDK(string appId,string appToken){
        act = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        actObj = act.GetStatic<AndroidJavaObject>("currentActivity");
        plugin = new AndroidJavaObject("com.flat.androidplugin.FlatAdsPlugin");
        FlatInitListener initListener = new FlatInitListener();
        plugin.Call("initSDK",actObj, appId, appToken ,initListener);
    }
}
