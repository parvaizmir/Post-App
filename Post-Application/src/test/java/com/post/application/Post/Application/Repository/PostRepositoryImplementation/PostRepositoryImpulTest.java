package com.post.application.Post.Application.Repository.PostRepositoryImplementation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.post.application.Post.Application.Entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.post.application.Post.Application.Helper.Constants.BASE_URL;
import static com.post.application.Post.Application.Helper.Constants.POSTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostRepositoryImpulTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectWriter objectWriter;

    @InjectMocks
    private PostRepositoryImplementation postRepository;


    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetPosts() throws IOException {
        Post post1 = new Post(1L,1L,"Title 1","Body 1");
        Post post2 = new Post(2L,2L,"Title 2","Body 2");
        Post[] postArray={post1,post2};
        when(restTemplate.getForObject(BASE_URL+POSTS, Post[].class)).thenReturn(postArray);
        List<Post> posts = postRepository.getPosts();
        assertNotNull(posts);
        assertEquals(2, posts.size());
        assertEquals("Title 1", posts.get(0).getTitle());
    }


    @Test
    void testReadPostsSuccess() throws IOException {
        List<Post> mockPosts = new ArrayList<>();
        Post post=new Post(1L,1L,"Title 1","Body 1");
        mockPosts.add(post);
        when(objectMapper.readValue(any(File.class), any(TypeReference.class))).thenReturn(mockPosts);
        List<Post> posts = postRepository.readPosts();
        assertNotNull(posts);
        assertEquals(1, posts.size());
        assertEquals("Title 1", posts.get(0).getTitle());
    }

    @Test
    void testReadPostsFileNotFound() throws IOException {
        when(objectMapper.readValue(any(File.class), any(TypeReference.class))).thenThrow(new IOException());
        List<Post> posts = postRepository.readPosts();
        assertNotNull(posts);
        assertTrue(posts.isEmpty());
    }

    @Test
    void testWritePostsSuccess() throws IOException {
        List<Post> mockPosts = new ArrayList<>();
        Post post=new Post(1L,1L,"Title 1","Body 1");
        mockPosts.add(post);
        when(objectMapper.writerWithDefaultPrettyPrinter()).thenReturn(objectWriter);
       doNothing().when(objectMapper).writeValue(any(File.class), any());
        boolean result = postRepository.writePosts(mockPosts);
        assertTrue(result);

    }

}
