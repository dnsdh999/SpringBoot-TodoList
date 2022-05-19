package jpatodo.jpanam.controller;

import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Local;

import javax.persistence.SequenceGenerators;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
public class AddTodoForm {
    @NotEmpty(message = "제목을 입력해주세요.")
    private String todoCon;

    @NotEmpty(message = "마감일을 입력하세요")
    private String todoDate;

    private String todoTime;

    private String categoryNum;
}
