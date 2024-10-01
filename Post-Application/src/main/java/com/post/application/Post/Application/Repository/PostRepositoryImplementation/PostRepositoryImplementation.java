package com.post.application.Post.Application.Repository.PostRepositoryImplementation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.post.application.Post.Application.Entity.Post;
import com.post.application.Post.Application.Repository.PostRepository;
import com.post.application.Post.Application.Service.ServiceImplementation.PostServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.post.application.Post.Application.Helper.Constants.*;

@Repository
public class PostRepositoryImplementation implements PostRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImplementation.class);

    @Autowired
   private ObjectMapper ObjectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Post> getPosts() {
            Post[] postsArray = restTemplate.getForObject(BASE_URL+POSTS, Post[].class);
            return Arrays.asList(postsArray);
    }



    @Override
    public List<Post> readPosts() {
    List<Post> postList=new ArrayList<>();
        try {
       postList = ObjectMapper.readValue(new File(POST_FILE_NAME), new TypeReference<>() {});
        } catch (IOException e) {
     logger.error("File not present");
    }
        return postList;
}

    @Override
    public boolean writePosts(List<Post> posts) {
     boolean flag=false;
        try {
            ObjectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(POST_FILE_NAME), posts);
            flag=true;
        } catch (IOException e) {
            logger.error("Could not write on file");
            return flag;
        }
        return flag;
    }



}
