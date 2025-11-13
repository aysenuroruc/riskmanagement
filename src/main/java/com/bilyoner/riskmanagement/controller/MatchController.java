package com.bilyoner.riskmanagement.controller;

import com.bilyoner.riskmanagement.model.domain.MatchDO;
import com.bilyoner.riskmanagement.model.dto.response.MatchResponseDTO;
import com.bilyoner.riskmanagement.model.mapper.MatchMapper;
import com.bilyoner.riskmanagement.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final MatchMapper matchMapper;

    @GetMapping
    public ResponseEntity<List<MatchResponseDTO>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> getMatchById(@PathVariable Long id) {
        MatchDO matchDO = matchService.getMatchById(id);
        return ResponseEntity.ok(matchMapper.toDTO(matchDO));
    }
}
