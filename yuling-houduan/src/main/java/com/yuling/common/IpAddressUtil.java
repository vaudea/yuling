package com.yuling.common;

import cn.hutool.core.util.StrUtil;
import com.yuling.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * IP地址Util
 */
@Slf4j
public class IpAddressUtil {


    // 修改路径为类路径资源
    public static final String XDB_PATH = "/ip/ip2region.xdb";

    /**
     * 缓存整个 xdb 数据，对用户ip地址进行转换
     * 注：并发使用时，用整个 xdb 数据缓存创建的查询对象可以安全的用于并发，也就是你可以把这个 searcher 对象做成全局对象去跨线程访问。
     */
    public static String getCityInfoByMemorySearch(String ip) {
        if (StrUtil.isNotEmpty(ip)) {
            try {
                // 从类路径加载整个 xdb 到内存
                InputStream inputStream = IpAddressUtil.class.getResourceAsStream(XDB_PATH);
                if (inputStream == null) {
                    throw new RuntimeException("未能找到资源文件: " + XDB_PATH);
                }
                byte[] cBuff = inputStream.readAllBytes();
                Searcher searcher = Searcher.newWithBuffer(cBuff);
                long sTime = System.nanoTime();
                String region = searcher.search(ip);
                long cost = TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - sTime);

                // 日志记录查询信息
                log.info("{地区: {}, IO操作数: {}, 耗时: {} μs}", region, searcher.getIOCount(), cost);

                // 检查是否内网IP
                if (region.contains("内网IP")) {
                    return "内网IP";
                } else {
                    // 处理返回的字符串
                    String[] parts = region.split("\\|");
                    if (parts.length > 0 && parts[0].equals("中国")) {
                        // 如果是中国，返回“省份 城市”
                        String province = parts.length > 2 ? parts[2] : "未知省份";
                        String city = parts.length > 3 ? parts[3] : "未知城市";
                        return province + " " + city;
                    } else if (parts.length > 0) {
                        // 如果是国外，仅返回国家名称
                        return parts[0];
                    } else {
                        // 如果分割后的部分不符合预期，记录错误并返回默认值
                        log.error("无法解析地区信息：{}", region);
                        return "地区信息格式错误";
                    }
                }
            } catch (Exception e) {
                log.error("获取IP地址异常：{} ", e.getMessage());
                throw new CustomException("获取IP地址异常");
            }
        }
        return "未知";
    }


    /**
     * 获取请求的 IP 地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip)) {
                // 根据网卡取本机配置的 IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("获取IP地址异常，{}", e.getMessage());
                }
                if (inet != null) {
                    ip = inet.getHostAddress();
                }
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        // 本机访问
        if ("localhost".equalsIgnoreCase(ip) || "127.0.0.1".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
            // 根据网卡取本机配置的IP
            InetAddress inet;
            try {
                inet = InetAddress.getLocalHost();
                ip = inet.getHostAddress();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("获取本机IP地址异常，{}", e.getMessage());
            }
        }
        // 如果查找不到 IP,可以返回 127.0.0.1，可以做一定的处理，但是这里不考虑
        // if (ip == null) {
        //     return "127.0.0.1";
        // }
        return ip;
    }

    /**
     * 获取IP地址
     */
    public static String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ipAddress = headers.getFirst("X-Forwarded-For");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
            if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                try {
                    InetAddress inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (Exception e) {
                    log.error("获取IP地址异常,{}", e.getMessage());
                }
            }
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP，多个IP按照','分割
        if (ipAddress != null && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.split(",")[0];
        }
        return ipAddress;
    }

    /**
     * 获取mac地址
     */
    public static String getMacIpAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            byte[] macAddressBytes = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
            // 将mac地址拼装成String
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < macAddressBytes.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // mac[i] & 0xFF 是为了把byte转化为正整数
                String s = Integer.toHexString(macAddressBytes[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            return sb.toString().trim().toUpperCase();
        } catch (Exception e) {
            log.error("Mac获取IP地址异常,{}", e.getMessage());
        }
        return "";
    }

}