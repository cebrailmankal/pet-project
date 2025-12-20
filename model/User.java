package com.petsystem.pet_project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "USERS")
public class User {

    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_ID")
    private Long id;

    @Column(name = "User_Name", nullable = false)
    private String name;

    @Column(name = "User_Mail", unique = true, nullable = false)
    private String email;

    @Column(name = "User_Password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "User_Role", nullable = false)
    private Role role;


    public User(){

    }

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }


}
