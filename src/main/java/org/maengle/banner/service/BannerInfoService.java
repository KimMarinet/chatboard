package org.maengle.banner.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.maengle.admin.banner.controllers.BannerGroupSearch;
import org.maengle.admin.banner.controllers.RequestBanner;
import org.maengle.banner.entities.Banner;
import org.maengle.banner.entities.BannerGroup;
import org.maengle.banner.entities.QBanner;
import org.maengle.banner.entities.QBannerGroup;
import org.maengle.banner.repositories.BannerGroupRepository;
import org.maengle.banner.repositories.BannerRepository;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.services.FileInfoService;
import org.maengle.global.libs.Utils;
import org.maengle.global.search.ListData;
import org.maengle.global.search.Pagination;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class BannerInfoService {
    private final BannerRepository bannerRepository;
    private final BannerGroupRepository bannerGroupRepository;
    private final FileInfoService fileInfoService;
    private final HttpServletRequest request;

    public Banner get(Long seq) {
        Banner banner = bannerRepository.findById(seq).orElseThrow();

        addBannerInfo(banner);

        return banner;
    }

    public RequestBanner getForm(Long seq) {
        Banner data = get(seq);
        RequestBanner form = new ModelMapper().map(data, RequestBanner.class);
        form.setBannerImage(data.getBannerImage());
        form.setGroupCode(data.getBannerGroup().getGroupCode());
        form.setMode("edit");

        return form;
    }

    public List<Banner> getList(String groupCode, boolean isAll) {
        QBanner banner = QBanner.banner;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(banner.bannerGroup.groupCode.eq(groupCode));

        if (!isAll) {
            builder.and(banner.active.eq(true));
        }

        List<Banner> items = (List<Banner>)bannerRepository.findAll(builder, Sort.by(desc("listOrder"), asc("createdAt")));

        items.forEach(this::addBannerInfo);

        return items;
    }

    public List<Banner> getList(String groupCode) {
        return getList(groupCode, false);
    }

    public BannerGroup getGroup(String groupCode, boolean isAll) {
        BannerGroup group = bannerGroupRepository.findById(groupCode).orElseThrow();

        List<Banner> banners = getList(groupCode, isAll);
        group.setBanners(banners);

        return group;
    }

    public BannerGroup getGroup(String groupCode) {
        return getGroup(groupCode, false);
    }

    /**
     * 배너 그룹 목록
     *
     * @return
     */
    public ListData<BannerGroup> getGroupList(BannerGroupSearch search) {
        int page = Utils.onlyPositiveNumber(search.getPage(), 1);
        int limit = Utils.onlyPositiveNumber(search.getLimit(), 20);

        BooleanBuilder builder = new BooleanBuilder();
        QBannerGroup bannerGroup = QBannerGroup.bannerGroup;

        /* 검색 조건 처리 S */
        String sopt = search.getSopt();
        String skey = search.getSkey();

        sopt = StringUtils.hasText(sopt) ? sopt : "all";
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();
            BooleanExpression groupCodeCond = bannerGroup.groupCode.contains(skey);
            BooleanExpression groupNameCond = bannerGroup.groupName.contains(skey);

            if (sopt.equals("groupCode")) { // 그룹 코드
                builder.and(groupCodeCond);
            } else if (sopt.equals("groupName")) { // 그룹명
                builder.and(groupNameCond);

            } else { // 통합 검색
                BooleanBuilder orBuilder = new BooleanBuilder();
                builder.and(orBuilder.or(groupCodeCond).or(groupNameCond));
            }
        }
        /* 검색 조건 처리 E */

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));

        Page<BannerGroup> data = bannerGroupRepository.findAll(builder, pageable);
        int total = (int)bannerGroupRepository.count(builder);

        Pagination pagination = new Pagination(page, total, 10, limit, request);

        return new ListData<>(data.getContent(), pagination);
    }

    private void addBannerInfo(Banner banner) {
        String groupCode = banner.getBannerGroup().getGroupCode();
        List<FileInfo> banners = fileInfoService.getListDone(groupCode, "banner_" + banner.getSeq());
        if (banners != null && !banners.isEmpty()) {
            banner.setBannerImage(banners.get(0));
        }
    }

}
