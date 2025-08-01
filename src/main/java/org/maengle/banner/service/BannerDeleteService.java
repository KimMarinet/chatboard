package org.maengle.banner.service;

import lombok.RequiredArgsConstructor;
import org.maengle.banner.entities.Banner;
import org.maengle.banner.repositories.BannerRepository;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.services.FileDeleteService;
import org.maengle.global.exceptions.script.AlertException;
import org.maengle.global.libs.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BannerDeleteService {
    private final BannerInfoService bannerInfoService;
    private final BannerRepository bannerRepository;
    private final FileDeleteService fileDeleteService;
    private final Utils utils;

    public void delete(Long seq) {
        Banner banner = bannerInfoService.get(seq);

        FileInfo fileInfo = banner.getBannerImage();
        if (fileInfo != null) {
            fileDeleteService.deleteProcess(fileInfo.getSeq());
        }

        bannerRepository.delete(banner);
        bannerRepository.flush();
    }

    public void deleteList(List<Integer> chks) {
        if (chks == null || chks.isEmpty()) {
            throw new AlertException("삭제할 배너를 선택하세요.", HttpStatus.BAD_REQUEST);
        }

        for (int chk : chks) {
            Long seq = Long.valueOf(utils.getParam("seq_" + chk));
            delete(seq);
        }
    }
}
