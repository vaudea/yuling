package com.yuling.entity;


import jakarta.annotation.PostConstruct;
import lombok.Data;

@Data
public class Login  extends Employee{
    private String username;
//    验证码
    private String code;
    private String token;
    private String newPsd;
    private String confirmPsd;
    private String encryption;
    // 密码 + 职位 + 部门
    private String ppd;
    private String iv;

    public void init(){
        this.setPpd(this.getPassword() + this.getPositionId() + this.getDepartmentId() + this.getFatherId());
    }

    @Override
    public String toString() {
        return "Login{" +
                "username='" + username + '\'' +
                ", code='" + code + '\'' +
                ", jobNumber='" + this.getJobNumber() + '\'' +
                ", bankCard='" + this.getBankCard() + '\'' +
                ", email='" + this.getEmail() + '\'' +
                ", gender='" + this.getGender() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", password='" + this.getPassword() + '\'' +
                ", phone='" + this.getPhone() + '\'' +
                ", positionId='" + this.getPositionId() + '\'' +
                ", departmentId='" + this.getDepartmentId() + '\'' +
                '}';
    }
}
