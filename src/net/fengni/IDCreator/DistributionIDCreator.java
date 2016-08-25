 /*
 *分布式ID生成器
 *ID的组成部分：当前时间戳（秒）+机器IP最后一个字节+单机自增序列号，位数 34bit+8bit+21bit=63bit。首位为0；
 *使用条件，部署的机器的IP最后一个字节不能不能相同，否则会产生重复的IP
 *改进方法，为每个实例配置一个唯一的编号，或者IP转换成唯一的一个编号
 */
 
 public class DistributionIDCreator{
	 public static int ip = 0;

    private static AtomicInteger count = new AtomicInteger();

    static {
        List<Integer> ips = getLocalIPs();
        ip = ips.size() > 0 ? ips.get(0) : 0;
    }
	
	public static long getId() {
        long serverNo = ip & 0xFF;
        long time = System.currentTimeMillis() / 1000;
        count.compareAndSet(2097152, 0);
        long index = count.getAndIncrement();
        time = time << 29;
        serverNo = serverNo << 21;
        long id = time + serverNo + index;
        //System.out.println("time:" + time + "\nserverNo:" + serverNo + "\nindex=" + index);
        return id;
    }
 

    private static void getLocalIPs(){
		List<Integer> ipList = new ArrayList<Integer>();
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                //System.out.println(netInterface.getName());
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address && ip.isSiteLocalAddress()) {
                        System.out.println("本机的IP = " + ip.getHostAddress());
						byte[] ip = ip.getAddress();
						ipList.add(iptoInt(ip));
                    }
                }
            }
        }catch (Exception e){

        }
		return ipList；
    }
	
	public static int iptoInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }
	 
	 
	 
 }
