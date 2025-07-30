package org.maengle.file.services;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.maengle.file.constants.FileStatus;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.entities.QFileInfo;
import org.maengle.file.exceptions.FileNotFoundException;
import org.maengle.file.repositories.FileInfoRepository;
import org.maengle.global.configs.FileProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileInfoService {
    private final FileInfoRepository repository;
    private final HttpServletRequest request;
    private final FileProperties fileProperties;

    // 하나의 파일 조회

    public FileInfo get(Long seq) {
        FileInfo item = repository.findById(seq).orElseThrow(FileNotFoundException::new);

        addInfo(item);

        return item;
    }

    // 파일 목록 조회

    public List<FileInfo> getList(String gid, String location, FileStatus status) {
        status = Objects.requireNonNullElse(status, FileStatus.ALL);

        QFileInfo fileInfo = QFileInfo.fileInfo;
        BooleanBuilder andBuilder = new BooleanBuilder();
        andBuilder.and(fileInfo.gid.eq(gid));

        if (StringUtils.hasText(location)) {
            andBuilder.and(fileInfo.location.eq(location));
        }

        if (status != FileStatus.ALL) {
            andBuilder.and(fileInfo.done.eq(status == FileStatus.DONE));
        }

        List<FileInfo> items = (List<FileInfo>) repository.findAll(andBuilder, fileInfo.createdAt.asc());

        items.forEach(this::addInfo);

        return items;
    }

    public List<FileInfo> getList(String gid, String location) {
        return getList(gid, location, FileStatus.DONE);
    }

    public List<FileInfo> getList(String gid) {
        return getList(gid, null);
    }

    public FileInfo get(String gid) {
        List<FileInfo> items = getList(gid);
        return items == null || items.isEmpty() ? null : items.getFirst();
    }


    public void addInfo(FileInfo item) {
        item.setFileUrl(getFileUrl(item));
        item.setFilePath(getFilePath(item));

        // 이미지인지 boolean을 활용해 체크
        String contentType = item.getContentType();
        item.setImage(StringUtils.hasText(contentType) && contentType.startsWith("image"));

        //

        if (item.isImage()) {
            String folder = folder(item);
            String thumbPath = String.format("%s/thumbs/%s/", fileProperties.getPath(), folder);
            String thumbUrl = String.format("%s%s/thumbs/%s/", request.getContextPath(), fileProperties.getUrl(), folder);

            item.setThumbBasePath(thumbPath);
            item.setThumbBaseUrl(thumbUrl);
        }
    }

    public String folder(FileInfo item) {
        long seq = item.getSeq();

        return folder(seq);
    }

    public String folder(long seq) {
        return String.valueOf(seq % 10L); // 0 ~ 9
    }

    // 브라우저에서 접근할 수 있는 URL
    public String getFileUrl(FileInfo item) {
        return String.format("%s%s/%s/%s", request.getContextPath(), fileProperties.getUrl(), folder(item), item.getSeq()+ Objects.requireNonNullElse(item.getExtension(), ""));
    }

    // 파일이 위치한 서버 경로
    public String getFilePath(FileInfo item) {
        return String.format("%s/%s/%s", fileProperties.getPath(), folder(item), item.getSeq()+ Objects.requireNonNullElse(item.getExtension(),""));
    }

}
