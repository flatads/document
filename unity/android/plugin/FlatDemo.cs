using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FlatDemo : MonoBehaviour
{

    private InteractiveAd interactiveAd;

    private AndroidJavaClass act;
    private AndroidJavaObject actObj;

    // Start is called before the first frame update
    void Start()
    {
        act = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        actObj = act.GetStatic<AndroidJavaObject>("currentActivity");
        FlatInit flatInit = new FlatInit();
        flatInit.initSDK("xxxxxx","xxxxxxxx");

    }


    public void createInteractiveAd(){
        if(interactiveAd == null){
            interactiveAd = new InteractiveAd();
            interactiveAd.createInteractiveAd(actObj,"xxxxxxx",200,200,0,0,0,0,600,400);
        }
    }

    public void loadInteractiveAd(){
        if(interactiveAd!=null){
            interactiveAd.loadInteractiveAd(actObj);
        } 
    }

}
