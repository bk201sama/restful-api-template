package com.sce.data.gaia.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author bk201
 * eq.
 * {
 * CREATE TABLE `custom_sys_users` (
 *   `id` bigint(20) NOT NULL AUTO_INCREMENT,
 *   `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
 *   `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
 * }
 *
 *
 */
@Entity
@Table(name = "custom_sys_users")
@Data
public class CustomUser {
    @Id
    @GeneratedValue
    private long id;
    private String username;
    private String password;
}