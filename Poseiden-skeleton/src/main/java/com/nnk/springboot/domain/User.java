package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "Username is mandatory")
    @Column(name = "username")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Column(name = "password")
    private String password;

    @NotBlank(message = "FullName is mandatory")
    @Column(name = "fullname")
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    @Column(name = "role")
    private String role;

    public User(String username, String fullname, String password, String role) {
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.role = password;
    }

}
