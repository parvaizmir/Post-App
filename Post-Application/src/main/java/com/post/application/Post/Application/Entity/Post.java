package com.post.application.Post.Application.Entity;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class Post {
        private Long userId;
        private Long id;
        @NotBlank( message = "Title can not be empty")
        private String title;
        @NotBlank( message = "Body can not be empty")
        private String body;

        public Post() {
        }

        public Post( Long userId,Long id, String title, String body) {
                this.body = body;
                this.id = id;
                this.title = title;
                this.userId = userId;
        }

        public String getBody() {
                return body;
        }

        public void setBody(String body) {
                this.body = body;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public Long getUserId() {
                return userId;
        }

        public void setUserId(Long userId) {
                this.userId = userId;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Post post)) return false;
            return Objects.equals(id, post.id);
        }

        @Override
        public int hashCode() {
                return Objects.hashCode(id);
        }

        @Override
        public String toString() {
                return "Post{" +
                        "userId=" + userId +
                        ", id=" + id +
                        ", title='" + title + '\'' +
                        ", body='" + body + '\'' +
                        '}';
        }
}

