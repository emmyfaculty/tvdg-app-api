package com.tvdgapp.utils;

import com.tvdgapp.constants.Constants;
import com.tvdgapp.models.shipment.Shipment;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;

public class FilePathUtils {

    public static String buildOrganizationFileLocation(Shipment shipment, String dir) {
        //user dir+resource dir
        return shipment.getTrackingNumber()+ File.separator + dir;
    }

    public static String buildUniqueFileName(File file){
        //return LocalDateTime.now()+"."+ FilenameUtils.getExtension(file.getName());
        return RandomStringUtils.randomAlphanumeric(30)+"."+ FilenameUtils.getExtension(file.getName());
    }

    public static String buildUniqueFileName(MultipartFile file){
        //return LocalDateTime.now() +"."+FilenameUtils.getExtension(file.getOriginalFilename());
        return RandomStringUtils.randomAlphanumeric(30)+"."+ FilenameUtils.getExtension(file.getOriginalFilename());
    }

    public static String getFileNameFromFileNamePath(String fileNamePath) {
        /*if(fileNamePath==null){
           return null;
        }*/
        Objects.requireNonNull(fileNamePath);
        if(!fileNamePath.contains(File.separator)){
            return fileNamePath;
        }
       return fileNamePath.substring(fileNamePath.lastIndexOf(File.separator) + 1);
    }


    public static String getPathFromFileNamePath(String fileNamePath) {
        Objects.requireNonNull(fileNamePath);
        /*if(fileNamePath==null){
            return null;
        }*/
        if(!fileNamePath.contains(File.separator)){
            return fileNamePath;
        }
        return fileNamePath.substring(0,fileNamePath.lastIndexOf(File.separator));
    }


    public static String buildCustomerUserProfilePicUploadPath() {
        return Constants.CUSTOMER_USER_PROFILE_PIC_DIR;
    }

    public static String buildEngineerThumbnailUploadPath() {
        return Constants.CUSTOMER_USER_PROFILE_PIC_DIR+File.separator+"thumbnails";
    }

    public static boolean fileHasNewExtension(String filename1, String filename2) {
       return !FilenameUtils.getExtension(filename1).equals(FilenameUtils.getExtension(filename2));
    }

    public static String buildAdminUserProfilePicUploadPath() {
        return Constants.PROFILE_PIC_DIR;
    }
    public static String buildRiderUserProfilePicUploadPath() {
        return Constants.PROFILE_PIC_DIR;
    }
    public static String buildFileClaimsReceiptUploadPath() {
        return Constants.FILE_CLAIMS_RECEIPT_DIR;
    }


    public static String buildFileUrl(String storageLocation, String dir, String fileName) {
        return storageLocation+File.separator+dir+File.separator+fileName;
    }

}
