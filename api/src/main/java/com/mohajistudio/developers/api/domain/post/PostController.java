package com.mohajistudio.developers.api.domain.post;

import com.mohajistudio.developers.api.domain.post.dto.request.CreatePostRequest;
import com.mohajistudio.developers.api.domain.post.dto.request.UpdatePostRequest;
import com.mohajistudio.developers.common.enums.ErrorCode;
import com.mohajistudio.developers.common.exception.CustomException;
import com.mohajistudio.developers.database.dto.PostDto;
import com.mohajistudio.developers.database.entity.MediaFile;
import com.mohajistudio.developers.database.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    Page<PostDto> getPosts(Pageable pageable) {
        return postService.findAllPost(pageable);
    }

    @PostMapping
    Post addPost(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CreatePostRequest post) {
        return null;
    }

    @PostMapping(value = "/media", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    List<MediaFile> addMedia(@RequestPart(value = "files", required = false) List<MultipartFile> files) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (files == null || files.isEmpty()) {
            throw new CustomException(ErrorCode.MULTIPART_FILE_EXCEPTION);
        }

        String email = userDetails.getUsername();

        return postService.uploadImages(email, files);
    }

    @GetMapping("/{postId}")
    Post getPost(@PathVariable UUID postId) {
        return null;
    }

    @PatchMapping("/{postId}")
    Post updatePost(@PathVariable UUID postId, @RequestBody UpdatePostRequest post) {
        return null;
    }

    @DeleteMapping("/{postId}")
    void deletePost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID postId) {
    }
}
