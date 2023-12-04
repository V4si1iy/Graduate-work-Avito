package ru.skypro.homework.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skypro.homework.model.entity.Image;
import ru.skypro.homework.repository.ImageRepository;

@Controller
@RequestMapping("/image")
@CrossOrigin(value = "http://localhost:3000")
@AllArgsConstructor
public class ImageController {
    private final ImageRepository imageRepository;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImageFromDB(@PathVariable("id") String id) {
        HttpHeaders headers = new HttpHeaders();
        Image image = imageRepository.getById(id);
        headers.setContentType(MediaType.parseMediaType(image.getMediaType()));
        headers.setContentLength(image.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(image.getData());
    }
}
