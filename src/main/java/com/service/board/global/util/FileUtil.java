package com.service.board.global.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUtil {

    @Value("${upload.path}")
    private String uploadPath;

    // 파일 업로드 처리
    public String upload(MultipartFile file) throws IOException {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFileName.contains("..")) throw new IOException("잘못된 파일명입니다.");

        // 파일명 중복 방지를 위한 UUID 적용
        String fileExtension = "";
        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex > 0) fileExtension = originalFileName.substring(dotIndex);
        String newFileName = UUID.randomUUID() + fileExtension;

        // 업로드 경로 설정
        Path uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
        if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);
        Path targetPath = uploadDir.resolve(newFileName).normalize();

        // 파일 저장
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // 웹에서 접근 가능한 URL 반환
        return "/uploads/" + newFileName;
    }
}
