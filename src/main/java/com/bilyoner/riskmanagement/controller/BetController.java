package com.bilyoner.riskmanagement.controller;

import com.bilyoner.riskmanagement.model.dto.request.BetRequestDto;
import com.bilyoner.riskmanagement.model.dto.response.BetResponseDto;
import com.bilyoner.riskmanagement.service.BetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bets")
@RequiredArgsConstructor
public class BetController {

    private final BetService betService;

    @PostMapping
    public ResponseEntity<BetResponseDto> placeBet(@Valid @RequestBody BetRequestDto betRequest) {
        BetResponseDto response = betService.placeBet(betRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BetResponseDto> getBetById(@PathVariable Long id) {
        return ResponseEntity.ok(betService.getBetById(id));
    }
}