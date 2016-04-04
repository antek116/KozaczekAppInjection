package example.kozaczekapp.DatabaseConnection;

import java.util.List;

import example.kozaczekapp.KozaczekItems.Article;

/**
 * interface that contains set of priceless methods using to manage database
 */
public interface IDatabaseHelper {

    /**
     * adds new article list
     *
     * @param articleList contains list of all articles
     */
    void addArticleList(List<Article> articleList);

    /**
     * adds new article
     *
     * @param article to be added
     */
    void addArticle(Article article);


    /**
     * gets all articles from database
     *
     * @return list of all articles
     */

    List<Article> getAllArticles();


    /**
     * gets number of articles in database
     *
     * @return articles' count
     */

    int getArticlesCount();


    /**
     * deletes single article
     *
     * @param id specify index of article to be deleted
     */

    void deleteArticle(int id);


}
