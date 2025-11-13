package com.bilyoner.riskmanagement;

import com.bilyoner.riskmanagement.enums.MatchResult;
import com.bilyoner.riskmanagement.model.dto.request.BetRequestDTO;
import com.bilyoner.riskmanagement.model.dto.request.BetSelectionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.hamcrest.Matchers.closeTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class BetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnMatchWithCorrectOddsAfterBetIsPlaced() throws Exception {
        BetRequestDTO betRequestDTO = new BetRequestDTO();
        betRequestDTO.setBetAmount(new BigDecimal("2187.0"));
        betRequestDTO.setSelections(new ArrayList<>());

        BetSelectionDTO betSelectionDTO = new BetSelectionDTO();
        betSelectionDTO.setMatchId(1);
        betSelectionDTO.setSelectedResult(MatchResult.MS1);

        betRequestDTO.getSelections().add(betSelectionDTO);

        betSelectionDTO = new BetSelectionDTO();
        betSelectionDTO.setMatchId(2);
        betSelectionDTO.setSelectedResult(MatchResult.MSX);

        betRequestDTO.getSelections().add(betSelectionDTO);
        betSelectionDTO = new BetSelectionDTO();
        betSelectionDTO.setMatchId(3);
        betSelectionDTO.setSelectedResult(MatchResult.MSX);

        betRequestDTO.getSelections().add(betSelectionDTO);
        String body = new ObjectMapper().writeValueAsString(betRequestDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/match/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.odds[0].oddsValue").value(closeTo(1.12, 0.01)));
    }

    @Test
    void shouldReturn400AfterBetIsPlaced() throws Exception {
        BetRequestDTO betRequestDTO = new BetRequestDTO();
        betRequestDTO.setBetAmount(new BigDecimal("10000.0"));
        betRequestDTO.setSelections(new ArrayList<>());

        BetSelectionDTO betSelectionDTO = new BetSelectionDTO();
        betSelectionDTO.setMatchId(1);
        betSelectionDTO.setSelectedResult(MatchResult.MS1);

        betRequestDTO.getSelections().add(betSelectionDTO);

        betSelectionDTO = new BetSelectionDTO();
        betSelectionDTO.setMatchId(2);
        betSelectionDTO.setSelectedResult(MatchResult.MSX);

        betRequestDTO.getSelections().add(betSelectionDTO);
        betSelectionDTO = new BetSelectionDTO();
        betSelectionDTO.setMatchId(3);
        betSelectionDTO.setSelectedResult(MatchResult.MSX);

        betRequestDTO.getSelections().add(betSelectionDTO);
        String body = new ObjectMapper().writeValueAsString(betRequestDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isBadRequest());
    }
}
