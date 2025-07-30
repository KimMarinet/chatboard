package org.maengle.file.services;

import lombok.RequiredArgsConstructor;
import org.maengle.file.constants.FileStatus;
import org.maengle.file.controllers.RequestUpload;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.repositories.FileInfoRepository;
import org.maengle.global.configs.FileProperties;
import org.maengle.global.exceptions.script.AlertBackException;
import org.maengle.global.libs.Utils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Lazy
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileUploadService {
    private final Utils utils;
    private final FileProperties fileProperties;
    private final FileInfoRepository repository;

    private final FileInfoService fileInfoService;
    private final FileDeleteService fileDeleteService;

    public List<FileInfo> uploadProcess(RequestUpload uploadForm) {
        String gid = uploadForm.getGid();
        gid = StringUtils.hasText(gid) ? gid : UUID.randomUUID().toString(); // gid 설정 없으면 uuid를 활용해 새로 생성

        String location = uploadForm.getLocation();
        boolean single = uploadForm.isSingle(); // 하나의 파일만 업로드하기 위해 사용하는 boolean
        boolean imageOnly = uploadForm.isImageOnly(); // 이미지 형식만을 받기 위한 boolean

        MultipartFile[] files = uploadForm.getFiles();
        if (files == null || files.length == 0) {
            // 파일 업로드 X
            throw new AlertBackException(utils.getMessage("NotUpload.file"), HttpStatus.BAD_REQUEST);
        }
        // 하나의 파일만 업로드
        if (single) {

            // 기존 파일 삭제
            fileDeleteService.process(gid, location);

            files = new MultipartFile[] {files[0]};
        }

        // 이미지만 올리는 경우
        if (imageOnly) {
            files = Arrays.stream(files).filter(file -> file.getContentType() != null && file.getContentType().startsWith("image/")).toArray(MultipartFile[]::new);
        }

        String basePath = fileProperties.getPath(); // 파일 업로드 기본 경로

        List<FileInfo> uploadedFiles = new ArrayList<>(); // 업로드 파일이 여러개일때 받기 위해 사용할 변수

        for (MultipartFile file : files) {
            // DB에 업로드 파일 기록
            String fileName = file.getOriginalFilename(); // 업로드 파일명
            String extension = fileName.substring(fileName.lastIndexOf(".")); // 확장자 이름
            String contentType = file.getContentType();

            FileInfo item = new FileInfo();

            item.setGid(gid);

            if (StringUtils.hasText(location)) {
                item.setLocation(location);
            }

            item.setFileName(fileName);
            item.setExtension(extension);
            item.setContentType(contentType);

            repository.saveAndFlush(item);



            // 업로드 파일을 서버에 올릴 경로 만들고 업로드 처리
            long seq = item.getSeq();
            long folder = seq % 10L; // 폴더 번호
            String _fileName = seq + Objects.requireNonNullElse(extension, "");
            String uploadDir = String.format("%s/%s", basePath, folder);

            File _uploadDir = new File(uploadDir);

            if (!_uploadDir.exists() || !_uploadDir.isDirectory()) {
                _uploadDir.mkdir(); // 업로드 폴더 없으면 만들기
            }
            File uploadPath = new File(_uploadDir, _fileName);
            try {
                file.transferTo(uploadPath);
                fileInfoService.addInfo(item);
                uploadedFiles.add(item);
            } catch (IOException e) {
                repository.deleteById(seq);
                repository.flush();
                e.printStackTrace();
            }

        }
        return uploadedFiles;

    }


    // 파일 그룹 작업 시 그룹 작업이 되었음을 표시
    public void processDone(String gid) {
        List<FileInfo> items = fileInfoService.getList(gid, null, FileStatus.ALL);

        items.forEach(item -> item.setDone(true));

        repository.saveAllAndFlush(items);
    }
}
