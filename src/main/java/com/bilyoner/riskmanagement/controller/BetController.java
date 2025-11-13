package com.bilyoner.riskmanagement.controller;

import com.bilyoner.riskmanagement.model.domain.BetDO;
import com.bilyoner.riskmanagement.model.dto.request.BetRequestDTO;
import com.bilyoner.riskmanagement.model.dto.response.BetResponseDTO;
import com.bilyoner.riskmanagement.model.mapper.BetMapper;
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
    private final BetMapper betMapper;

    @PostMapping
    public ResponseEntity<BetResponseDTO> placeBet(@Valid @RequestBody BetRequestDTO betRequest) {
        BetDO betDO = betMapper.toDO(betRequest);
        betDO = betService.placeBet(betDO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(betMapper.toResponseDTO(betDO));
    }
}