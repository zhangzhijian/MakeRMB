package com.dream.makermb.constant;

import android.os.Environment;

import java.io.File;

public class Dir {

    public static final String ROOT_DIR = Environment.getExternalStorageDirectory().getPath() + "/mm/";
    public static final String IMAGE_DIR = ROOT_DIR + "/images/";
    public static final String FILE_DIR = ROOT_DIR + "/download/";

    static {
        if (hasSDCard()) {
            File file = new File(ROOT_DIR);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(IMAGE_DIR);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(FILE_DIR);
            if (!file.exists()) {
                file.mkdirs();
            }
        }

    }

    public static boolean hasSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

}
