package com.sykj.mapper;

import com.sykj.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select id,name from student")
    List<User> findAllUser();
}
