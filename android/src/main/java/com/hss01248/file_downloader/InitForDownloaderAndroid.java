package com.hss01248.file_downloader;

import android.content.Context;


import androidx.startup.Initializer;

import com.liulishuo.filedownloader.FileDownloader;

import java.util.ArrayList;
import java.util.List;

public class InitForDownloaderAndroid implements Initializer<String> {

    @Override
    public String create(Context context) {
        FileDownloader.setup(context.getApplicationContext());
        return "Dokit";
    }

    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return new ArrayList<>();
    }


}
