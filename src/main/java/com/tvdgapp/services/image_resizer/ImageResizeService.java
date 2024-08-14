package com.tvdgapp.services.image_resizer;

import java.io.File;
import java.io.InputStream;

public interface ImageResizeService {
    
    
    public File resizeImage(File file,int newWidth,int newHeight,String resizedFileName);

    public File resizeImage(File file,int newWidth,int newHeight);

    File resizeImage(InputStream inputStream, String fileType, int newWidth, int newHeight);
}
