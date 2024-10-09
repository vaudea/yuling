package com.yuling.mapper.login;

import com.yuling.entity.Employee;
import com.yuling.entity.Login;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {

    /**
     * 登录验证
     *
     * @param login 包含用户名和密码
     * @return Login 对象，包含用户名和密码
     */
    Login loginEmployee(Login login);

    /**
     * 修改密码
     *
     * @param login 包含电话号码和新密码
     */
    void resetPassword(Login login);

    /**
     * 查询员工管理
     *
     * @param jobNumber 员工管理主键
     * @return 员工管理
     */
    public Login selectEmployeeByJobNumber(Long jobNumber);
}
