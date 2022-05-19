package jpatodo.jpanam.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Todo {

    @Id @GeneratedValue
    @Column(name = "todo_num")
    private Long todoNum;

    private String todoCon;

    private LocalDateTime todoDate;

    private String todoState;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_num")
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    public void complete() {
        this.todoState = "Y";
    }

    public void delete() {
        this.todoState = "D";
    }

    public void update(String todoCon, LocalDateTime dateTime, Category formCategory) {
        this.todoCon = todoCon;
        this.todoDate = dateTime;
        this.category = formCategory;
    }


}
