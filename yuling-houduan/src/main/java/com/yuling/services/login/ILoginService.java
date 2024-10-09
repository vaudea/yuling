package com.yuling.services.login;

import com.yuling.entity.Login;

public interface ILoginService {

    /**
     * 登录验证
     *
     * @param login 包含用户名和密码
     * @param key
     * @return Login 对象，包含用户名和密码
     */
    Login loginEmployee(Login login, String key);

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
