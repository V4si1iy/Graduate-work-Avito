package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.EntityExistsException;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.dto.Ads;
import ru.skypro.homework.model.dto.Comment;
import ru.skypro.homework.model.dto.Comments;
import ru.skypro.homework.model.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.entity.AdModel;
import ru.skypro.homework.model.entity.CommentModel;
import ru.skypro.homework.model.entity.UserModel;
import ru.skypro.homework.repository.CommentRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Service
@Slf4j
@AllArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final CommentMapper commentMapper;

    public Comment create(Long commentId , CreateOrUpdateComment comment) throws EntityExistsException {
        log.info("Executing the method to create a new Comment");

        // Преобразование DTO в сущность
        CommentModel commentModel = commentMapper.createOrUpdateCommentToCommentModel(comment);

        // Проверка наличия объявления по уникальному идентификатору
        if (commentId != null && repository.existsById(commentId)) {
            log.error("Failed to create Comment. Comment with id {} already exists.", commentId);
            throw new EntityExistsException("Comment with id " + commentId + " already exists");
        }

        // Сохранение сущности
        CommentModel savedCommentModel = repository.save(commentModel);

        // Преобразование сохраненной сущности обратно в DTO и возврат
        Comment savedComment = commentMapper.mapCommentModelToCommentDto(savedCommentModel);

        log.info("Comment with id {} created successfully", savedComment.getPk());
        return savedComment;
    }

    public Comment getCommentById(Long commentId) throws EntityNotFoundException {
        log.info("Fetching Comment with id {}", commentId);

        try {
            CommentModel commentModel = repository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Comment with id " + commentId + " not found"));

            log.info("Fetched Comment: {}", commentModel);
            return commentMapper.mapCommentModelToCommentDto(commentModel);
        } catch (Exception e) {
            log.error("Error fetching Comment with id {}", commentId, e);
            throw e;
        }
    }

    public Comment updateComment(Long commentId, CreateOrUpdateComment updatedComment) throws EntityNotFoundException {
        log.info("Updating Comment with id {}", commentId);

        // Преобразование DTO в сущность
        CommentModel commentModel = commentMapper.createOrUpdateCommentToCommentModel(updatedComment);
        // Проверка существования объявления
        Comment existingComment = getCommentById(commentId);

        // Сохранение обновленной сущности
        CommentModel updatedCommentModel = repository.save(commentModel);

        log.info("Comment with id {} updated successfully", commentId);
        return commentMapper.mapCommentModelToCommentDto(updatedCommentModel);
    }

    public void deleteComment(Long commentId) throws EntityNotFoundException {
        log.info("Deleting Comment with id {}", commentId);
        try {
            CommentModel existingCommentModel = repository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Comment with id " + commentId + " not found"));

            repository.delete(existingCommentModel);

            log.info("Comment with id {} deleted successfully", commentId);
        } catch (Exception e) {
            log.error("Error deleting Comment with id {}", commentId, e);
            throw e;
        }
    }

    public Comments getAdComments(Long adId) throws EntityNotFoundException {
        log.info("Fetching ad Comments");
        Comments allComments = new Comments();
        // Получение списка всех комментариев

        allComments.setCount(repository.countByAds_Id(adId));
        List<CommentModel> commentModels = repository.getAllByAds_Id(adId);
        if(Objects.isNull(commentModels))
            throw new EntityNotFoundException("Comments don't found");

        allComments.setResult(commentModels.stream().map(commentMapper::mapCommentModelToCommentDto).collect(Collectors.toList()));
        return allComments;
    }
}
