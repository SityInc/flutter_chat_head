package com.flutter.chat_head.flutter_chat_head;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.provider.Settings;
import android.net.Uri;
import android.content.Intent;
import java.io.FileInputStream;
import java.io.IOException;
import android.app.Activity;
import io.flutter.view.FlutterMain;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;

import static android.content.Context.WINDOW_SERVICE;

/**
 * The handler receives {@link MethodCall}s from the UIThread, gets the related
 * information from a @{@link Connectivity}, and then send the result back to
 * the UIThread through the {@link MethodChannel.Result}.
 */
class FlutterChatHeadHandler implements MethodCallHandler {
    private WindowManager mWindowManager;
    private View mFloatingView;
    private Context context;
    public static String PACKAGE_NAME;
    private Activity activity;

    /**
     * registrar Construct the FlutterChatHeadHandler with a {@code context}. The
     * {@code
     * context} must not be null.
     */
    FlutterChatHeadHandler(Context context, Activity activity) {
        assert (context != null);
        this.context = context;
        this.activity = activity;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
        switch (call.method) {
        case "createChatHead":
            createChatHead(context);
            result.success("OK");
            break;
        case "checkPermission":
            result.success(Settings.canDrawOverlays(context));
            break;
        case "requestPermission":
            checkDrawOverlayPermission(context);
            result.success(Settings.canDrawOverlays(context));
            break;
        default:
            result.notImplemented();
            break;
        }
    }

    public void createChatHead(Context context) {
        // Inflate the floating view layout we created

        mFloatingView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_floating_widget, null);

        // Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O
                        ? WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                        : WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        // Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT; // Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        // Add the view to the window
        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        ImageView closeButton = (ImageView) mFloatingView.findViewById(R.id.close_btn);
        try {
            AssetManager assetManager = context.getAssets();
            String key = FlutterMain.getLookupKeyForAsset("assets/icons/icon_example.png");
            AssetFileDescriptor fd = assetManager.openFd(key);
            FileInputStream fis = fd.createInputStream();
            ImageView circle = mFloatingView.findViewById(R.id.collapsed_iv);
            circle.setImageBitmap(decodeBitmap(fis));
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWindowManager.removeView(mFloatingView);
            }
        });
        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    // remember the initial position.
                    initialX = params.x;
                    initialY = params.y;

                    // get the touch location
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return true;
                case MotionEvent.ACTION_UP:
                    int Xdiff = (int) (event.getRawX() - initialTouchX);
                    int Ydiff = (int) (event.getRawY() - initialTouchY);

                    // The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little
                    // while clicking.
                    // So that is click event.
                    return true;
                case MotionEvent.ACTION_MOVE:
                    // Calculate the X and Y coordinates of the view.
                    params.x = initialX + (int) (event.getRawX() - initialTouchX);
                    params.y = initialY + (int) (event.getRawY() - initialTouchY);

                    // Update the layout with new X & Y coordinate
                    mWindowManager.updateViewLayout(mFloatingView, params);
                    return true;
                }
                return false;
            }
        });

        // ….
        // ….
    }

    private static Bitmap decodeBitmap(FileInputStream fis) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap actuallyUsableBitmap = BitmapFactory.decodeStream(fis);
        Log.d("TAG", "TEXTE");
        return actuallyUsableBitmap;

    }

    public boolean checkDrawOverlayPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(context)) {
                /** if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context.getPackageName()));
                /** request permission via start activity for result */
                Log.d("TAG", activity.toString());

                activity.startActivityForResult(intent, 10101);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

}