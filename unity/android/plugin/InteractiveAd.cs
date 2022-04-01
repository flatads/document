using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class InteractiveAd : MonoBehaviour
{
    private AndroidJavaObject interactiveBridge;
    public void createInteractiveAd(AndroidJavaObject actObj,string unitId,int width,int height,int leftMargin, 
    int rightMargin, int topMargin,int bottomMargin, int gravity1, int gravity2)
    {
        interactiveBridge = new AndroidJavaObject("com.flat.androidplugin.bridge.InteractiveAdBridge");
        interactiveBridge.Call("createInteractiveAd",actObj,unitId,width,height,
        leftMargin,rightMargin,topMargin,bottomMargin,gravity1,gravity2);
    }

    public void loadInteractiveAd(AndroidJavaObject actObj){
        InteractiveListener listener = new InteractiveListener();
        interactiveBridge.Call("loadcreateInteractiveAd",actObj,listener);
    }

    public void destroy(){
        interactiveBridge.Call("destroy");
    }

}
