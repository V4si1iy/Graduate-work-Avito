package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.EntityExistsException;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.exception.NoAccessException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.dto.Comment;
import ru.skypro.homework.model.dto.Comments;
import ru.skypro.homework.model.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.dto.Role;
import ru.skypro.homework.model.entity.AdModel;
import ru.skypro.homework.model.entity.CommentModel;
import ru.skypro.homework.model.entity.UserModel;
import ru.skypro.homework.repository.CommentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final CommentMapper commentMapper;
    private final AdService adService;
    private final UserService userService;

    public Comment create(Long adId, CreateOrUpdateComment comment,String author) throws EntityExistsException, EntityNotFoundException {
        log.info("Executing the method to create a new Comment");

        // Преобразование DTO в сущность
        CommentModel commentModel = commentMapper.createOrUpdateCommentToCommentModel(comment);
        AdModel adModel = adService.getAdById(adId);
        UserModel userModel = userService.getUserByUsername(author);
        commentModel.setUser(userModel);
        commentModel.setAds(adModel);
        commentModel.setCreatedAt(LocalDateTime.now());

        // Сохранение сущности
        CommentModel savedCommentModel = repository.save(commentModel);

        // Преобразование сохраненной сущности обратно в DTO и возврат
        Comment savedComment = commentMapper.mapCommentModelToCommentDto(savedCommentModel);

        log.info("Comment with id {} created successfully", savedComment.getPk());
        return savedComment;
    }

    protected CommentModel getCommentById(Long commentId) throws EntityNotFoundException {
        log.info("Fetching Comment with id {}", commentId);

        try {
            CommentModel commentModel = repository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Comment with id " + commentId + " not found"));

            log.info("Fetched Comment: {}", commentModel);
            return commentModel;
        } catch (Exception e) {
            log.error("Error fetching Comment with id {}", commentId, e);
            throw e;
        }
    }

    public Comment updateComment(Long commentId, CreateOrUpdateComment updatedComment ,String user) throws EntityNotFoundException, NoAccessException {
        log.info("Updating Comment with id {}", commentId);

        // Преобразование DTO в сущность
        CommentModel commentModel = commentMapper.createOrUpdateCommentToCommentModel(updatedComment);

        UserModel userModel= userService.getUserByUsername(user);
        if(userModel.getRole() != Role.ADMIN && commentModel.getUser() != userModel) {
            log.error("Error update Comment with id {}", commentId);
            throw new NoAccessException("User with username" + userModel.getUsername() + "don't have permission");
        }

        // Проверка существования объявления
        CommentModel existingComment = getCommentById(commentId);
        commentModel.setUser(existingComment.getUser());
        commentModel.setAds(existingComment.getAds());
        commentModel.setCreatedAt(LocalDateTime.now());

        // Сохранение обновленной сущности
        CommentModel updatedCommentModel = repository.save(commentModel);

        log.info("Comment with id {} updated successfully", commentId);
        return commentMapper.mapCommentModelToCommentDto(updatedCommentModel);
    }

    public void deleteComment(Long commentId, String user) throws EntityNotFoundException, NoAccessException {
        log.info("Deleting Comment with id {}", commentId);
        try {
            CommentModel existingCommentModel = repository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Comment with id " + commentId + " not found"));

            UserModel userModel= userService.getUserByUsername(user);
            if(userModel.getRole() != Role.ADMIN && existingCommentModel.getUser() != userModel) {
                throw new NoAccessException("User with username" + userModel.getUsername() + "don't have permission");
            }

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
//        if (commentModels.size()==0)
//            throw new EntityNotFoundException("Comments don't found");

        allComments.setResults(commentModels.stream().map(commentMapper::mapCommentModelToCommentDto).collect(Collectors.toList()));
        return allComments;
    }
}
