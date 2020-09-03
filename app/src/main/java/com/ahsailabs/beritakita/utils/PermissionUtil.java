package com.ahsailabs.beritakita.utils;

/**
 * Created by ahsai on 12/7/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class PermissionUtil {
    private int requestCode;
    private Runnable taskWillDo;
    private Runnable taskIfDenied;
    private Object activityOrFragment;

    public PermissionUtil(Object activityOrFragment, int requestCode, Runnable taskWillDo, Runnable taskIfDenied){
        this.activityOrFragment = activityOrFragment;
        this.requestCode = requestCode;
        this.taskWillDo = taskWillDo;
        this.taskIfDenied = taskIfDenied;
    }

    public static PermissionUtil checkPermissionAndGo(Object activityOrFragment, int requestCode, boolean showDialogInit, String initTitle, String initBody, Runnable taskWillDo, Runnable taskIfDenied, String... permissions){
        PermissionUtil permissionUtil = null;
        if(permissions != null && permissions.length > 0) {
            permissionUtil = new PermissionUtil(activityOrFragment, requestCode, taskWillDo, taskIfDenied);
            if (permissionUtil.arePermissionsGranted(showDialogInit, initTitle, initBody, permissions)) {
                taskWillDo.run();
            }
        } else {
            taskWillDo.run();
        }
        return permissionUtil;
    }

    public static PermissionUtil checkPermissionAndGo(Object activityOrFragment, int requestCode, Runnable taskWillDo, Runnable taskIfDenied, String... permissions){
        return  checkPermissionAndGo(activityOrFragment, requestCode, true, null, null, taskWillDo, taskIfDenied, permissions);
    }


    public static PermissionUtil checkPermissionAndGo(Object activityOrFragment, int requestCode, Runnable taskWillDo, String... permissions){
        return checkPermissionAndGo(activityOrFragment, requestCode, taskWillDo, null, permissions);
    }


    private Context getContext(){
        Context context = null;
        if(activityOrFragment != null){
            if(activityOrFragment instanceof Activity){
                context = (Activity) activityOrFragment;
            } else if (activityOrFragment instanceof Fragment){
                context = ((Fragment)activityOrFragment).getActivity();
            }
        }
        return context;
    }

    private boolean arePermissionsGranted(boolean showDialogInit, String initTitle, String initBody, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activityOrFragment != null && permissions != null) {
            final ArrayList<String> needRequested = new ArrayList<>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                    needRequested.add(permission);
                }
            }

            if(needRequested.size() > 0){
                if(showDialogInit) {
                    DialogUtil.showDialog2Option(getContext(), initTitle==null?"Permission":initTitle, initBody==null?"we need all these permissions to run properly":initBody,
                            "OK", new Runnable() {
                                @Override
                                public void run() {
                                    //need request permission
                                    String[] needRequestedPermission = needRequested.toArray(new String[needRequested.size()]);
                                    requestPermisssion(activityOrFragment, needRequestedPermission, requestCode);
                                }
                            }, "Cancel", new Runnable() {
                                @Override
                                public void run() {
                                    if (taskIfDenied != null) taskIfDenied.run();
                                }
                            });
                } else {
                    //need request permission
                    String[] needRequestedPermission = needRequested.toArray(new String[needRequested.size()]);
                    requestPermisssion(activityOrFragment, needRequestedPermission, requestCode);
                }
                return false;
            }
        }
        return true;
    }

    private void requestPermisssion(final @NonNull Object activityOrFragment,
                                    final @NonNull String[] needRequestedPermission, final @IntRange(from = 0) int requestCode){
        if(activityOrFragment instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) activityOrFragment, needRequestedPermission, requestCode);
        } else if(activityOrFragment instanceof Fragment){
            ((Fragment)activityOrFragment).requestPermissions(needRequestedPermission, requestCode);
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == this.requestCode){
            // If request is cancelled, the result arrays are empty.
            ArrayList<String> deniedPermissions = new ArrayList<>();
            ArrayList<String> grantedPermissions = new ArrayList<>();
            if (grantResults.length > 0) {
                //partial cancelled
                int i = 0;
                for (int result : grantResults){
                    if(result == PackageManager.PERMISSION_DENIED){
                        deniedPermissions.add(permissions[i]);
                    }else if((result == PackageManager.PERMISSION_GRANTED)){
                        grantedPermissions.add(permissions[i]);
                    }
                    i++;
                }
            } else {
                //all cancelled
            }

            if(grantedPermissions.size() == permissions.length){
                //do task
                this.taskWillDo.run();
            }else{
                //there is some denied
                boolean isAnyNeverAskAgainChecked = false;
                if(activityOrFragment instanceof Activity) {
                    for (String deniedPermission : deniedPermissions) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) activityOrFragment, deniedPermission)) {
                            isAnyNeverAskAgainChecked = true;
                        }
                    }
                } else if(activityOrFragment instanceof Fragment){
                    for (String deniedPermission : deniedPermissions) {
                        if (!((Fragment) activityOrFragment).shouldShowRequestPermissionRationale(deniedPermission)) {
                            isAnyNeverAskAgainChecked = true;
                        }
                    }
                }
                if(isAnyNeverAskAgainChecked){
                    DialogUtil.showDialog2Option(getContext(), "Permission", "Sorry, because you already check never ask again and we need all these permissions to run properly, please enable permission in this application setting",
                            "Cancel", new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, "Setting", new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                    intent.setData(uri);
                                    getContext().startActivity(intent);
                                }
                            });

                }
                if(taskIfDenied != null)taskIfDenied.run();
            }

        }
    }
}

