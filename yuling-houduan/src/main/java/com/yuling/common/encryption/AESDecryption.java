package com.yuling.common.encryption;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.yuling.exception.CustomException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Base64;
import java.util.HexFormat;

@Component
public class AESDecryption {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public String decrypt(String iv, String jobNumber, String password) {
        try {
            // 生成密钥，类似前端方式
            String combined = jobNumber + iv + "veauwd177" + "030920";
            String hashed = DigestUtil.sha256Hex(combined);
            String truncatedHash = hashed.substring(0, 64);
            // 使用整个哈希值的十六进制字符串作为密钥
            byte[] key = HexUtil.decodeHex(truncatedHash);
            // 将十六进制字符串转换为字节数组
            IvParameterSpec ivParameterSpec = new IvParameterSpec(HexFormat.of().parseHex(iv));
            // 初始化解密器
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            // 解密数据
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(password));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            // 处理解密错误，返回空字符串或者特定的错误信息
            throw new CustomException("解密出错，请检查你的iv和密钥");
        }
    }
}