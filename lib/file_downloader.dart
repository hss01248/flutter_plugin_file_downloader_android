
import 'file_downloader_platform_interface.dart';

class FileDownloader {
  Future<String?> getPlatformVersion() {
    return FileDownloaderPlatform.instance.getPlatformVersion();
  }
}
