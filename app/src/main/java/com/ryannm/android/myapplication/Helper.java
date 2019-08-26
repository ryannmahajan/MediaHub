package com.ryannm.android.myapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.support.v4.app.FragmentActivity;

import static android.content.Context.CAMERA_SERVICE;

class Helper {

    /** Check if this device has a camera_fragment */
    static boolean phoneHasCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /** A safe way to get an instance of the Camera object. */
    static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera_fragment is unavailable
    }

    static boolean hasCamera2API(Context context) {
        CameraManager manager =
                (CameraManager)context.getSystemService(CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics chars
                        = manager.getCameraCharacteristics(cameraId);
                // Do something with the characteristics
            }
        }catch (CameraAccessException e) {
                e.printStackTrace();
        } catch (NullPointerException e) {
            return false;
        }

        return true;
    }

    static String getVideoPath(Context context) {
        return context.getExternalFilesDir(null).getAbsolutePath() + "/video.mp4";
    }

    static String getImagePath(Context context) {
        return context.getExternalFilesDir(null).getAbsolutePath()+ "/pic.jpg";
    }

    static String getAudioPath(Context context) {
        return context.getExternalFilesDir(null).getAbsolutePath() + "/audio.amr";
    }
}
