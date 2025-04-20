package com.BackEnd.utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownloader {

    public static MultipartFile urlToMultipartFile(String imageUrl) throws Exception {
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();

        String contentType = connection.getContentType(); // image/jpeg, image/png, ...
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        try (InputStream inputStream = connection.getInputStream()) {
            return new MockMultipartFile(
                    "file",                 // field name
                    fileName,              // original file name
                    contentType,           // content type (MIME)
                    inputStream            // file content as InputStream
            );
        }
    }
}