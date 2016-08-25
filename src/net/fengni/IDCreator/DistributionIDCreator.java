 /*
 *�ֲ�ʽID������
 *ID����ɲ��֣���ǰʱ������룩+����IP���һ���ֽ�+�����������кţ�λ�� 34bit+8bit+21bit=63bit����λΪ0��
 *ʹ������������Ļ�����IP���һ���ֽڲ��ܲ�����ͬ�����������ظ���IP
 *�Ľ�������Ϊÿ��ʵ������һ��Ψһ�ı�ţ�����IPת����Ψһ��һ�����
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
                        System.out.println("������IP = " + ip.getHostAddress());
						byte[] ip = ip.getAddress();
						ipList.add(iptoInt(ip));
                    }
                }
            }
        }catch (Exception e){

        }
		return ipList��
    }
	
	public static int iptoInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }
	 
	 
	 
 }
