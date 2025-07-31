//package org.maengle.model.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.maengle.file.entities.FileInfo;
//import org.maengle.file.services.FileInfoService;
//import org.maengle.model.entities.Model;
//import org.maengle.model.repositories.ModelRepository;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.modelmapper.ModelMapper;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.when;
//
//class ModelInfoServiceTest {
//
//    @Mock
//    private ModelRepository modelRepository;
//
//    @Mock
//    private FileInfoService fileInfoService;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    @InjectMocks
//    private ModelViewService modelInfoService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this); // Mockito 초기화
//    }
//
//    @Test
//    void test() {
//        // given
//        Long seq = 1L;
//        Model model = new Model();
//        model.setSeq(seq);
//        model.setGid("1234");
//        model.setName("샘플 모델");
//        model.setCategory("카테고리");
//        model.setDescription("상세 설명");
//
//        FileInfo mainFile = new FileInfo();
//        mainFile.setFileName("main1.jpg");
//
//        FileInfo listFile = new FileInfo();
//        listFile.setFileName("list1.jpg");
//
//        when(modelRepository.findById(seq)).thenReturn(Optional.of(model));
//        when(fileInfoService.getList("1234", "main")).thenReturn(List.of(mainFile));
//        when(fileInfoService.getList("1234", "list")).thenReturn(List.of(listFile));
//
//        Model result = modelInfoService.get(seq);
//
//        assertNotNull(result);
//        assertEquals("1234", result.getGid());
//        assertEquals("샘플 모델", result.getName());
//        assertEquals("카테고리", result.getCategory());
//        assertEquals("상세 설명", result.getDescription());
//        assertEquals("main1.jpg", result.getMainImages().get(0).getFileName());
//        assertEquals("list1.jpg", result.getListImages().get(0).getFileName());
//
//        System.out.println("▶ GID: " + result.getGid());
//        System.out.println("▶ 이름: " + result.getName());
//        System.out.println("▶ 카테고리: " + result.getCategory());
//        System.out.println("▶ 설명: " + result.getDescription());
//        System.out.println("▶ 메인 이미지: " + result.getMainImages().get(0).getFileName());
//        System.out.println("▶ 리스트 이미지: " + result.getListImages().get(0).getFileName());
//    }
//}
