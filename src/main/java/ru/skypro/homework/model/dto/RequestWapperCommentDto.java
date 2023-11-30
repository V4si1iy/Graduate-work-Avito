package ru.skypro.homework.model.dto;

import lombok.Data;

@Data
public class RequestWapperCommentDto {
    public Integer adId;
    public Comment data;
    public String username;

    public RequestWapperCommentDto setAdId(Integer adId) {
        this.adId = adId;
        return this;
    }

    public RequestWapperCommentDto setData(Comment data) {
        this.data = data;
        return this;
    }

    public RequestWapperCommentDto setUsername(String username) {
        this.username = username;
        return this;
    }
}
