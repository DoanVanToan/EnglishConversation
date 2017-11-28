package com.framgia.englishconversation.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import com.framgia.englishconversation.AppApplication;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by toand on 5/13/2017.
 */

public class Utils {
    public static void getKeyHash(Context context) {
        PackageInfo info;
        try {
            info = context.getPackageManager()
                .getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    public static boolean isAllowPermision(Activity activity, String[] permission) {
        boolean isAllowPermison = true;
        for (String s : permission) {
            if (ContextCompat.checkSelfPermission(activity, s)
                != PackageManager.PERMISSION_GRANTED) {
                isAllowPermison = false;
                ActivityCompat.requestPermissions(activity, new String[] { s }, 0);
            }
        }
        return isAllowPermison;
    }

    public static int getFileDuration(String filePath) {
        try {
            MediaPlayer mp = MediaPlayer.create(AppApplication.getInstance(), Uri.parse(filePath));
            return mp.getDuration();
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getFormatTimeFile(String filePath) {
        int duration = getFileDuration(filePath);
        if (duration == -1) {
            return "00:00";
        }
        return String.format("%d:%02d:%02d", duration / 3600, (duration % 3600) / 60,
            (duration % 60));
    }
}