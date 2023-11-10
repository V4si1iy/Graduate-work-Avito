package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.EntityExistsException;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.dto.Comment;
import ru.skypro.homework.model.dto.Comments;
import ru.skypro.homework.model.entity.AdModel;
import ru.skypro.homework.model.entity.CommentModel;
import ru.skypro.homework.repository.CommentRepository;

import java.util.stream.Collectors;
@Service
@Slf4j
@AllArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final CommentMapper commentMapper;

    public Comment create(Comment comment) throws EntityExistsException {
        log.info("Executing the method to create a new Comment");

        // Преобразование DTO в сущность
        CommentModel commentModel = commentMapper.mapCommentDtoToCommentModel(comment);

        // Проверка наличия объявления по уникальному идентификатору
        if (commentModel.getId() != null && repository.existsById(commentModel.getId())) {
            log.error("Failed to create Comment. Comment with id {} already exists.", commentModel.getId());
            throw new EntityExistsException("Comment with id " + commentModel.getId() + " already exists");
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

    public Comment updateComment(Long commentId, Comment updatedComment) throws EntityNotFoundException {
        log.info("Updating Comment with id {}", commentId);

        // Преобразование DTO в сущность
        CommentModel commentModel = commentMapper.mapCommentDtoToCommentModel(updatedComment);
        // Проверка существования объявления
        Comment existingComment = getCommentById(commentModel.getId());

        // Сохранение обновленной сущности
        CommentModel updatedCommentModel = repository.save(commentModel);

        log.info("Comment with id {} updated successfully", commentId);
        return commentMapper.mapCommentModelToCommentDto(updatedCommentModel);
    }

    public void deleteComment(Long commentId) throws EntityNotFoundException {
        log.info("Deleting Comment with id {}", commentId);
        try {
            CommentModel existingCommentModel = repository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Ad with id " + commentId + " not found"));

            repository.delete(existingCommentModel);

            log.info("Comment with id {} deleted successfully", commentId);
        } catch (Exception e) {
            log.error("Error deleting Comment with id {}", commentId, e);
            throw e;
        }
    }

    public Comments getAllComments() {
        log.info("Fetching all Comments");
        Comments allComments = new Comments();
        // Получение списка всех объявлений
        allComments.setCount(Integer.parseInt(String.valueOf(repository.count())));
        allComments.setResult(repository.findAll().stream().map(commentMapper::mapCommentModelToCommentDto).collect(Collectors.toList()));
        return allComments;
    }
}
