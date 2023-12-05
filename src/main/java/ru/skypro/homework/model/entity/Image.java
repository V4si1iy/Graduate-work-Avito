package ru.skypro.homework.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Image {

    @Id
    private String id;
    private String link;
    private long fileSize;
    private String mediaType;
    private byte[] data;

}
