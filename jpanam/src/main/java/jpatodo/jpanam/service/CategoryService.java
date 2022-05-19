package jpatodo.jpanam.service;

import jpatodo.jpanam.domain.Category;
import jpatodo.jpanam.domain.Member;
import jpatodo.jpanam.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    //카테고리 추가
    @Transactional
    public Long addCategory(Category category, Long id){
        categoryNameCheck(category, id);
        categoryRepository.save(category);
        return category.getCategoryNum();
    }

    private void categoryNameCheck(Category category, Long id){
        List<Category> findCates = categoryRepository.findByCategoryName(category.getCategoryName(), id);
        if(!findCates.isEmpty()){
            throw new IllegalStateException("이미 존재하는 카테고리명입니다.");
        }
    }

    public List<Category> findAll(Long id) {
        return categoryRepository.findAll(id);
    }

    public Category findOne(Long categoryNum) {
        return categoryRepository.findOne(categoryNum);
    }

    @Transactional
    public void deleteCategory(Long categoryNum) {
        Category category = categoryRepository.findOne(categoryNum);
        category.deleteCategory();
    }
}
