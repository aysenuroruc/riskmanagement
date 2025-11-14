package com.bilyoner.riskmanagement.integration;

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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.hamcrest.Matchers.closeTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class BetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
        String body = objectMapper.writeValueAsString(betRequestDTO);

        mockMvc.perform(post("/api/v1/bet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/match/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.odds[0].oddsValue").value(closeTo(1.12, 0.03)));
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
        String body = objectMapper.writeValueAsString(betRequestDTO);

        mockMvc.perform(post("/api/v1/bet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenDuplicateResultsInSelections() throws Exception {
        BetRequestDTO betRequestDTO = new BetRequestDTO();
        betRequestDTO.setBetAmount(new BigDecimal("50.0"));
        ArrayList<BetSelectionDTO> selections = new ArrayList<>();

        BetSelectionDTO s1 = new BetSelectionDTO();
        s1.setMatchId(1);
        s1.setSelectedResult(MatchResult.MS1);
        selections.add(s1);

        BetSelectionDTO s2 = new BetSelectionDTO();
        s2.setMatchId(1); // aynı maç
        s2.setSelectedResult(MatchResult.MS1); // duplicate result
        selections.add(s2);

        betRequestDTO.setSelections(selections);
        String body = objectMapper.writeValueAsString(betRequestDTO);

        mockMvc.perform(post("/api/v1/bet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
