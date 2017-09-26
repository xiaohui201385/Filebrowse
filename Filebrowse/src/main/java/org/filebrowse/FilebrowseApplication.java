package org.filebrowse;

import java.io.IOException;

import org.filebrowse.service.ResourceListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class FilebrowseApplication {

	public static void main(String[] args) throws IOException {
	    ResourceListener.addListener("C:/Program Files/Microsoft Office Web Apps/OpenFromUrlWeb/docview");
		SpringApplication.run(FilebrowseApplication.class, args);
	}
}
