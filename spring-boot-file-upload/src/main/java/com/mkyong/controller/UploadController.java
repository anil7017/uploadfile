package com.mkyong.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mkyong.service.UploadSeervice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {

	@Autowired
	UploadSeervice seervice;
	
    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "D:\\report\\";

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

        	File dir = new File(UPLOADED_FOLDER);
        	File[] listFiles = dir.listFiles();
    		for(File oldFile : listFiles){
    			System.out.println("Deleting "+file.getName());
    			oldFile.delete();
    		}
        	
    		
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            System.out.println("path  "+UPLOADED_FOLDER+path.getFileName());
            
            System.out.println("file orignal name "+file.getOriginalFilename());
            System.out.println("file content type  "+file.getContentType());
            System.out.println("file name "+file.getName());
           
            String filePath = UPLOADED_FOLDER + file.getOriginalFilename(); 
            
            seervice.readFromExcelFile(filePath);
            
            redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
    
    @GetMapping("/test")
    public String uploadStatustest(){
        return "index";
    }
    
}