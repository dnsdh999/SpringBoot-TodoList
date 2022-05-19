package jpatodo.jpanam.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String pwd;

    private String nickName;

    private String email;

    @OneToMany(mappedBy = "writer")
    private List<Todo> todos = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    private List<Category> categories  = new ArrayList<>();
}
