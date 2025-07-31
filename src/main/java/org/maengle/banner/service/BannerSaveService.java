package org.maengle.banner.service;

import lombok.RequiredArgsConstructor;
import org.maengle.admin.banner.controllers.RequestBanner;
import org.maengle.banner.entities.Banner;
import org.maengle.banner.entities.BannerGroup;
import org.maengle.banner.repositories.BannerGroupRepository;
import org.maengle.banner.repositories.BannerRepository;
import org.maengle.file.controllers.RequestUpload;
import org.maengle.file.services.FileUploadService;
import org.maengle.global.exceptions.script.AlertException;
import org.maengle.global.libs.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BannerSaveService {
    private final BannerGroupRepository groupRepository;
    private final BannerRepository repository;
    private final FileUploadService uploadService;
    private final Utils utils;

    public void save(RequestBanner form) {
        String mode = form.getMode();
        Long seq = form.getSeq();

        mode = StringUtils.hasText(mode) ? mode : "add";

        Banner banner = null;
        if (mode.equals("edit") && seq != null) {
            banner = repository.findById(seq).orElseThrow();
        } else {
            banner = new Banner();
            BannerGroup bannerGroup = groupRepository.findById(form.getGroupCode()).orElseThrow();
            banner.setBannerGroup(bannerGroup);
        }

        banner.setBName(form.getBName());
        banner.setListOrder(form.getListOrder());
        banner.setActive(form.isActive());
        banner.setBLink(form.getBLink());
        banner.setTarget(form.getTarget());

        repository.save(banner);

        String groupCode = banner.getBannerGroup().getGroupCode();
        if (form.getFiles() != null && !form.getFiles()[0].isEmpty()) {
            try {
                RequestUpload uploadForm = new RequestUpload();
                uploadForm.setFiles(form.getFiles());
                uploadForm.setGid(groupCode);
                uploadForm.setGid(groupCode);
                uploadForm.setLocation("banner_" + banner.getSeq());
                uploadForm.setSingle(true);
                uploadForm.setImageOnly(true);

                uploadService.uploadProcess(uploadForm);
                uploadService.processDone(groupCode);
            } catch (Exception e) { }
        }
    }

    public void saveList(List<Integer> chks) {
        if (chks == null || chks.isEmpty()) {
            throw new AlertException("수정할 배너를 선택하세요.", HttpStatus.BAD_REQUEST);
        }

        for (int chk : chks) {
            Long seq = Long.valueOf(utils.getParam("seq_" + chk));
            boolean active = Boolean.parseBoolean(utils.getParam("active_" + chk));

            Banner banner = repository.findById(seq).orElse(null);
            if (banner == null) continue;

            banner.setActive(active);
        }

        repository.flush();
    }
}
