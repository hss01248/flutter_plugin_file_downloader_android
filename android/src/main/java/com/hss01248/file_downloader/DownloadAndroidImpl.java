package com.hss01248.file_downloader;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.hss01248.baseapiresponse.BaseApiResponse;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Despciption todo
 * @Author hss
 * @Date 04/12/2023 10:43
 * @Version 1.0
 */
public class DownloadAndroidImpl implements DownloaderAndroidPigeonApi.DownloaderAndroid{

    static DownloaderAndroidPigeonApi.DownloaderAndroidProgress progress ;
    Map<String,BaseDownloadTask> taskMap = new HashMap<>();
    @Override
    public void download(@NonNull String url, @NonNull String savePath, @NonNull DownloaderAndroidPigeonApi.Result<Map<String, Object>> result) {

        try{
            if(savePath == null || TextUtils.isEmpty(savePath)){
                String name = url;
                if(name.contains("?")){
                    name = name.substring(0,name.indexOf("?"));
                }
                name = name.substring(name.lastIndexOf("/")+1);
                File file  = new File(Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),name);
                savePath = file.getAbsolutePath();
            }
            String finalSavePath = savePath;
            BaseDownloadTask baseDownloadTask = FileDownloader.getImpl()
                    .create(url);
            taskMap.put(url,baseDownloadTask);
            //.setTag(url)
            baseDownloadTask.setPath(savePath,false)
                    .setSyncCallback(false)
                    .setForceReDownload(false)
                    .setAutoRetryTimes(1)
                    .setWifiRequired(false)
                    .setListener(new FileDownloadSampleListener(){
                        @Override
                        protected void error(BaseDownloadTask task, Throwable e0) {
                            super.error(task, e0);
                            taskMap.remove(url);
                            LogUtils.w(url,finalSavePath,e0);
                            Throwable e = e0;
                            if(e == null){
                                e = task.getErrorCause();
                            }
                            if(e == null){
                                e = new IOException("download failed");
                            }
                            if(e.getCause() != null){
                                e = e.getCause();
                            }
                            result.success(BaseApiResponse.exception(e).asMap());
                        }

                        @Override
                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            super.paused(task, soFarBytes, totalBytes);
                            LogUtils.v("download","取消了下载. 这里不处理,由取消的地方去处理: \n"+task.getUrl());
                        }

                        @Override
                        protected void completed(BaseDownloadTask task) {
                            super.completed(task);
                            taskMap.remove(url);
                            result.success(BaseApiResponse.success(finalSavePath).asMap());
                        }

                        @Override
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            super.progress(task, soFarBytes, totalBytes);
                            if(AppUtils.isAppDebug()){
                                Log.v("download","progress on android: "+soFarBytes+"/"+totalBytes);
                            }

                            if(progress == null){
                                if(FileDownloaderPlugin.binaryMessenger  == null){
                                    Log.d("download","FileDownloaderPlugin.binaryMessenger  == null: "+soFarBytes+"/"+totalBytes);
                                }else {
                                    progress = new DownloaderAndroidPigeonApi.DownloaderAndroidProgress(FileDownloaderPlugin.binaryMessenger);
                                }
                            }
                            if(progress == null){
                                return;
                            }
                            progress.progress(url, (long) soFarBytes, (long) totalBytes,
                                    new DownloaderAndroidPigeonApi.DownloaderAndroidProgress.Reply<Void>() {
                                        @Override
                                        public void reply(Void reply) {

                                        }
                                    });
                        }
                    }).start();
        }catch (Throwable throwable){
            LogUtils.w(url,savePath,throwable);
            result.success(BaseApiResponse.exception(throwable).asMap());
        }

    }

    @Override
    public void cancel(@NonNull String url, @NonNull DownloaderAndroidPigeonApi.Result<Map<String, Object>> result) {
        BaseDownloadTask baseDownloadTask = taskMap.remove(url);
        if(baseDownloadTask !=null){
            boolean pause = baseDownloadTask.pause();
            if(pause){
                result.success(BaseApiResponse.success().build().setMsg("cancel success").asMap());
            }else {
                result.success(BaseApiResponse.error("fail","cancel failed").asMap());
            }
        }else {
            result.success(BaseApiResponse.error("fail","cancel failed, task not found").asMap());
        }
    }
}
