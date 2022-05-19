package jpatodo.jpanam.controller;

import jpatodo.jpanam.domain.Member;
import jpatodo.jpanam.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    //회원가입 페이지 이동
    @GetMapping("/members/join")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    //회원가입
    @PostMapping("/members/join")
    public String create(@Valid MemberForm form, BindingResult result){

        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        Member member = new Member();
        member.setEmail(form.getEmail());
        member.setNickName(form.getNickName());
        member.setPwd(form.getPassword());
        memberService.join(member);
        return "redirect:/";
    }

    //로그인 창이동
    @GetMapping("/members/login")
    public String loginForm(Model model){
        model.addAttribute("loginForm", new LoginForm());
        return "members/loginForm";
    }

    //로그인
    @PostMapping("/members/login")
    public String login(@Valid LoginForm form, BindingResult result, HttpServletRequest request){
        if(result.hasErrors()){
            return "members/loginForm";
        }

        List<Member> member = memberService.verifyAccount(form.getEmail(),form.getPassword());

        HttpSession session = request.getSession();
        session.setAttribute("loginMember", member.get(0));

        return "redirect:/";
    }

    //로그인 했는지 여부 체크
    @PostMapping("/members/loginCheck")
    public String loginCheck(Model model, HttpServletRequest request){
        
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");
        System.out.println(loginMember);
        if(loginMember == null){
            model.addAttribute("msg","로그인 되지 않음");
        }else{
            model.addAttribute("msg","로그인 됨");
        }

        return "../static/index :: #resultDiv";
    }

    //로그아웃
    @GetMapping("members/logOut")
    public String logOut(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("loginMember");

        return "redirect:/";
    }

}
