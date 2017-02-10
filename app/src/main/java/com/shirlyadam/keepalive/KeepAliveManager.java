package com.shirlyadam.keepalive;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2016/10/21.
 */
public class KeepAliveManager {

    private static final String TAG = "KeepAliveManager";
    private Context mContext;
    private List<Activity> activityStack = new ArrayList<>();//activity的栈管理
    private static KeepAliveManager mKeepAliveManager;

    public void init(Context context){
        if(this.mContext == null){
            this.mContext = context;
        }
    }

    public static synchronized KeepAliveManager getInstance(){
        if(mKeepAliveManager == null){
            mKeepAliveManager = new KeepAliveManager();
        }
        return mKeepAliveManager;
    }

    public void addActivityToStack(Activity mActivity){
        if(mActivity != null){
            Log.d(TAG,"addActivityToStack");
            activityStack.add(mActivity);
            Log.d(TAG,"activityStack的size为="+ activityStack.size());
        }
    }

    public void stopKeepAliveActivity(){
        if(activityStack != null && activityStack.size() > 0){
            for(Activity m:activityStack){
                m.finish();
            }
            activityStack.clear();
            Log.d(TAG,"stopKeepAliveAcitivity ---> SUCCESS");
            Log.d(TAG,"activityStack的size为="+ activityStack.size());
        }else{
            Log.d(TAG,"stopKeepAliveActivity ---> Fail cause activitystack is null or size = 0");
            Log.d(TAG,"activityStack的size为="+ activityStack.size());
        }
    }

    public void startKeepAliveActivity(){
        if(activityStack != null){
            Intent mIntent = new Intent(mContext,KeepAliveActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(mIntent);
            Log.d(TAG,"startKeepAliveActivity ---> SUCCESS");
            Log.d(TAG,"activityStack的size为="+ activityStack.size());
        }else{
            Log.d(TAG,"startKeepAliveActivity ---> Fail cause activitystack is null");
            Log.d(TAG,"activityStack的size为="+ activityStack.size());
        }
    }

    public void destroyKeepAliveManager(){
        if(mKeepAliveManager != null){
            activityStack = null;
            mKeepAliveManager = null;
            Log.d(TAG,"destroyKeepAliveManager ---> SUCCESS");
        }
    }

    /**
     * 提升service的优先级为前台service方法，适配性有待考究
     */
    public void setForground(final Service dgzqWatchService,final Service innerService){
        final int forgroundIdentifyId = 1;
        Log.d(TAG,"setForground,dgzqWatchservice:" + dgzqWatchService + ",innerservice :"+innerService);
        if(dgzqWatchService != null){
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
                dgzqWatchService.startForeground(forgroundIdentifyId,new Notification());
            }else{
                dgzqWatchService.startForeground(forgroundIdentifyId,new Notification());
                if(innerService != null){
                    innerService.startForeground(forgroundIdentifyId,new Notification());
                    innerService.stopSelf();
                }
            }
        }
    }

}
