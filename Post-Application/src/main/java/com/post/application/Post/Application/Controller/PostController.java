package com.post.application.Post.Application.Controller;


import com.post.application.Post.Application.Entity.Post;
import com.post.application.Post.Application.Service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;


    @GetMapping("/count")
    public  ResponseEntity<?> getPostCount(){
        return new ResponseEntity<>(postService.getPostCount(), HttpStatus.OK);
    }


    @GetMapping("/unique")
    public ResponseEntity<?>   getUniqueUserIds(){
        return new ResponseEntity<>(postService.getUniqueUserPosts(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@Valid @RequestBody Post post, @PathVariable Long id){
        return new ResponseEntity<>(postService.updatePost(post,id),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id){
        return new ResponseEntity<>(postService.getPostById(id),HttpStatus.OK);
    }

    @GetMapping("/user/distinct")
    public ResponseEntity<?>   getDistinctUserIds(){
        return new ResponseEntity<>(postService.getDistinctUserIds(), HttpStatus.OK);
    }
    @GetMapping("/")
    public ResponseEntity<?>   getAllPosts(){
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }
}
