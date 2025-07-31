package org.maengle.model.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.maengle.admin.model.controllers.RequestModel;
import org.maengle.file.services.FileUploadService;
import org.maengle.global.exceptions.script.AlertException;
import org.maengle.global.libs.Utils;
import org.maengle.model.constants.ModelStatus;
import org.maengle.model.entities.Model;
import org.maengle.model.repositories.ModelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelUpdateService {
    private final ModelRepository modelRepository;
    private final FileUploadService fileUploadService;
    private final HttpServletRequest request;
    private final ModelMapper mapper;
    private final Utils utils;

    // 모델 등록,수정 화면에서 입력한 값을 requestModel에 담아서 넘겨줌
    public Model process(RequestModel form) {
        Model item = mapper.map(form, Model.class);

        modelRepository.saveAndFlush(item);

        // 파일 업로드 완료 처리
        fileUploadService.processDone(form.getGid());

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
