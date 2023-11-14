package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.exception.EntityExistsException;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.model.dto.Comment;
import ru.skypro.homework.model.dto.Comments;
import ru.skypro.homework.model.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.dto.ExtendedAd;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
@AllArgsConstructor
public class CommentController {
   private final CommentService commentService;


    @Operation(
            tags = "Комментарии",
            summary = "Получить комментарии объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Comments.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content()
                    )
            }
    )
    @GetMapping("{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable(value = "id") int id){
        try {
            Comments comments = commentService.getAdComments((long)id);
            return ResponseEntity.ok(comments);
        }
        catch (EntityNotFoundException e)
        {
            return ResponseEntity.notFound().build();

    }}

    @Operation(
            tags = "Комментарии",
            summary = "Добавить комментарий к объявлению",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Comment.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content()
                    )
            }
    )
    @PostMapping(value = "{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable("id") int commentId, @RequestBody CreateOrUpdateComment comment){
        try {
            Comment newComment = commentService.create((long)commentId,comment);
            return ResponseEntity.ok(newComment);
        }
        catch (EntityExistsException e)
        {
            return ResponseEntity.status(401).build();

        }
    }
    @Operation(
            tags = "Комментарии",
            summary = "Удалить комментарий",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content()
                    )
            }
    )
    @DeleteMapping("{adId}/comments/{commentId}")
    public ResponseEntity<Comment> deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId){
        try {
           commentService.deleteComment((long)commentId);
            return ResponseEntity.ok().build();
        }
        catch (EntityNotFoundException e)
        {
            return ResponseEntity.notFound().build();

        }
    }
    @Operation(
            tags = "Комментарии",
            summary = "Обновить комментарий",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Comment.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content()
                    )
            }
    )
    @PatchMapping("{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer adId,
                                 @PathVariable Integer commentId,
                                 @RequestBody CreateOrUpdateComment comment){
        try {
            Comment newComment = commentService.updateComment((long)commentId,comment);
            return ResponseEntity.ok(newComment);
        }
        catch (EntityNotFoundException e)
        {
            return ResponseEntity.notFound().build();

        }
    }
}
