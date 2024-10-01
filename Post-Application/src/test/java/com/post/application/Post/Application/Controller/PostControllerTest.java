package com.post.application.Post.Application.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.post.application.Post.Application.Entity.Post;
import com.post.application.Post.Application.Service.PostService;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    private  Post post;
    List<Post> uniquePosts;

    @BeforeEach
    void setUp() {
        post = new Post(1L, 1L, "Title 1", "Body 1");
        uniquePosts = Arrays.asList(
                new Post(1L, 1L, "Title 1", "Body 1"),
                new Post(2L, 2L, "Title 2", "Body 2"));
    }

    @Test
    void testGetPostCount() throws Exception {
        when(postService.getPostCount()).thenReturn(10);
        mockMvc.perform(get("/post/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }


    @Test
    public void testGetUniqueUserPosts_WithUniquePosts() throws Exception {
        List<Post> uniquePosts = Arrays.asList(
                new Post(1L, 1L, "Title 1", "Body 1"),
                new Post(2L, 2L, "Title 2", "Body 2"));
        when(postService.getUniqueUserPosts()).thenReturn(uniquePosts);
        mockMvc.perform(get("/post/unique")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[1].userId").value(2));
    }

    @Test
    void testUpdatePostSuccess() throws Exception {
        post.setTitle("Updated Title");
        post.setBody("Updated Body");
        when(postService.updatePost(any(Post.class), eq(post.getId()))).thenReturn(post);
        mockMvc.perform(put("/post/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONValue.toJSONString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.body").value(post.getBody()));
    }



    @Test
    void testUpdatePostNotFound() throws Exception {
        Post updatedPost = new Post();
        updatedPost.setId(post.getId());
        updatedPost.setUserId(post.getUserId());
        updatedPost.setTitle("Updated Title");
        updatedPost.setBody("Updated Body");
        when(postService.updatePost(any(Post.class), anyLong()))
                .thenThrow( new RuntimeException("Post with this Id not found"));
        mockMvc.perform(put("/post/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONValue.toJSONString(updatedPost)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Post with this Id not found"));
    }

    @Test
    void testGetPostByIdFound() throws Exception {
        when(postService.getPostById(anyLong())).thenReturn(post);
        mockMvc.perform(get("/post/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Title 1"))
                .andExpect(jsonPath("$.body").value("Body 1"));
    }


    @Test
    void testGetPostByIdNotFound() throws Exception {
        when(postService.getPostById(anyLong())).thenThrow(new RuntimeException("Id not available"));
        mockMvc.perform(get("/post/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Id not available"));
    }


    @Test
    void getDistinctUserIds() throws Exception {
        Map<String,Long> users=new HashMap<>();
        users.put("Distinct Users",5L);
        when(postService.getDistinctUserIds()).thenReturn(users);
        mockMvc.perform(get("/post/user/distinct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Distinct Users']").value(5));
    }

    @Test
    void getAllPosts() throws Exception {
        when(postService.getAllPosts()).thenReturn(uniquePosts);

        
        mockMvc.perform(get("/post/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[1].title").value("Title 2"));

    }
}
