package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.exception.EntityExistsException;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.model.dto.*;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
@AllArgsConstructor
public class CommentController {
   private final CommentService commentService;
    private final UserService userService;


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
    public ResponseEntity<Comment> addComment(@PathVariable("id") int adId, @RequestBody CreateOrUpdateComment comment , Authentication authentication){
        try {
            Comment newComment = commentService.create((long)adId,comment,authentication.getName());
            return ResponseEntity.ok(newComment);
        }
        catch (EntityExistsException e)
        {
            return ResponseEntity.status(401).build();

        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
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
    public ResponseEntity<Comment> deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId,Authentication authentication) throws EntityNotFoundException {
        Comment comment = new Comment();
        comment.setPk((long)(commentId));
        RequestWapperCommentDto rq = rq()
                .setAdId(adId)
                .setData(comment)
                .setUsername(authentication.getName());
        comment = commentService.isMine(rq) || userService.getUser(authentication.getName()).getRole() == Role.ADMIN ? commentService.deleteComment(rq) : null;
        checkResult(comment);
        return ResponseEntity.ok().build();
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
                                 @RequestBody Comment comment,Authentication authentication) throws EntityNotFoundException {
        comment.setPk((long)(commentId));
        RequestWapperCommentDto rq = rq()
                .setAdId(adId)
                .setData(comment)
                .setUsername(authentication.getName());
        comment = commentService.isMine(rq) || userService.getUser(authentication.getName()).getRole() == Role.ADMIN ? commentService.updateComment(rq) : null;
        checkResult(comment);
        return ResponseEntity.ok(comment);
    }
    private RequestWapperCommentDto rq() {
        return new RequestWapperCommentDto();
    }

    private void checkAuth(Authentication auth) {
        if (null == auth) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    private void checkResult(Comment comment) {
        if (null == comment) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
