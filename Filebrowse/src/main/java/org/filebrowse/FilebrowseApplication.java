package org.filebrowse;

import java.io.IOException;

import org.filebrowse.service.ResourceListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilebrowseApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(FilebrowseApplication.class, args);
		ResourceListener.addListener("D:/doc_resources");
	}
}
