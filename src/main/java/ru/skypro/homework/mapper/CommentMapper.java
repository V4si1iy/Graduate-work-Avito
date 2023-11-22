package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.Comment;
import ru.skypro.homework.model.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.entity.CommentModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Mapper(componentModel = "spring")
@Component
public interface CommentMapper {

    default Comment mapCommentModelToCommentDto(CommentModel commentModel) {
        Comment comment = new Comment();
        comment.setAuthor(commentModel.getUser().getId());
        comment.setAuthorImage(String.valueOf(Optional.ofNullable(commentModel.getUser())
                .orElse(null)));
        comment.setCreatedAt(LocalDateTime.parse(commentModel.getCreatedAt().toString()));
        comment.setText(commentModel.getText());
        return comment;
    }
    default CommentModel mapCommentDtoToCommentModel(Comment comment) {
        CommentModel commentModel = new CommentModel();
        if (null != comment.getPk()) {
            commentModel.setId(comment.getPk().longValue());
        }
        if (null != comment.getCreatedAt()) {
            commentModel.setCreatedAt(comment.getCreatedAt());
        }
        if (null != comment.getText()) {
            commentModel.setText(comment.getText());
        }
        return commentModel;
    }
    default CommentModel createOrUpdateCommentToCommentModel(CreateOrUpdateComment createOrUpdateComment){
        CommentModel commentModel = new CommentModel();
        commentModel.setText(createOrUpdateComment.getText());
        return commentModel;
    }

}
