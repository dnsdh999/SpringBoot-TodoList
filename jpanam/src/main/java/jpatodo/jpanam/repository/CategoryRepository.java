package jpatodo.jpanam.repository;

import jpatodo.jpanam.domain.Category;
import jpatodo.jpanam.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {
    private final EntityManager em;

    public void save(Category category){em.persist(category);}

    public List<Category> findAll(Long id){
        return em.createQuery("select c from Category c where c.creator.id = ?1 and c.categoryState = 'Y'", Category.class)
                .setParameter(1, id)
                .getResultList();
    }

    public Category findOne(Long categoryNum) {
        return em.find(Category.class, categoryNum);
    }

    public List<Category> findByCategoryName(String categoryName, Long id) {
        return em.createQuery("select c from Category c where c.categoryName = :categoryName and c.creator.id = :id and c.categoryState = 'Y'", Category.class)
                .setParameter("categoryName", categoryName)
                .setParameter("id", id)
                .getResultList();
    }


}
