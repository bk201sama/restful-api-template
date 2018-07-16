package com.sce.data.gaia.dao.domain;


import com.sce.data.gaia.constant.ColumnNames;
import com.sce.data.gaia.constant.TableNames;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * system user table
 * @author bk201
 */
@Entity
@Table(name = TableNames.CUSTOM_SYS_USERS)
@Data
public class CustomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(table = TableNames.CUSTOM_SYS_USERS,name = ColumnNames.USER_NAME,nullable = false,unique = true)
    private String userName;
    @Column(table = TableNames.CUSTOM_SYS_USERS,name = ColumnNames.PASSWORD,nullable = false)
    private String password;
    @Column(table = TableNames.CUSTOM_SYS_USERS,name = ColumnNames.ROLE_NAMES,nullable = false)
    private String roleNames;
}