
import 'package:file_downloader/api_generated.dart';
import 'package:flutter/cupertino.dart';

class FlutterDownloadProgressImpl extends DownloaderAndroidProgress{

  FlutterDownloadProgressImpl();


  @override
  void progress(String url, int sofarBytes, int totalBytes) {
    debugPrint("flutter progress:$sofarBytes/$totalBytes");
  }


}