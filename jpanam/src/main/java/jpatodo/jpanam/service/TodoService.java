package jpatodo.jpanam.service;

import jpatodo.jpanam.domain.Category;
import jpatodo.jpanam.domain.Member;
import jpatodo.jpanam.domain.Todo;
import jpatodo.jpanam.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {
    
    private final TodoRepository todoRepository;

    public Todo findOne(Long todoNum){
        return todoRepository.findOne(todoNum);
    }

    //투두 전체조회
    public List<Todo> findAllTodo(Long loginMemberNo, String searchText){


        List<Todo> noList =  todoRepository.findNoListAll(loginMemberNo, searchText);
        List<Todo> yesList =  todoRepository.findYesListAll(loginMemberNo, searchText);

        List<Todo> joined = new ArrayList<>();
        joined.addAll(noList);
        joined.addAll(yesList);

        return joined;
    }

    //투두생성
    @Transactional
    public Long addTodo(Todo todo){
        todoRepository.save(todo);
        return todo.getTodoNum();
    }

    //투두 삭제 (업데이트)
//    @Transactional
//    public Long deleteTodo(Long todoNum){
//        todoRepository.deleteTodo(todoNum);
//        return todoNum;
//    }
    // 특정 단어로 조회
    public List<Todo> findByWord(String word){
        return todoRepository.findByWord(word);
    }

    // 특정 카테고리로 조회
    public List<Todo> findByCategory(Long categoryNo, Long id, String searchText){

        List<Todo> noList =  todoRepository.findByCategoryNo(categoryNo, id, searchText);
        List<Todo> yesList =  todoRepository.findByCategoryYes(categoryNo, id, searchText);

        List<Todo> joined = new ArrayList<>();
        joined.addAll(noList);
        joined.addAll(yesList);

        return joined;
    }

    @Transactional
    public void conductTodo(Long todoNum) {
        Todo todo = todoRepository.findOne(todoNum);
        todo.complete();
    }

    public List<Todo> findNullTodo(Long id, String searchText) {
        List<Todo> noList =  todoRepository.findNullTodoNo(id, searchText);
        List<Todo> yesList =  todoRepository.findNullTodoYes(id, searchText);

        List<Todo> joined = new ArrayList<>();
        joined.addAll(noList);
        joined.addAll(yesList);

        return joined;
    }

    @Transactional
    public void deleteTodo(Long todoNum) {
        Todo todo = todoRepository.findOne(todoNum);
        todo.delete();
    }

    @Transactional
    public void updateTodo(Long todoNum, String todoCon, LocalDateTime dateTime, Category formCategory) {
        Todo todo = todoRepository.findOne(todoNum);
        todo.update(todoCon, dateTime, formCategory);
    }

    @Transactional
    public void conductAll(Member loginMember) {
        List<Todo> todos = todoRepository.findPastList(loginMember.getId());
        for (Todo t : todos){
            t.complete();
        }
    }

    @Transactional
    public void deleteByCategory(Category category, Long id) {
        List<Todo> todos = findByCategory(category.getCategoryNum(), id, "");
        for (Todo t : todos){
            t.delete();
        }
    }
}
