package com.dailycodework.shoppingcart.controller;

import com.dailycodework.shoppingcart.dto.ImageDto;
import com.dailycodework.shoppingcart.exceptions.ResourceNotFoundException;
import com.dailycodework.shoppingcart.model.Image;
import com.dailycodework.shoppingcart.response.ApiResponse;
import com.dailycodework.shoppingcart.service.image.IImageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@AllArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService imageService;


    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId){
        try{
            List<ImageDto> imageDtos = imageService.saveImages(files , productId);
            return ResponseEntity.ok(new ApiResponse("Upload success!", imageDtos));
        }catch(Exception e){
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed!", e.getMessage()));
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed!", e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> download(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1,(int) image.getImage().length()));//ByteArrayResource is suitable method tp return files in restful apis
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +image.getFileName() + "\"")
                .body(resource);
    }


    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId , @RequestBody MultipartFile file){
        try{
            Image image = imageService.getImageById(imageId);
            if(image != null){//I think this check is unnecessary
                imageService.updateImage(file,imageId);
                return ResponseEntity.ok(new ApiResponse("Update success!", null));
            }
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed!" , INTERNAL_SERVER_ERROR));
    }



    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId){
        try{
            Image image = imageService.getImageById(imageId);
            if(image != null){
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("delete success!", null));
            }
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("delete failed!" , INTERNAL_SERVER_ERROR));
    }
}
