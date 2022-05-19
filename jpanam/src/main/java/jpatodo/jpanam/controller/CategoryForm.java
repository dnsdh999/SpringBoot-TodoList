package jpatodo.jpanam.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CategoryForm {
    @NotEmpty(message = "카테고리명을 확인해주세요.")
    private String categoryName;
}