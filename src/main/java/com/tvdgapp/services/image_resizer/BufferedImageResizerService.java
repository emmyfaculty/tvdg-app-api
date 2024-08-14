package com.tvdgapp.services.image_resizer;

import com.tvdgapp.config.FileStorageProperties;
import com.tvdgapp.exceptions.TvdgAppSystemException;
import jakarta.annotation.Nullable;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Component
public class BufferedImageResizerService implements ImageResizeService {

    @Value("${info.root-dir}")
    private @Nullable String rootDir;

    private final FileStorageProperties fileStorageProperties;

    @Autowired
    public BufferedImageResizerService(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
    }

    @Override
    public File resizeImage(File file, int width, int height, String resizedFileName) {
        try {
            InputStream in = new FileInputStream(file);
            return this.resize(in, FilenameUtils.getExtension(file.getName()), width, height, resizedFileName);
        } catch (Exception e) {
            throw new TvdgAppSystemException("Unable to resize image", e);
        }
    }

    private File resize(InputStream inputStream, String fileType, int width, int height, String resizedFileName) {
        if (fileStorageProperties.getUploadDirTmp() == null) {
            throw new TvdgAppSystemException("Upload tmp directory not configured");
        }
        File resizeFile = null;
        try {

            BufferedImage originalImage = ImageIO.read(inputStream);
            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
            BufferedImage resizeImage = resizeImageWithHint(originalImage, type, width, height);
            StringBuilder filenamePathBuilder = new StringBuilder();
            filenamePathBuilder.append(rootDir).append(File.separator).append(fileStorageProperties.getUploadDirTmp());
            Path fileStorageLocation = Paths.get(filenamePathBuilder.toString()).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            filenamePathBuilder.append(File.separator).append(resizedFileName);
            resizeFile = new File(filenamePathBuilder.toString());
            ImageIO.write(resizeImage, fileType, resizeFile);
            return resizeFile;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new TvdgAppSystemException("Unable to resize image", ex);
        } finally {
            //file.delete();
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height) {

        // Infer the scaling factor to avoid stretching the image
        // unnaturally
		/*float scalingFactor = Math.min(MAX_WIDTH / srcWidth, MAX_HEIGHT
				/ srcHeight);
		int width = (int) (scalingFactor * srcWidth);
		int height = (int) (scalingFactor * srcHeight);*/

        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type, int width, int height) {

        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return resizedImage;
    }

    @Override
    public File resizeImage(File file, int newWidth, int newHeight) {
        return this.resizeImage(file, newWidth, newHeight, file.getName());
    }

    @Override
    public File resizeImage(InputStream inputStream, String fileType, int newWidth, int newHeight) {
        return this.resize(inputStream, fileType, newWidth, newHeight, "");
    }


}



