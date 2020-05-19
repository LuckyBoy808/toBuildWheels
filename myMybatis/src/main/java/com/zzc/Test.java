package com.zzc;

import com.zzc.entity.User;
import com.zzc.mapper.UserMapper;
import com.zzc.session.SqlSession;
import com.zzc.session.SqlSessionFactory;

import java.util.List;

/**
 * @author zzc
 * @create 2020-05-18 22:31
 */
public class Test {

    public static void main(String[] args) {
        SqlSessionFactory factory = new SqlSessionFactory();
        SqlSession sqlSession = factory.openSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectById(1l);
        System.out.println(user);
    }
}