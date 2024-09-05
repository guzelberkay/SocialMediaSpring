package com.berkay.controller;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.berkay.dto.response.ResponseDto;
import com.berkay.service.MediaService;
import com.berkay.utility.BucketSubDirectoryName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
@CrossOrigin(origins = "*",methods = {RequestMethod.POST,RequestMethod.GET})
public class MediaController {

    private final MediaService mediaService;

    @PostMapping(value = "/add-storage-avatar")
    @CrossOrigin("*")
    public ResponseEntity<ResponseDto<String>> uploadAvatarFile(@RequestParam("file")MultipartFile file) throws IOException {
            return  ResponseEntity.ok(
                    ResponseDto.<String>builder()
                            .data(mediaService.uploadAvatarPhotos(file))
                            .message("ok")
                            .code(200)
                            .build()
            );
    }

    @PostMapping(value = "/add-storage-post")
    @CrossOrigin("*")
    public ResponseEntity<ResponseDto<String>> uploadPostFile(@RequestParam("file") MultipartFile file) throws IOException {
        return  ResponseEntity.ok(
                ResponseDto.<String>builder()
                        .data(mediaService.uploadPostPhotos(file))
                        .message("ok")
                        .code(200)
                        .build()
        );
    }

    @GetMapping("/test")
    public void test(){
        mediaService.getPhotoUrl(BucketSubDirectoryName.AVATAR,"fcc56ead-a1fc-4dfa-a864-4915674a7e23.png");
    }


}
