package com.kgc.kmall.manager;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KmallManagerWebApplicationTests {

	@Test
	void contextLoads() {
		try {
			String file = this.getClass().getResource("/tracker.conf").getFile();
			ClientGlobal.init(file);
			TrackerClient trackerClient=new TrackerClient();
			TrackerServer trackerServer=trackerClient.getTrackerServer();
			StorageClient storageClient=new StorageClient(trackerServer,null);
			String orginalFilename="C:\\Users\\39740\\Pictures\\Saved Pictures\\Overload.png";
			String[] upload_file = storageClient.upload_file(orginalFilename, "png", null);
			String path="http://192.168.154.132";
			for (int i = 0; i < upload_file.length; i++) {
				String s = upload_file[i];
				System.out.println("s = " + s);
				path+="/"+s;
			}
			System.out.println(path);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
