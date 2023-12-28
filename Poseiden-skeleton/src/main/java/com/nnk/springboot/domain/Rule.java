package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rule")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @NotEmpty(message = "Name is mandatory")
    @Column(name = "name")
    private String name;

    @NotNull
    @NotEmpty(message = "Description is mandatory")
    @Column(name = "description")
    private String description;

    @NotNull
    @NotEmpty(message = "Json is mandatory")
    @Column(name = "json")
    private String json;

    @NotNull
    @NotEmpty(message = "Template is mandatory")
    @Column(name = "template")
    private String template;

    @NotNull
    @NotEmpty(message = "SQL is mandatory")
    @Column(name = "sql_str")
    private String sqlStr;

    @NotNull
    @NotEmpty(message = "SQL Part is mandatory")
    @Column(name = "sql_part")
    private String sqlPart;

    public Rule(String name, String description, String json, String template, String sqlStr, String sqlPart) {
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;
    }
}
