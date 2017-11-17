package com.udacity.gradle.builditbigger;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by joeljohnson on 11/16/17.
 */

public class PhotoFilter implements FileFilter {

    String[] validTypes = new String[]{"png", "jpg"};

    @Override
    public boolean accept(File file) {
        String extension = getFileExtension(file);
        if (!extension.equals("")) {
            for (String type: validTypes){
                if (type.equals(extension.toLowerCase())){
                    return true;
                }
            }
        }
        return false;
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}
