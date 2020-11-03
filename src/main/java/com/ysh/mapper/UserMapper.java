package com.ysh.mapper;

import com.ysh.model.Role;
import com.ysh.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select id,userName,password,email,createDateTime,enabled,locked from user where userName=#{userName}")
    User loadUserByUserName(String userName);

    @Select("select * from role r,user_role ur where r.id=ur.roleID and ur.userID=#{userID}")
    List<Role> getUserRolesByUserId(int id);

    @Insert("insert into user(userName, password, email, createDateTime, enabled, locked) values(#{userName},#{password},#{email},#{createDateTime},#{enabled},#{locked})")
    void save(User user);

    @Insert("insert into user_role(userID, roleID) values(#{id},2)")
    void setRole(int id);
}
