package redburning.github.io.npms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import redburning.github.io.npms.service.DataDownloadService;

@RestController
@RequestMapping("/download")
public class DataDownloadController {

	@Autowired
	private DataDownloadService fileDownloadService;
	
	@GetMapping("/mol/{fileName}")
    public ResponseEntity<Resource> downloadMolFile(@PathVariable String fileName) {
		return fileDownloadService.downloadMolFile(fileName);
    }
	
	@GetMapping("/structure2d/{fileName}")
    public ResponseEntity<Resource> download2dStructure(@PathVariable String fileName) {
		return fileDownloadService.download2dStructure(fileName);
    }
	
}
