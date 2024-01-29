package ControllerTest;


import Controller.ArticleController;
import Model.Article;
import Service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;

class ArticleControllerTest {

    @Mock
    private ArticleService articleService;

    @InjectMocks
    private ArticleController articleController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllArticles() throws Exception {
        // Arrange
        when(articleService.getAllArticles()).thenReturn(Arrays.asList(
                new Article(1L, "Title 1", "Content 1"),
                new Article(2L, "Title 2", "Content 2")
        ));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/articles"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    void getArticleById() throws Exception {
        // Arrange
        when(articleService.getArticleById(1L)).thenReturn(new Article(1L, "Title 1", "Content 1"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/articles/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Content 1"));
    }

    @Test
    void createArticle() throws Exception {
        // Arrange
        Article newArticle = new Article(null, "New Title", "New Content");
        when(articleService.createArticle(any(Article.class))).thenReturn(newArticle);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newArticle)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("New Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("New Content"));
    }

    @Test
    void updateArticle() throws Exception {
        // Arrange
        Article updatedArticle = new Article(1L, "Updated Title", "Updated Content");
        when(articleService.updateArticle(eq(1L), any(Article.class))).thenReturn(updatedArticle);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/articles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedArticle)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Updated Content"));
    }

    @Test
    void deleteArticle() throws Exception {
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/articles/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Assert
        verify(articleService, times(1)).deleteArticle(1L);
    }
}
