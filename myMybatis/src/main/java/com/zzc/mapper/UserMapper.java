package com.zzc.mapper;

import com.zzc.entity.User;

import java.util.List;


/**
 * @author zzc
 * @create 2020-02-27 12:44
 */
public interface UserMapper {

    User selectById(Long id);

    List<User> selectAll();
}
