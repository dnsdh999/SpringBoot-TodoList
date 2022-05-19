package jpatodo.jpanam.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_num")
    private Long categoryNum;

    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Todo> cateTodo = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member creator;

    private String categoryState = "Y";

    public void deleteCategory() {
        this.categoryState = "D";
    }
}
