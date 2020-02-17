package com.flutter.chat_head.flutter_chat_head;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.BinaryMessenger;
import android.app.Activity;

/** FlutterChatHeadPlugin */
public class FlutterChatHeadPlugin implements FlutterPlugin, ActivityAware {
  private MethodChannel methodChannel;
  Activity activity;
  FlutterChatHeadHandler flutterChatHeadHandler;

  public static void registerWith(Registrar registrar) {
    FlutterChatHeadPlugin plugin = new FlutterChatHeadPlugin();
    plugin.activity = registrar.activity();
    plugin.setupChannels(registrar.messenger(), registrar.context());
  }

  @Override
  public void onAttachedToEngine(FlutterPluginBinding binding) {
    Log.d("ICON", "onAttachedToEngine");
    setupChannels(binding.getFlutterEngine().getDartExecutor(), binding.getApplicationContext());
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding activityPluginBinding) {
    flutterChatHeadHandler.setActivity(activityPluginBinding.getActivity());
  }
  private void setupChannels(BinaryMessenger messenger, Context context) {
    methodChannel = new MethodChannel(messenger, "flutter_chat_head");
    flutterChatHeadHandler = new FlutterChatHeadHandler(context, activity);

    methodChannel.setMethodCallHandler(flutterChatHeadHandler);
  }
  @Override
  public void onDetachedFromActivityForConfigChanges() {
    // TODO: the Activity your plugin was attached to was
    // destroyed to change configuration.
    // This call will be followed by onReattachedToActivityForConfigChanges().
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding activityPluginBinding) {
    // TODO: your plugin is now attached to a new Activity
    // after a configuration change.
  }

  @Override
  public void onDetachedFromActivity() {
    // TODO: your plugin is no longer associated with an Activity.
    // Clean up references.
  }

}