/*
package KTB4_gourmet_Week4.Assignment.controller;

import KTB4_gourmet_Week4.Assignment.dto.PostRequestDto;
import KTB4_gourmet_Week4.Assignment.dto.PostResponseDto;
import KTB4_gourmet_Week4.Assignment.entity.Post;
import KTB4_gourmet_Week4.Assignment.entity.User;
import KTB4_gourmet_Week4.Assignment.repository.PostRepository;
import KTB4_gourmet_Week4.Assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @PostMapping("/users/{userId}")
    @Transactional
    public PostResponseDto createPost(
            @PathVariable Long userId,
            @RequestBody PostRequestDto request
    ) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = new Post(
                request.getTitle(),
                request.getContent(),
                author
        );

        Post savedPost = postRepository.save(post);
        return new PostResponseDto(savedPost);
    }

    @GetMapping("/{postId}")
    public PostResponseDto getPost(@PathVariable Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        return new PostResponseDto(post);
    }

    @PutMapping("/{postId}")
    @Transactional
    public PostResponseDto updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto request
    ) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        post.changeTitle(request.getTitle());
        post.changeContent(request.getContent());

        return new PostResponseDto(post);
    }

    @DeleteMapping("/{postId}")
    @Transactional
    public void deletePost(@PathVariable Long postId) {
        postRepository.deleteById(postId);
    }
}
*/

package KTB4_gourmet_Week4.Assignment.controller;

import KTB4_gourmet_Week4.Assignment.dto.PostRequestDto;
import KTB4_gourmet_Week4.Assignment.dto.PostResponseDto;
import KTB4_gourmet_Week4.Assignment.entity.Post;
import KTB4_gourmet_Week4.Assignment.entity.User;
import KTB4_gourmet_Week4.Assignment.store.MemoryStore;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @PostMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseDto createPost(
            @PathVariable Long userId,
            @RequestBody PostRequestDto request
    ) {
        User user = MemoryStore.users.get(userId);

        if (user == null) {
            throw new IllegalArgumentException("user not found");
        }

        Long id = MemoryStore.postId++;

        Post post = new Post(
                id,
                userId,
                request.getTitle(),
                request.getContent()
        );

        MemoryStore.posts.put(id, post);

        return new PostResponseDto(post);
    }

    @GetMapping
    public List<PostResponseDto> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return MemoryStore.posts.values().stream()
                .skip((long) page * size)
                .limit(size)
                .map(PostResponseDto::new)
                .toList();
    }

    @GetMapping("/{postId}")
    public PostResponseDto getPost(@PathVariable Long postId) {
        Post post = MemoryStore.posts.get(postId);

        if (post == null) {
            throw new IllegalArgumentException("post not found");
        }

        return new PostResponseDto(post);
    }

    @PatchMapping("/{postId}")
    public PostResponseDto updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto request
    ) {
        Post post = MemoryStore.posts.get(postId);

        if (post == null) {
            throw new IllegalArgumentException("post not found");
        }

        post.update(request.getTitle(), request.getContent());

        return new PostResponseDto(post);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long postId) {
        MemoryStore.posts.remove(postId);
    }
}
