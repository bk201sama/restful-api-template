package com.sce.data.gaia.dao.domain


import com.sce.data.gaia.constant.ColumnNames
import com.sce.data.gaia.constant.TableNames

import javax.persistence.*

/**
 * system user table
 * @author bk201
 */
@Entity
@Table(name = TableNames.CUSTOM_SYS_USERS)
class CustomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    @Column(table = TableNames.CUSTOM_SYS_USERS, name = ColumnNames.USER_NAME, nullable = false, unique = true)
    var userName: String? = null
    @Column(table = TableNames.CUSTOM_SYS_USERS, name = ColumnNames.PASSWORD, nullable = false)
    var password: String? = null
    @Column(table = TableNames.CUSTOM_SYS_USERS, name = ColumnNames.ROLE_NAMES, nullable = false)
    var roleNames: String? = null
}