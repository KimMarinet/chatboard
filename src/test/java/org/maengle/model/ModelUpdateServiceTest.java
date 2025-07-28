package org.maengle.model;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.maengle.admin.model.controllers.RequestModel;
import org.maengle.global.libs.Utils;
import org.maengle.model.entities.Model;
import org.maengle.model.repositories.ModelRepository;
import org.maengle.model.services.ModelUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ModelUpdateServiceTest {

    @Autowired
    private ModelUpdateService modelUpdateService;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private Utils utils;

    @Test
    void test1() {
        RequestModel form = new RequestModel(); // 가상 폼 데이터, seq 제외하고 설정
        form.setMid("model_001");
        form.setName("테스트모델");
        form.setDescription("설명");

        Model saved = modelUpdateService.process(form);

        System.out.println("== 저장 된 모델 ==");
        System.out.println(saved);
        System.out.println("== 생성일자 ==");
        System.out.println(saved.getCreatedAt());

        // DB에서 저장 된 모델 다시 조회
        Model found = modelRepository.findById(saved.getSeq()).orElse(null);

        // 저장 된 모델이 null이 아니고, seq가 자동 생성 되고, 이름이 올바른지 확인
        assert found != null;
        assert found.getName().equals("테스트모델");
    }

    @Test
    void test2() {
        Model origin = new Model(); // 기존 모델 저장
        origin.setMid("model_002");
        origin.setName("원래이름");
        origin.setDescription("원래설명");
        modelRepository.saveAndFlush(origin);

        RequestModel form = new RequestModel();
        form.setSeq(origin.getSeq());
        form.setMid("model_002");
        form.setName("수정된이름");
        form.setDescription("수정된설명");

        /*
        * seq를 통해 기존 모델이 조회 되었는지
        * 기존 모델의 name, description이 덮어 씌워졌는지
        * modelRepository.saveAndFlush가 정상 작동 하는지 확인
        */

        Model updated = modelUpdateService.process(form);

        System.out.println("== 수정된모델 ==");
        System.out.println(updated);

        assert updated.getName().equals("수정된이름"); // 이름이 수정 된 값과 일치하는지 확인
    }

    /* DELETE + 상태 처리 미완료 */
}
