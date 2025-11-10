package com.bilyoner.riskmanagement.controller;

import com.bilyoner.riskmanagement.model.dto.response.MatchResponseDto;
import com.bilyoner.riskmanagement.model.dto.response.RiskInfoResponseDto;
import com.bilyoner.riskmanagement.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    // Tüm maçları alan bir api, matchId ile get eden api yazılacak.

    @GetMapping
    public ResponseEntity<List<MatchResponseDto>> getAllMatches() {
       return ResponseEntity.ok(matchService.getAllMatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponseDto> getMatchById(@PathVariable Long id) {
        MatchResponseDto match = matchService.getMatchById(id);
        return ResponseEntity.ok(match);
    }

    @GetMapping("/{id}/risk")
    public ResponseEntity<RiskInfoResponseDto> getMatchRiskInfo(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.getMatchRiskInfo(id));
    }
}
