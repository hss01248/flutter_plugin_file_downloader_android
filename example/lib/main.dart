import 'package:base_api_response_flutter/base_api_response_flutter.dart';
import 'package:file_downloader/api_generated.dart';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:file_downloader/file_downloader.dart';

import 'flutter_download_progress.dart';

void main() {
  runApp(const MyApp());
  DownloaderAndroidProgress.setup(FlutterDownloadProgressImpl());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _fileDownloaderPlugin = FileDownloader();

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await _fileDownloaderPlugin.getPlatformVersion() ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
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
            children: [
              ElevatedButton(onPressed: (){
                String url = "http://kodo.hss01248.tech/apk/AppManager_v3.1.3.apk?v=2";
                String path = "";
                DownloaderAndroid().download(url, path).then((value) {
                  debugPrint(value.toString());
                  BaseApiResponse response = BaseApiResponse.fromMap(value);
                  if(response.success){

                  }
                });
                //FlutterDownloadProgressImpl().progress(url, sofarBytes, totalBytes);

              }, child: Text("下载apk")),
              ElevatedButton(onPressed: (){
                String url = "http://kodo.hss01248.tech/apk/AppManager_v3.1.3.apk?v=2";
                DownloaderAndroid().cancel(url).then((value) {
                  debugPrint(value.toString());
                  BaseApiResponse response = BaseApiResponse.fromMap(value);
                  if(response.success){

                  }
                });
                //FlutterDownloadProgressImpl().progress(url, sofarBytes, totalBytes);

              }, child: Text("取消下载"))
            ],
          ),
        ),
      ),
    );
  }
}
