package com.framgia.englishconversation.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.framgia.englishconversation.AppApplication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * Created by toand on 5/13/2017.
 */

public class Utils {

    private static final int POSITION_LEFT_BITMAP = 0;
    private static final int POSITION_TOP_BITMAP = 0;
    private static final int ALPHA = 0;
    private static final int RED = 0;
    private static final int GREEN = 0;
    private static final int BLUE = 0;

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
                ActivityCompat.requestPermissions(activity, new String[]{s}, 0);
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
        return getFormatTimeFile(duration);
    }

    public static String getFormatTimeFile(long duration) {
        if (duration == -1) {
            return "00:00";
        }
        int minute = (int) Math.floor(duration / 1000 / 60);
        int second = (int) (duration / 1000 - minute * 60);
        return String.format(Locale.getDefault(), "%02d:%02d", minute, second);
    }

    public static String updateDuration(long duration) {
        long s = duration % Constant.SECOND_PER_MINUTE;
        long m = (duration / Constant.SECOND_PER_MINUTE) % Constant.SECOND_PER_MINUTE;
        return String.format(Locale.getDefault(), "%02d:%02d", m, s);
    }
    
    public static long generateOppositeNumber(long number){
        return -number;
    }

    public static void hideKeyBoard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null && view instanceof EditText) {
            InputMethodManager imm =
                    (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm == null) {
                return;
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), Constant.FLAG_HIDE_KEYBOARD);
        }
    }

    public static Bitmap createCircleBitmap(Bitmap input) {
        int width = input.getWidth();
        int height = input.getHeight();
        int size = (width > height) ? height : width;
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        Rect rect = new Rect(POSITION_LEFT_BITMAP, POSITION_TOP_BITMAP, size, size);
        RectF rectF = new RectF(rect);
        canvas.drawARGB(ALPHA, RED, GREEN, BLUE);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(input, rect, rect, paint);
        input.recycle();
        return output;
    }

}
