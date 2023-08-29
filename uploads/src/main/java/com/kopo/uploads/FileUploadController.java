package com.kopo.uploads;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Controller
public class FileUploadController {

    @RequestMapping("/")
    public String uploadForm() {
        return "upload"; // 업로드 폼을 보여주는 뷰 이름
    }

    @Async // 비동기 실행을 위한 어노테이션 추가
    @PostMapping("/upload_result")
    public CompletableFuture<String> uploadFiles(@RequestParam("files") MultipartFile[] multipartFiles, Model model) {
        // 업로드된 파일을 저장할 폴더 경로 (변경 필요)
        String uploadDir = "C:\\movedfile"; // 변경할 경로

        CompletableFuture<Void> uploadTasks = CompletableFuture.runAsync(() -> {
            for (MultipartFile multipartFile : multipartFiles) {
                String fileName = multipartFile.getOriginalFilename();
                Path filePath = Path.of(uploadDir, fileName);
                try {
                    multipartFile.transferTo(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    // 에러 처리 로직 추가
                }
            }
        });

        model.addAttribute("message", "Uploading files..."); // 업로드 중임을 알려주는 메시지

        return uploadTasks.thenApplyAsync(ignored -> {
            model.addAttribute("message", "Files uploaded successfully!");
            return "upload_result"; // 업로드 결과를 보여줄 뷰 이름
        });
    }
}
