using System;
using UnityEngine;

public class InteractiveListener : AndroidJavaProxy
{
    public InteractiveListener():base("com.flatads.sdk.callback.InteractiveAdListener"){

    }

    public void onAdClick(){
        Debug.Log("onAdClick");
    }

    public void onAdClose(){
        Debug.Log("onAdClose");
    }

    public void onAdLoadSuc(){
        Debug.Log("onAdLoadSuc");
    }

    public void onAdLoadFail(int code, String msg){
        Debug.Log("onAdLoadFail : code = " + code + " ,msg = " + msg);
    }
    
    public void onRenderSuccess(){
        Debug.Log("onRenderSuccess");
    }

    public void onRenderFail(int code, String msg){
        Debug.Log("onRenderFail : code = " + code + " ,msg = " + msg);
    }
}
