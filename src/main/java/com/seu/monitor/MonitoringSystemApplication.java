package com.seu.monitor;

import com.seu.monitor.entity.User;
import com.seu.monitor.utils.MachineUtils;
import com.seu.monitor.utils.UserUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.seu.monitor.socket.*;

@SpringBootApplication
public class MonitoringSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(MonitoringSystemApplication.class, args);
		MachineUtils.createFirstMachine();
		UserUtils.addRootUser();
		StartSocketServer.start();
		//TestSomeFun.test6();

	}
}
