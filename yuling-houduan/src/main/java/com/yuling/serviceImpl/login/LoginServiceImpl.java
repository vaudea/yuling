package com.yuling.serviceImpl.login;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.yuling.common.JwtTokenUtils;
import com.yuling.common.RedisUtil;
import com.yuling.common.encryption.AESDecryption;
import com.yuling.entity.Login;
import com.yuling.exception.CustomException;
import com.yuling.mapper.login.LoginMapper;
import com.yuling.services.login.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AESDecryption aesDecryption;

    @Override
    public Login loginEmployee(Login login, String key) {
        //进行非空验证
        Login user = loginMapper.loginEmployee(login);
        System.out.println("user: "+user);
        String job = login.getUsername();
        if (!login.getUsername().equals(user.getJobNumber())) {
            job = user.getJobNumber().toString();
        }
        String password = aesDecryption.decrypt(login.getIv(), job, login.getPassword());
        if (user != null) {
            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new CustomException("密码错误，请重新输入");
            }
        } else {
            throw new CustomException("不存在该账户或已被停用");
        }

        String cachedCaptcha = redisUtil.getUtil("captcha:" + key);

        if (!login.getCode().toLowerCase().equals(cachedCaptcha)) {
            redisUtil.remove("captcha:" + key);
            throw new CustomException("验证码错误");
        }
        redisUtil.remove("captcha:" + key);
        user.init();
        System.out.println("user: "+user.getPpd());
        //生成token发送到前台
        String token = JwtTokenUtils.getToken(user.getJobNumber().toString(), user.getPpd());
        user.setToken(token);
        return user;
    }

    @Override
    public void resetPassword(Login login) {
        Login user = loginMapper.selectEmployeeByJobNumber(login.getJobNumber());

        if (user == null) {
            throw new CustomException("不存在该账户");
        }
        System.out.println("IV   :" + login.getIv());
        System.out.println("getNewPsd: "+ login.getNewPsd());
        String newPassword = aesDecryption.decrypt(login.getIv(), String.valueOf(login.getJobNumber()), login.getNewPsd());
        System.out.println("gConfirmPsd: "+ login.getConfirmPsd());
        String oldPassword = aesDecryption.decrypt(login.getIv(), String.valueOf(login.getJobNumber()), login.getPassword());
        // 检查原密码是否正确
        System.out.println("oldPassword:" + oldPassword);
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new CustomException("原密码错误");
        }

        // 检查新密码是否与旧密码相同
        if (newPassword.equals(oldPassword)) {
            throw new CustomException("新密码不能与旧密码相同");
        }
        System.out.println("oldPassword:" + oldPassword);
        System.out.println("newPassword:" + newPassword);
        // 如果都通过检查，则更新密码
        System.out.println("newPassword:" + newPassword);
        login.setNewPsd(BCrypt.hashpw(newPassword, BCrypt.gensalt(10)));
        loginMapper.resetPassword(login);
    }


    @Override
    public Login selectEmployeeByJobNumber(Long jobNumber) {
        return loginMapper.selectEmployeeByJobNumber(jobNumber);
    }
}
