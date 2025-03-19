package com.dailycodework.shoppingcart.service.image;

import com.dailycodework.shoppingcart.dto.ImageDto;
import com.dailycodework.shoppingcart.exceptions.ResourceNotFoundException;
import com.dailycodework.shoppingcart.model.Image;
import com.dailycodework.shoppingcart.model.Product;
import com.dailycodework.shoppingcart.repository.ImageRepository;
import com.dailycodework.shoppingcart.service.product.IProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
//@RequiredArgsConstructor

public class ImageService implements IImageService{
    private final ImageRepository imageRepository;
    private final IProductService iProductService;

    public ImageService(ImageRepository imageRepository, IProductService iProductService){
        this.imageRepository = imageRepository;
        this.iProductService = iProductService;
    }
    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("No image found with id: " + id);
        });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = iProductService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for(MultipartFile file : files){
            try{
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);
                imageRepository.save(image);

                Image savedImage = imageRepository.save(image);
                image.setDownloadUrl(buildDownloadUrl + savedImage.getId());


                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        }catch (IOException | SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
