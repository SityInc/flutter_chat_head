import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_chat_head/flutter_chat_head.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              RaisedButton(
                onPressed: () {
                  FlutterChatHead.checkPermission().then((bool permission) {
                    print('Permissão: ');
                    print(permission);
                  });
                },
                child: Text("Check Permission"),
              ),
              RaisedButton(
                onPressed: () {
                  FlutterChatHead.createChatHead();
                },
                child: Text("Create ChatHead"),
              ),
              RaisedButton(
                onPressed: () {
                  FlutterChatHead.requestPermission();
                },
                child: Text("Request Permission"),
              ),
              Image.asset(
                "assets/icons/icon_example.png",
                width: 100,
              )
            ],
          ),
        ),
      ),
    );
  }
}
