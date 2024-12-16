package redburning.github.io.npms.service;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import redburning.github.io.npms.Application;

@Service
public class DataDownloadService {

	private static final Logger logger = LoggerFactory.getLogger(DataDownloadService.class);
	
	private static final String MOLFILE_DIR = Application.home() + File.separator + "assets/mol/";
	
	private static final String STRUCTURE_2D_DIR = Application.home() + File.separator + "assets/img/";
	
	public ResponseEntity<Resource> downloadMolFile(String fileName) {
		return download(MOLFILE_DIR, fileName);
    }
	
	public ResponseEntity<Resource> download2dStructure(String fileName) {
		return download(STRUCTURE_2D_DIR, fileName);
    }
	
	private ResponseEntity<Resource> download(String directory, String fileName) {
		try {
			// 创建文件资源
	        Resource resource = new FileSystemResource(directory + fileName);
	        // 检查文件是否存在
	        if (!resource.exists()) {
	        	logger.error("File {} not found", fileName);
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }
	        // 获取文件
	        File file = resource.getFile();
	        // 设置响应头
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
	        // 返回响应实体
	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentLength(file.length())
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
}
