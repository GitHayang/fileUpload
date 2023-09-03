package com.kopo.uploads_test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Controller
public class FileUploadController {

    @RequestMapping("/")
    public String uploadForm() {
        return "upload"; // 업로드 폼을 보여주는 뷰 이름
    }

    @PostMapping("/upload_result")
    public String uploadFiles(@RequestParam("files") MultipartFile[] multipartFiles, Model model) {
        // 업로드된 파일을 저장할 폴더 경로 (변경 필요)
        String uploadDir = "C:\\movedfile"; // 변경할 경로

        // 업로드된 파일 처리 로직
        for (MultipartFile multipartFile : multipartFiles) {
            String fileName = multipartFile.getOriginalFilename();
            Path filePath = Path.of(uploadDir, fileName);
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                // 에러 처리 로직 추가
            }
        }

        model.addAttribute("message", "Files uploaded successfully!");
        return "upload_result"; // 업로드 결과를 보여줄 뷰 이름
    }
}
