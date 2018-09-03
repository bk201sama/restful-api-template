package com.sce.data.gaia.dao.domain;


import lombok.Data;

import javax.persistence.*;

/**
 * system user table
 * @author bk201
 */
@Entity
@Table(name = "bi_gaia_users")
@Data
public class CustomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(table = "bi_gaia_users",name = "user_name",nullable = false,unique = true)
    private String userName;
    @Column(table = "bi_gaia_users",name = "password",nullable = false)
    private String password;
    @Column(table = "bi_gaia_users",name = "role_names",nullable = false)
    private String roleNames;
}