package com.post.application.Post.Application.Service.ServiceImplementation;

import com.post.application.Post.Application.Entity.Post;
import com.post.application.Post.Application.Exception.PostNotFoundException;
import com.post.application.Post.Application.Repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostServiceImpulTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImplementation postServiceImpul;

    private List<Post> mockPosts;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Post post1 = new Post(1L, 1L, "Title 1", "Body 1");
        Post post2 = new Post(2L, 2L, "Title 2", "Body 2");
        Post post3 = new Post(3L, 3L, "Title 3", "Body 3");
        Post post4 = new Post(3L, 4L, "Title 4", "Body 4");
        mockPosts = Arrays.asList(post1,post2,post3,post4);
        when(postRepository.readPosts()).thenReturn(mockPosts);
    }

    @Test
    void testGetPostCount() {
        when(postRepository.readPosts()).thenReturn(mockPosts);
        int postCount = postServiceImpul.getPostCount();
        assertEquals(4, postCount);
    }

    @Test
    void testGetUniqueUserPosts() {
        when(this.postServiceImpul.getUniqueUserPosts()).thenReturn(mockPosts);
        List<Post> uniqueUserPosts = postServiceImpul.getUniqueUserPosts();
        assertEquals(2, uniqueUserPosts.size());
        assertEquals(1, uniqueUserPosts.get(0).getUserId());
        assertEquals(2, uniqueUserPosts.get(1).getUserId());
    }

    @Test
    void testUpdatePost_Success() {
        Post updatedPost = new Post();
        updatedPost.setId(2L);
        updatedPost.setUserId(1L);
        updatedPost.setTitle("Updated Title");
        updatedPost.setBody("Updated Body");
        Post result = postServiceImpul.updatePost(updatedPost, 2L);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Body", result.getBody());

    }

    @Test
    void testUpdatePost_PostNotFound() {
        Post updatedPost = new Post();
        updatedPost.setId(1L);
        updatedPost.setUserId(8L);
        updatedPost.setTitle("Updated Title");
        updatedPost.setBody("Updated Body");
        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> {
            postServiceImpul.updatePost(updatedPost, 10L);});
        assertEquals("Id not available", exception.getMessage());

    }

    @Test
    void testGetPostById_Success() {
        Post post = postServiceImpul.getPostById(2L);
        assertNotNull(post);
        assertEquals(2L, post.getId());
        assertEquals("Title 2", post.getTitle());
    }

    @Test
    void testGetPostById_PostNotFound() {
        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> postServiceImpul.getPostById(10L));
        assertEquals("Id not available", exception.getMessage());

    }

    @Test
    void  createJSONFileTest(){
      when(this.postServiceImpul.WriteToJSON()).thenReturn(true);
        boolean writeToJSON = this.postServiceImpul.WriteToJSON();
        assertEquals(writeToJSON,true);
    }

    @Test
    void getAllPostsTest() {
        when(postRepository.readPosts()).thenReturn(mockPosts);
        List<Post> result = postServiceImpul.getAllPosts();
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals("Title 1", result.get(0).getTitle());
        assertEquals("Title 2", result.get(1).getTitle());
    }
}
