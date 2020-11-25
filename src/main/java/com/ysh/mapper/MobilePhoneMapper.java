package com.ysh.mapper;

import com.ysh.model.Role;
import com.ysh.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MobilePhoneMapper {

    @Select("select id, userName, password, email, mobilePhone, createDateTime, enabled, locked from user where mobilePhone=#{mobilePhone}")
    User loadUserByUserName(String mobilePhone);

    @Select("select * from role r,user_role ur where r.id=ur.roleID and ur.userID=#{userID}")
    List<Role> getUserRolesByUserId(int id);
    @Select("select count(*) from user where mobilePhone=#{mobilePhone}")
    int isExistByMobilePhone(String mobilePhone);
}
