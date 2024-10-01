package com.post.application.Post.Application.Repository;

import com.post.application.Post.Application.Entity.Post;

import java.util.List;

public interface PostRepository {

     List<Post> readPosts();
     boolean writePosts(List<Post> post);
     List<Post> getPosts();

}
