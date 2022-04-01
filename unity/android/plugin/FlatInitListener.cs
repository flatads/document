using System;
using UnityEngine;

public class FlatInitListener : AndroidJavaProxy
{
    public FlatInitListener():base("com.flatads.sdk.callback.InitListener"){
        

    }

    public void onSuccess(){ 
        Debug.Log("init success");
    }

    public void onFailure(int code, String msg){
        Debug.Log("init failure : code = "+ code +",msg = "+msg);
    }


}
