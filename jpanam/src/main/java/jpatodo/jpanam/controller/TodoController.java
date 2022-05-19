package jpatodo.jpanam.controller;

import jpatodo.jpanam.domain.Category;
import jpatodo.jpanam.domain.Member;
import jpatodo.jpanam.domain.Todo;
import jpatodo.jpanam.service.CategoryService;
import jpatodo.jpanam.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;
    private final CategoryService categoryService;


    //리스트보여주기
    @GetMapping("/todo/list")
    public String todoList(Model model, HttpServletRequest request,
                           @RequestParam(required = false) Long cateNum,
                           @RequestParam(required = false) String searchText,
                           @RequestParam(required = false) String update,
                           @PageableDefault Pageable pageable){
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");
        if(loginMember == null){
            return "error/loginError";
        }

        List<Todo> todos = null;
        String text = null;
        if(searchText == null){text = "";}else{text = searchText;}

        if(cateNum == null){    //전체목록
            todos = todoService.findAllTodo(loginMember.getId(),text);
        }else if(cateNum == 0L){    //카테고리 없는 것
            todos = todoService.findNullTodo(loginMember.getId(),text);
        }else{                  //카테고리 있는 것
            todos = todoService.findByCategory(cateNum, loginMember.getId(), text);
        }

        List<Category> categories = categoryService.findAll(loginMember.getId());
        model.addAttribute("todos", todos);
        model.addAttribute("categories", categories);
        model.addAttribute("cateNum", cateNum);
        model.addAttribute("searchText", text);
        if(update != null) {
            model.addAttribute("isUpdate", update);
        }
        return "todo/todoList";
    }

    //리스트 추가하기 창 이동
    @GetMapping("/todo/add")
    public String createTodoForm(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");
        if(loginMember == null){
            return "error/loginError";
        }

        List<Category> categories = categoryService.findAll(loginMember.getId());


        model.addAttribute("categories", categories);
        model.addAttribute("todoForm", new AddTodoForm());

        return "todo/addTodo";
    }

    //리스트 추가하기
    @PostMapping("/todo/add")
    public String createTodo(@Valid AddTodoForm form, BindingResult result, HttpServletRequest request){
        if(result.hasErrors()) {
            return "todo/addTodo";
        }
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");
        if(loginMember == null){
            return "error/loginError";
        }

        System.out.println(" 선택한 카테고리 넘버 : " + form.getCategoryNum());
        Category formCategory = categoryService.findOne(Long.parseLong(form.getCategoryNum()));
        System.out.println("시간 양식 : " + form.getTodoTime());

        String str = null;

        if(form.getTodoTime().equals("")) {
            str = form.getTodoDate() + " 00:00:00.000";
        }else{
            str = form.getTodoDate() + " " + form.getTodoTime() + ":00.000";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        LocalDateTime now = LocalDateTime.now();

        if(dateTime.isBefore(now)){
            throw new IllegalStateException("현재보다 이후의 날짜를 설정해야합니다.");
        }

        Todo todo = new Todo();
        todo.setTodoCon(form.getTodoCon());
        todo.setWriter(loginMember);
        todo.setCategory(formCategory);
        todo.setTodoState("N");
        todo.setTodoDate(dateTime);
        todoService.addTodo(todo);
        return "redirect:/todo/list";
    }

    //수행완료처리
    @GetMapping("/todo/conduct")
    public String conductTodo(@RequestParam("todoNum") Long todoNum,
                              @RequestParam(required = false) Long cateNum,
                              @RequestParam(required = false) String searchText,
                              Model model, HttpServletRequest request) throws UnsupportedEncodingException {

        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");
        if(loginMember == null){
            return "error/loginError";
        }

        String text = null;
        String encodedParam = "";
        if(searchText == null){text = "";}else{text = searchText; encodedParam = URLEncoder.encode(text, "UTF-8");}

        todoService.conductTodo(todoNum);

        if(cateNum == null){
            return  "redirect:/todo/list?searchText=" + encodedParam;
        }
        else{
            return  "redirect:/todo/list?cateNum=" + cateNum + "&searchText=" + encodedParam;
        }
    }

    //모두  수행완료
    @GetMapping("/todo/conductAll")
    public String conductAll(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");
        if(loginMember == null){
            return "error/loginError";
        }

        todoService.conductAll(loginMember);
        return "redirect:/todo/list";
    }


    //투두 삭제 처리
    @GetMapping("/todo/delete")
    public String deleteTodo(@RequestParam("todoNum") Long todoNum,
                              @RequestParam(required = false) Long cateNum,
                             @RequestParam(required = false) String searchText,
                              Model model, HttpServletRequest request) throws UnsupportedEncodingException {

        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");
        if(loginMember == null){
            return "error/loginError";
        }

        String text = null;
        String encodedParam = "";
        if(searchText == null){text = "";}else{text = searchText; encodedParam = URLEncoder.encode(text, "UTF-8");}


        todoService.deleteTodo(todoNum);

        if(cateNum == null){
            return  "redirect:/todo/list?searchText=" + encodedParam;
        }
        else{
            return  "redirect:/todo/list?cateNum=" + cateNum + "&searchText=" + encodedParam;
        }
    }

    @GetMapping("/todo/update")
    public String updateTodoForm(@RequestParam("todoNum") Long todoNum,
                             Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");
        if(loginMember == null){
            return "error/loginError";
        }

        Todo todo = todoService.findOne(todoNum);
        List<Category> categories = categoryService.findAll(loginMember.getId());

        String formatDate = todo.getTodoDate().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        String year = formatDate.substring(0,4);
        String month = formatDate.substring(4,6);
        String day = formatDate.substring(6,8);

        String fullTime = formatDate.substring(8,10) + ":" + formatDate.substring(10,12);



        String fullDate = year + "-" + month + "-" + day;


        AddTodoForm addTodoForm = new AddTodoForm();
        addTodoForm.setTodoCon(todo.getTodoCon());
        addTodoForm.setTodoDate(fullDate);
        addTodoForm.setTodoTime(fullTime);

        if(todo.getCategory() != null) {
            addTodoForm.setCategoryNum(todo.getCategory().getCategoryNum() + "");
        }
        model.addAttribute("categories", categories);
        model.addAttribute("todoForm", addTodoForm);
        model.addAttribute("todoNum", todo.getTodoNum());

        return "todo/updateTodo";
    }

    //리스트 수정하기
    @PostMapping("/todo/update")
    public String updateTodo(@Valid AddTodoForm form, BindingResult result, HttpServletRequest request, @RequestParam("todoNum") Long todoNum, @RequestParam("isUpdate") String isUpdate) {

        if (result.hasErrors()) {
            return "todo/updateTodo";
        }
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "error/loginError";
        }


            Category formCategory = categoryService.findOne(Long.parseLong(form.getCategoryNum()));

        String str = null;

        if(form.getTodoTime().equals("")) {
            str = form.getTodoDate() + " 00:00:00.000";
        }else{
            str = form.getTodoDate() + " " + form.getTodoTime() + ":00.000";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        LocalDateTime now = LocalDateTime.now();

        if(dateTime.isBefore(now)){
            throw new IllegalStateException("현재보다 이후의 날짜를 설정해야합니다.");
        }

        todoService.updateTodo(todoNum, form.getTodoCon(), dateTime, formCategory);

        return  "redirect:/todo/list?update=" + isUpdate;
    }
    }
