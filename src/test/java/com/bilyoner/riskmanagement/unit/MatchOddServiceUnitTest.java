package com.bilyoner.riskmanagement.unit;

import com.bilyoner.riskmanagement.enums.MatchResult;
import com.bilyoner.riskmanagement.model.domain.MatchOddsDO;
import com.bilyoner.riskmanagement.model.entity.MatchOdds;
import com.bilyoner.riskmanagement.model.mapper.MatchOddMapper;
import com.bilyoner.riskmanagement.repository.MatchOddRepository;
import com.bilyoner.riskmanagement.service.impl.MatchOddServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchOddServiceUnitTest {

    private final MatchOddRepository matchOddRepository = mock(MatchOddRepository.class);
    private final MatchOddMapper matchOddMapper = mock(MatchOddMapper.class);
    private final MatchOddServiceImpl matchOddService = new MatchOddServiceImpl(matchOddRepository, matchOddMapper);

    @Test
    void updateMatchOdds_shouldUpdateAndSave() {
        MatchOddsDO matchOddsDO = new MatchOddsDO();
        matchOddsDO.setId(1L);
        matchOddsDO.setRiskLimit(BigDecimal.TEN);
        matchOddsDO.setOddsValue(BigDecimal.ONE);

        MatchOdds matchOdds = new MatchOdds();
        matchOdds.setId(1L);

        when(matchOddRepository.findById(1L)).thenReturn(Optional.of(matchOdds));

        matchOddService.updateMatchOdds(matchOddsDO);

        assertThat(matchOdds.getRiskLimit()).isEqualTo(BigDecimal.TEN);
        assertThat(matchOdds.getOddsValue()).isEqualTo(BigDecimal.ONE);
        verify(matchOddRepository).save(matchOdds);
    }

    @Test
    void updateMatchOdds_shouldThrowIfNotFound() {
        MatchOddsDO matchOddsDO = new MatchOddsDO();
        matchOddsDO.setId(99L);

        when(matchOddRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchOddService.updateMatchOdds(matchOddsDO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Match odd not found");
    }

    @Test
    void findAllMatchOddsByMatchId_shouldReturnMappedList() {
        MatchOdds entity = new MatchOdds();
        entity.setId(1L);
        when(matchOddRepository.findAllByMatchId(1L)).thenReturn(List.of(entity));

        MatchOddsDO dto = new MatchOddsDO();
        dto.setId(1L);
        when(matchOddMapper.toDO(entity)).thenReturn(dto);

        List<MatchOddsDO> result = matchOddService.findAllMatchOddsByMatchId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(1L);
    }

    @Test
    void findAllMatchOddsByMatchIdAndResult_shouldReturnMapped() {
        MatchOdds entity = new MatchOdds();
        entity.setId(2L);
        when(matchOddRepository.findByMatchIdAndResultType(2L, MatchResult.MS1)).thenReturn(Optional.of(entity));

        MatchOddsDO dto = new MatchOddsDO();
        dto.setId(2L);
        when(matchOddMapper.toDO(entity)).thenReturn(dto);

        MatchOddsDO result = matchOddService.findAllMatchOddsByMatchIdAndResult(2L, MatchResult.MS1);

        assertThat(result.getId()).isEqualTo(2L);
    }
}
