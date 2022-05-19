package jpatodo.jpanam.service;

import jpatodo.jpanam.domain.Member;
import jpatodo.jpanam.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //회원가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        String encodedPassword = passwordEncoder.encode(member.getPwd());
        member.setPwd(encodedPassword);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member){
        List<Member> findMembers = memberRepository.findByEmail(member.getEmail());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }


    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    public List<Member> verifyAccount(String email, String password) {
        List<Member> members = memberRepository.findByEmail(email);
        if(!(!members.isEmpty() && passwordEncoder.matches(password, members.get(0).getPwd()))){
            throw new IllegalStateException("아이디나 비밀번호가 일치하지 않음");
        }else{
            return members;
        }
    }
}
