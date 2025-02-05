package com.ttdat.productservice.infrastructure.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

    public String uploadToCloudinary(MultipartFile multipartFile) throws IOException {
        File file = convertMultiPartToFile(multipartFile);
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", "/agrohub/product"));
        file.delete();
        return uploadResult.get("url").toString();
    }

}
