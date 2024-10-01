package com.post.application.Post.Application.Service;

import com.post.application.Post.Application.Entity.Post;
import java.util.List;
import java.util.Map;

public interface PostService {


    int getPostCount();

    List<Post> getUniqueUserPosts();

    Post updatePost(Post post, Long id);

    Post getPostById(Long id);
    boolean WriteToJSON() ;
    Map<String, Long> getDistinctUserIds();
    List<Post> getAllPosts();


}
