package com.qq.book.bookquestionanswerserver.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 评论的UUID生成器
 * Created by boxerli on 14-1-21.
 */
public class UUIDCreator {

    public static HashSet<String> jh = new HashSet<String>();

    public static int ip = 0;

    private static AtomicInteger count = new AtomicInteger();

    static {
        List<Integer> ips = getLocalIPs();
        ip = ips.size() > 0 ? ips.get(0) : 0;
    }

    // 主键生成
    public static String getKey() {
        try {
            //ip加上一个偏移量，防止被猜测IP
            String ipstr = format(ip + 12345);
            String timestr = format((int) (System.currentTimeMillis() / 1000));
            //计数器自动回0.默认一秒钟不超过100w条新增数据
            count.compareAndSet(1000000, 0);
            String instr = format(count.getAndIncrement());
            return timestr + instr + ipstr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加2位的混淆随机数
     *
     * @param intval
     * @return
     */
    protected static String format(int intval) {
        String formatted = Integer.toHexString(intval);
        StringBuffer buf = getRandomStringBuffer(10, 0);
        buf.replace(10 - formatted.length(), 10, formatted);
        return buf.toString();
    }

    public static int iptoInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }

    private static StringBuffer getRandomStringBuffer(int size, int loop) {
        if (loop > 5) {
            return null;
        }
        try {
            if (size <= 10) {
                return new StringBuffer(String.valueOf(Math.random()).substring(2, 2 + size));
            } else {
                StringBuffer sb = new StringBuffer(String.valueOf(Math.random()).substring(2, 12));
                sb.append(String.valueOf(Math.random()).substring(2, 2 + size - 10));
                return sb;
            }
        } catch (Exception e) {
            return getRandomStringBuffer(size, loop + 1);
        }
    }


    /*
     * 得到本机ip，不包括127.0.0.1
     */
    public static List<Integer> getLocalIPs() {
        try {
            List<Integer> ipList = new ArrayList<Integer>();
            Enumeration<NetworkInterface> e1 = NetworkInterface.getNetworkInterfaces();
            while (e1.hasMoreElements()) {
                NetworkInterface ni = e1.nextElement();

                //获取mac地址
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    //下面代码是把mac地址拼装成String
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        if (i != 0) {
                            sb.append("-");
                        }
                        //mac[i] & 0xFF 是为了把byte转化为正整数
                        String s = Integer.toHexString(mac[i] & 0xFF);
                        sb.append(s.length() == 1 ? 0 + s : s);
                    }
                    System.out.println(sb.toString());
                }

                Enumeration<InetAddress> e2 = ni.getInetAddresses();
                while (e2.hasMoreElements()) {
                    InetAddress ia = e2.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue; //忽略IPv6 address
                    }
                    byte[] ip = InetAddress.getLocalHost().getAddress();
                    ipList.add(iptoInt(ip));
                }
            }
            return ipList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get local ip!", e);
        }
    }

    public static void main(String[] args) {
        int now = (int)(System.currentTimeMillis()/1000);
        System.out.println(now);
        String key = getKey();
        String time = key.substring(2,10);
        long t = Long.parseLong(time,16);
        System.out.println(t);
    }


}
