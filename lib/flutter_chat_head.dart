import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_chat_head/exceptions/no_permission.dart';

class FlutterChatHead {
  static const MethodChannel _channel = MethodChannel('flutter_chat_head');

  static Future createChatHead() async {
    if (await checkPermission() == false) throw new NoPermissionExeption();
    final result = await _channel.invokeMethod('createChatHead');
    return result;
  }

  static Future<bool> checkPermission() async {
    final result = await _channel.invokeMethod('checkPermission');
    return result;
  }

  static Future<void> requestPermission() async {
    await _channel.invokeMethod('requestPermission');
  }
}
