package org.maengle.model.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.maengle.admin.model.controllers.RequestModel;
import org.maengle.file.services.FileInfoService;
import org.maengle.file.services.FileUploadService;
import org.maengle.global.exceptions.script.AlertException;
import org.maengle.global.libs.Utils;
import org.maengle.model.constants.ModelStatus;
import org.maengle.model.entities.Model;
import org.maengle.model.repositories.ModelRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelUpdateService {
    private final ModelRepository modelRepository;
    private final HttpServletRequest request;
    private final Utils utils;
    private final FileInfoService fileInfoService;
    private final FileUploadService fileUploadService;

    // 모델 등록,수정 화면에서 입력한 값을 requestModel에 담아서 넘겨줌
    public Model process(RequestModel form) {
        Long seq = form.getSeq(); // seq(모델고유번호)로 구분

        // seq 값이 없거나 0보다 작으면 새 모델 객체 생성, 아니면 기존 모델 불러오기
        Model item = (seq == null || seq < 1L)
                ? new Model()
                : modelRepository.findById(seq).orElseGet(Model::new);

        // 모델 등록일 경우에만 고유 GID 설정
        if (seq == null || seq.equals("add")) {
            item.setGid(form.getGid());
        }

        // 공통 정보 저장 (등록/수정 공통 처리)
        item.setName(form.getName());
        item.setDescription(form.getDescription());
        item.setCategory(form.getCategory());
        item.setModelStatus(form.getModelStatus());
        item.setListImages(fileInfoService.getList(form.getGid(), "list"));
        item.setMainImages(fileInfoService.getList(form.getGid(), "main"));

        modelRepository.saveAndFlush(item); // DB에 저장

        fileUploadService.processDone(item.getGid());

        //modelInfoService.processDone(form.getGid());
        /*
        * 후처리 예정 (상태 변경 등..)
        * ModelInfoService와 같이 완성
        */

        return item;

    }

    public void processList(List<Integer> idxes) { // idxes : 사용자가 선택한 인덱스 목록
        if (idxes == null || idxes.isEmpty()) {
            throw new AlertException("처리할 모델을 선택하세요");
            // CommonControllerAdvice+common/_execute_script
        }

        List<Model> items = new ArrayList<>();

        for (int idx : idxes) { // 선택한 인덱스 목록 하나씩 반복해서 seq 가져온다
            Long seq = Long.valueOf(utils.getParam("seq_" + idx));
            Model item = modelRepository.findById(seq).orElse(null); // 해당 seq로 모델을 DB에서 조회
            if (item == null) continue;

            if (request.getMethod().equalsIgnoreCase("DELETE")) { // 요청 방식이 DELETE면 삭제처리하고 삭제시간 등록
                item.setDeletedAt(LocalDateTime.now());
            } else { // DELETE가 아니면 status 업데이트
                ModelStatus status = ModelStatus.valueOf(utils.getParam("status_" + idx));
                item.setModelStatus(status);
            }

            items.add(item);
        }

        modelRepository.saveAllAndFlush(items); // 수정 된 모든 모델 한 번에 저장
    }
}
