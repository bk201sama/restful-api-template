package com.sce.data.gaia.entity;


import com.sce.data.gaia.constant.TableNames;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * system user table
 * @author bk201
 */
@Entity
@Table(name = TableNames.CUSTOM_SYS_USERS,indexes = {
        @Index(name="idx_username", columnList="user_name")
})
@Data
public class CustomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(table = TableNames.CUSTOM_SYS_USERS,name = "user_name",nullable = false,unique = true)
    private String userName;
    @Column(table = TableNames.CUSTOM_SYS_USERS,name = "password",nullable = false)
    private String password;
    @Column(table = TableNames.CUSTOM_SYS_USERS,name = "role_names",nullable = false)
    private String roleNames;
}