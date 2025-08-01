package org.maengle.banner.service;

import lombok.RequiredArgsConstructor;
import org.maengle.banner.entities.Banner;
import org.maengle.banner.entities.BannerGroup;
import org.maengle.banner.repositories.BannerGroupRepository;
import org.maengle.global.exceptions.script.AlertException;
import org.maengle.global.libs.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerGroupDeleteService {
    private final BannerGroupRepository groupRepository;
    private final BannerInfoService bannerInfoService;
    private final BannerDeleteService bannerDeleteService;
    private final Utils utils;

    public void delete(String groupCode) {
        BannerGroup data = bannerInfoService.getGroup(groupCode, true);
        List<Banner> banners = data.getBanners();
        if (banners != null && !banners.isEmpty()) {
            banners.forEach(b -> bannerDeleteService.delete(b.getSeq()));
        }

        groupRepository.delete(data);
    }


    public void deleteList(List<Integer> chks) {
        if (chks == null || chks.isEmpty()) {
            throw new AlertException("삭제할 배너 그룹을 선택하세요.", HttpStatus.BAD_REQUEST);
        }

        for (int chk : chks) {
            String groupCode = utils.getParam("groupCode_" + chk);
            BannerGroup data = groupRepository.findById(groupCode).orElse(null);
            if (data == null) continue;

            groupRepository.delete(data);
        }

        groupRepository.flush();
    }
}
