package org.grpcvsrest.leaderboard.rest;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.grpcvsrest.leaderboard.grpc.LeaderboardGrpcEndpoint;
import org.grpcvsrest.leaderboard.service.Leaderboard;
import org.grpcvsrest.leaderboard.service.LeaderboardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class LeaderboardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LeaderboardService leaderboardService;
    @MockBean
    private LeaderboardGrpcEndpoint grpcEndpoint;


    @Test
    public void testLeaderboard() throws Exception {
        // given
        when(leaderboardService.getLeaderboard("Pokemon"))
                .thenReturn(new Leaderboard(
                        5,
                        Arrays.asList(
                                new Leaderboard.Line("Tarzan", 1, 3),
                                new Leaderboard.Line("Jane", 0, 2))));

        // when
        mockMvc.perform(
                get("/leaderboard/Pokemon")
        ) // then
        .andExpect(status().is(200))
        .andExpect(content().json(expectedJson()));

    }

    private String expectedJson() throws IOException {
        return Resources.toString(Resources.getResource("leaderboard.json"), Charsets.UTF_8);
    }


}