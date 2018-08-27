package com.sce.data.gaia.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class HttpUtils {

    /**
     * 获取真实客户端ip
     *
     * @param request
     * @return 客户端ip
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;

    }

    /**
     * 获取服务器ip
     * 
     * @return 服务器ip
     * @throws SocketException
     */
    public static String getServerIp() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface ni = networkInterfaces.nextElement();
            Enumeration<InetAddress> nias = ni.getInetAddresses();
            while (nias.hasMoreElements()) {
                InetAddress ia = nias.nextElement();
                if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress()
                        && ia instanceof Inet4Address) {
                    return ia.getHostAddress();
                }
            }
        }
        return "127.0.0.1";
    }

}
