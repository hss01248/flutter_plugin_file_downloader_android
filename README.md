# file_downloader

file download impl by android

## Getting Started

This project is a starting point for a Flutter
[plug-in package](https://flutter.dev/developing-packages/),
a specialized package that includes platform-specific implementation code for
Android

For help getting started with Flutter development, view the
[online documentation](https://flutter.dev/docs), which offers tutorials,
samples, guidance on mobile development, and a full API reference.

# 使用

```yaml
  base_api_response_flutter: ^0.0.1
  file_downloader_android: ^1.0.0
```



```dart
DownloaderAndroid().download(url, path).then((value) {
              debugPrint(value.toString());
              BridgeResponse response = BridgeResponse.fromMap(value);
              if(response.success){

              }
            });
```

进度回调: 

写一个类继承DownloaderAndroidProgress,在里面拿到进度







```dart
DownloaderAndroidProgress.setup(FlutterDownloadProgressImpl());


class FlutterDownloadProgressImpl extends DownloaderAndroidProgress{

  FlutterDownloadProgressImpl();


  @override
  void progress(String url, int sofarBytes, int totalBytes) {
    debugPrint("flutter progress:$sofarBytes/$totalBytes");
  }


}
```



## 取消下载

```dart
 String url = "http://kodo.hss01248.tech/apk/AppManager_v3.1.3.apk?v=2";
DownloaderAndroid().cancel(url);
```

