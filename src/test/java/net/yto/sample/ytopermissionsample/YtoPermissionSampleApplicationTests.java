package net.yto.sample.ytopermissionsample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetAddress;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YtoPermissionSampleApplicationTests {

	@Test
	public void contextLoads() throws Exception{
		InetAddress addr = InetAddress.getLocalHost();
		String ip = addr.getHostAddress();
		String hostName = addr.getHostName();
		System.out.println("ip is : " + ip);
		System.out.println("hostname is : " + hostName);
	}

}
