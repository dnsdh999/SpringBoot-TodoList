package jpatodo.jpanam.controller;

import jpatodo.jpanam.domain.Category;
import jpatodo.jpanam.domain.Member;
import jpatodo.jpanam.repository.CategoryRepository;
import jpatodo.jpanam.service.CategoryService;
import jpatodo.jpanam.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final TodoService todoService;

    @GetMapping("/category/list")
    public String categoryList(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");
        if(loginMember == null){
            return "error/loginError";
        }
        List<Category> categoryList = categoryService.findAll(loginMember.getId());
        model.addAttribute("categoryList", categoryList);
        return "category/categoryList";
    }

    @GetMapping("/category/delete")
    public String categoryDelete(@RequestParam("categoryNum") Long categoryNum, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");
        if(loginMember == null){
            return "error/loginError";
        }
        Category category = categoryService.findOne(categoryNum);
        todoService.deleteByCategory(category, loginMember.getId());
        categoryService.deleteCategory(categoryNum);

        return "redirect:/category/list";
    }

    @GetMapping("/category/add")
    public String addCategoryForm(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");
        if(loginMember == null){
            return "error/loginError";
        }
        model.addAttribute("categoryForm", new CategoryForm());
        return "category/addCategory";
    }

    @PostMapping("/category/add")
    public String addCategory(@Valid CategoryForm form, BindingResult result, HttpServletRequest request){
        if(result.hasErrors()){
            return "category/addCategory";
        }
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");

        if(loginMember == null){
            return "error/loginError";
        }

        Category category = new Category();
        category.setCategoryName(form.getCategoryName());
        category.setCreator(loginMember);
        categoryService.addCategory(category, loginMember.getId());
        return "redirect:/todo/add";
    }
}
