package com.BackEnd.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryyService {

    private final Cloudinary cloudinary;
    public CloudinaryyService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public List<String> uploadImages(List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();
        for(MultipartFile file : files){
            Map uploadOptions = ObjectUtils.asMap(
                    "resource_type", "auto", // resources type
                    "folder", "AutoParts",// folder on cloudinary
                    "public_id", file.getOriginalFilename()
                    //"tags", "batch_upload"    // tag on resources
            );
            try{
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);

                imageUrls.add(uploadResult.get("url").toString());
            }catch(IOException e){
               e.printStackTrace();
            }

        }
        return imageUrls;
    }




}
