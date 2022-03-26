package codes.karlo.api.controller;

import codes.karlo.api.entity.Url;
import codes.karlo.api.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

@WebMvcTest(UrlController.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlService urlService;

    private Url url;

    @BeforeEach
    void setUp() {
        url = Url.builder()
                .longUrl("https://karlo.codes")
                .shortUrl("karlo")
                .visits(0L)
                .createDate(LocalDateTime.now())
                .build();
    }

    @Test
    void saveUrl() throws Exception {
        Url inputUrl = Url.builder()
                .longUrl("https://karlo.codes")
                .shortUrl("karlo")
                .visits(0L)
                .createDate(LocalDateTime.now())
                .build();

        String api_key = "";
        Mockito.when(urlService.saveUrlWithApiKey(inputUrl, api_key))
                .thenReturn(url);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"longUrl\": \"https://l.karlo.codes\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void fetchUrlByShort() throws Exception {
        Mockito.when(urlService.fetchUrlByShortUrl("karlo"))
                .thenReturn(url);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/url/karlo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.longUrl").value(url.getLongUrl()));


    }
}