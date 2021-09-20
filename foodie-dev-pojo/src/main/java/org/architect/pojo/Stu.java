package org.architect.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
public class Stu {
    @Id
    private Integer id;

    private String name;

    private Integer age;

    }