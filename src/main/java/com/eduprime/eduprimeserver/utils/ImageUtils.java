package com.eduprime.eduprimeserver.utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ImageUtils {

    public static String getImageFormat(byte[] imageData) {
        try {
            // Đọc định dạng ảnh từ dữ liệu hình ảnh
            ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(imageData));
            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);

            if (imageReaders.hasNext()) {
                ImageReader reader = imageReaders.next();
                iis.close();

                return reader.getFormatName().toLowerCase();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "png";
    }
}
