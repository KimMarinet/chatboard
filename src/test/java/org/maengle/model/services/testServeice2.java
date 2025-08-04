//package org.maengle.model.services;
//
//import jdk.jfr.Category;
//import org.junit.jupiter.api.Test;
//import org.maengle.model.entities.Categorys;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static java.lang.reflect.Array.get;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.List;
//
//@SpringBootTest
//public class testServeice2 {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private CategorySerive serive;
//
//    @Test
//    void getAllCategories_ReturnsCategoryList() throws Exception {
//        // given
//        List<Category> mockCategories = List.of(
//                new Categorys(1L, "액션", "액션 장르"),
//                new Categorys(2L, "로맨스", "사랑 이야기")
//        );
//        when(serive.get()).thenReturn(mockCategories);
//
//        // when & then
//        mockMvc.perform(get("/categories, model"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(2))
//                .andExpect(jsonPath("$[0].name").value("액션"))
//                .andExpect(jsonPath("$[1].name").value("로맨스"));
//    }
//
//
//
//
//
//}
