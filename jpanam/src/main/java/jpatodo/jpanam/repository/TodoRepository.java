package jpatodo.jpanam.repository;

import jpatodo.jpanam.domain.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TodoRepository {
    private final EntityManager em;

    public void save(Todo todo){ em.persist(todo); }

    public Todo findOne(Long id){
        return em.find(Todo.class, id);
    }

    public List<Todo> findNoListAll(Long memberNo, String searchText){
        return em.createQuery("select t from Todo t where t.todoState = 'N' and t.writer.id = :memberNo and t.todoCon like '%'|| :searchText ||'%' order by t.todoDate", Todo.class)
                .setParameter("memberNo", memberNo)
                .setParameter("searchText",searchText)
                .getResultList();
    }

    public List<Todo> findYesListAll(Long memberNo, String searchText){

        return em.createQuery("select t from Todo t where t.todoState = 'Y' and t.writer.id = :memberNo and t.todoCon like '%'|| :searchText ||'%' order by t.todoDate desc", Todo.class)
                .setParameter("memberNo", memberNo)
                .setParameter("searchText",searchText)
                .getResultList();
    }



    public List<Todo> findByWord(String word){
        return em.createQuery("select t from Todo t where t.todo_con = :word", Todo.class)
                .setParameter("word", word)
                .getResultList();
    }

    public List<Todo> findByCategoryNo(Long categoryNo, Long id, String searchText){
        return em.createQuery("select t from Todo t where t.todoState = 'N' and t.category.categoryNum = :categoryNo and t.todoCon like '%'|| :searchText ||'%' and t.writer.id = :id order by t.todoDate", Todo.class)
                .setParameter("categoryNo", categoryNo)
                .setParameter("searchText", searchText)
                .setParameter("id", id)
                .getResultList();
    }

    public List<Todo> findByCategoryYes(Long categoryNo, Long id, String searchText){
        return em.createQuery("select t from Todo t where t.todoState = 'Y' and t.category.categoryNum = :categoryNo and t.todoCon like '%'|| :searchText ||'%' and t.writer.id = :id order by t.todoDate desc", Todo.class)
                .setParameter("categoryNo", categoryNo)
                .setParameter("searchText", searchText)
                .setParameter("id", id)
                .getResultList();
    }

    public List<Todo> findNullTodoNo(Long id, String searchText) {
        return em.createQuery("select t from Todo t where t.category.categoryNum is null and t.writer.id = :id and t.todoCon like '%'|| :searchText ||'%' and t.todoState = 'N' order by t.todoDate", Todo.class)
                .setParameter("id", id)
                .setParameter("searchText", searchText)
                .getResultList();
    }

    public List<Todo> findNullTodoYes(Long id, String searchText) {
        return em.createQuery("select t from Todo t where t.category.categoryNum is null and t.writer.id = :id and t.todoCon like '%'|| :searchText ||'%' and t.todoState = 'Y' order by t.todoDate desc", Todo.class)
                .setParameter("id", id)
                .setParameter("searchText", searchText)
                .getResultList();
    }

    public List<Todo> findPastList(Long id) {
        return em.createQuery("select t from Todo t where t.writer.id = :id and t.todoDate <= SYSDATE and t.todoState = 'N'", Todo.class)
                .setParameter("id", id).getResultList();
    }
}
