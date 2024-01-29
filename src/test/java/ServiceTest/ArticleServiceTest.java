package ServiceTest;


import Model.Article;
import Repository.ArticleRepository;
import Service.ArticleService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllArticles() {
        // Arrange
        when(articleRepository.findAll()).thenReturn(Arrays.asList(
                new Article(1L, "Title 1", "Content 1"),
                new Article(2L, "Title 2", "Content 2")
        ));

        // Act
        List<Article> articles = articleService.getAllArticles();

        // Assert
        assertEquals(2, articles.size());
    }

    @Test
    void getArticleById() {
        // Arrange
        when(articleRepository.findById(1L)).thenReturn(Optional.of(new Article(1L, "Title 1", "Content 1")));

        // Act
        Article article = articleService.getArticleById(1L);

        // Assert
        assertEquals("Title 1", article.getTitle());
        assertEquals("Content 1", article.getContent());
    }

    @Test
    void createArticle() {
        // Arrange
        Article newArticle = new Article(null, "New Title", "New Content");

        // Act
        articleService.createArticle(newArticle);

        // Assert
        verify(articleRepository, times(1)).save(newArticle);
    }

    @Test
    void updateArticle() {
        // Arrange
        Article existingArticle = new Article(1L, "Title 1", "Content 1");
        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));

        Article updatedArticle = new Article(1L, "Updated Title", "Updated Content");

        // Act
        articleService.updateArticle(1L, updatedArticle);

        // Assert
        assertEquals("Updated Title", existingArticle.getTitle());
        assertEquals("Updated Content", existingArticle.getContent());
        verify(articleRepository, times(1)).save(existingArticle);
    }

    @Test
    void deleteArticle() {
        // Arrange & Act
        articleService.deleteArticle(1L);

        // Assert
        verify(articleRepository, times(1)).deleteById(1L);
    }
}
