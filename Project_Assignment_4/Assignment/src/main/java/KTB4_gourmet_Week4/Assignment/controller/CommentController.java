package KTB4_gourmet_Week4.Assignment.controller;

import KTB4_gourmet_Week4.Assignment.dto.CommentRequestDto;
import KTB4_gourmet_Week4.Assignment.dto.CommentResponseDto;
import KTB4_gourmet_Week4.Assignment.entity.Comment;
import KTB4_gourmet_Week4.Assignment.entity.Post;
import KTB4_gourmet_Week4.Assignment.entity.User;
import KTB4_gourmet_Week4.Assignment.store.MemoryStore;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    @PostMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(
            @PathVariable Long postId,
            @PathVariable Long userId,
            @RequestBody CommentRequestDto request
    ) {
        Post post = MemoryStore.posts.get(postId);
        User user = MemoryStore.users.get(userId);

        if (post == null) {
            throw new IllegalArgumentException("post not found");
        }

        if (user == null) {
            throw new IllegalArgumentException("user not found");
        }

        Long id = MemoryStore.commentId++;

        Comment comment = new Comment(
                id,
                postId,
                userId,
                request.getContent()
        );

        MemoryStore.comments.put(id, comment);

        return new CommentResponseDto(comment);
    }

    @GetMapping("/{commentId}")
    public CommentResponseDto getComment(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        Comment comment = MemoryStore.comments.get(commentId);

        if (comment == null || !comment.getPostId().equals(postId)) {
            throw new IllegalArgumentException("comment not found");
        }

        return new CommentResponseDto(comment);
    }

    @PatchMapping("/{commentId}")
    public CommentResponseDto updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto request
    ) {
        Comment comment = MemoryStore.comments.get(commentId);

        if (comment == null || !comment.getPostId().equals(postId)) {
            throw new IllegalArgumentException("comment not found");
        }

        comment.update(request.getContent());

        return new CommentResponseDto(comment);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        Comment comment = MemoryStore.comments.get(commentId);

        if (comment == null || !comment.getPostId().equals(postId)) {
            throw new IllegalArgumentException("comment not found");
        }

        MemoryStore.comments.remove(commentId);
    }
}