package com.post.application.Post.Application.Service.ServiceImplementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.post.application.Post.Application.Entity.Post;
import com.post.application.Post.Application.Exception.PostNotFoundException;
import com.post.application.Post.Application.Repository.PostRepository;
import com.post.application.Post.Application.Service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.post.application.Post.Application.Helper.Constants.POST_FILE_NAME;

@Service
public class PostServiceImplementation implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImplementation.class);


    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public int getPostCount() {
        if(!JSONExists())
            this.WriteToJSON();
        return this.postRepository.readPosts().size();
    }

    @Override
    public List<Post> getUniqueUserPosts() {
        if(!JSONExists())
            this.WriteToJSON();
        List<Post> UniqueUserPost = this.postRepository.readPosts().stream()
                .collect(Collectors.groupingBy(Post::getUserId))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() == 1)
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toList());
        return UniqueUserPost;
    }

    @Override
    public Post updatePost(Post post, Long id) {
        if(!JSONExists())
            this.WriteToJSON();
        List<Post> postList = this.postRepository.readPosts();
        Post updatedPost = getPostById(id);
        updatedPost.setTitle(post.getTitle());
        updatedPost.setBody(post.getBody());
        postList.set(postList.indexOf(updatedPost),updatedPost);
        this.postRepository.writePosts(postList);
        return updatedPost;
    }

    @Override
    public Map<String, Long> getDistinctUserIds() {
        long count = this.postRepository.readPosts().stream().map(Post::getUserId).distinct().count();
        Map<String,Long> distinctCount=new HashMap<>();
        distinctCount.put("Distinct Users",count);
        return distinctCount;


    }

    @Override
    public List<Post> getAllPosts() {
        if(!JSONExists())
            this.WriteToJSON();
       return this.postRepository.readPosts();
    }

    @Override
    public Post getPostById(Long id) {
        if(!JSONExists())
            this.WriteToJSON();
        Optional<Post> postById = this.postRepository.readPosts().stream().filter(post -> post.getId().equals(id)).findFirst();
       return postById.orElseThrow(() -> new PostNotFoundException("Id not available"));
    }

    @Override
    public boolean WriteToJSON(){
        List<Post> posts = this.postRepository.getPosts();
        return this.postRepository.writePosts(posts);
    }


    public boolean JSONExists(){
        return  Files.exists(Paths.get(POST_FILE_NAME));
    }


}