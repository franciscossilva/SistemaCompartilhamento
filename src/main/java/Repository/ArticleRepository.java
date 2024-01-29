package Repository;


import Model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    // ... métodos de consulta personalizados, se necessário
}
