import 'package:pigeon/pigeon.dart';

// Flutter 调用原生代码

@HostApi()
abstract class DownloaderAndroid {

  @async
  Map<String, Object> download(String url,String savePath);

  @async
  Map<String, Object> cancel(String url);


}

@FlutterApi()
abstract class DownloaderAndroidProgress {

  void progress(String url,int sofarBytes,int totalBytes );

}




