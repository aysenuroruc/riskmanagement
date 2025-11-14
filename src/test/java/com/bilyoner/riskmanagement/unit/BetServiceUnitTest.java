package com.bilyoner.riskmanagement.unit;

import com.bilyoner.riskmanagement.constants.BetConstants;
import com.bilyoner.riskmanagement.enums.MatchResult;
import com.bilyoner.riskmanagement.exception.InvalidBetException;
import com.bilyoner.riskmanagement.model.domain.BetDO;
import com.bilyoner.riskmanagement.model.domain.BetSelectionDO;
import com.bilyoner.riskmanagement.model.domain.MatchDO;
import com.bilyoner.riskmanagement.model.domain.MatchOddsDO;
import com.bilyoner.riskmanagement.model.entity.Bet;
import com.bilyoner.riskmanagement.model.mapper.BetMapper;
import com.bilyoner.riskmanagement.service.CacheEvictService;
import com.bilyoner.riskmanagement.service.MatchService;
import com.bilyoner.riskmanagement.service.OddsCalculationService;
import com.bilyoner.riskmanagement.service.impl.BetServiceImpl;
import com.bilyoner.riskmanagement.service.MatchOddService;
import io.micrometer.core.instrument.Counter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BetServiceUnitTest {

    @Mock
    private MatchService matchService;

    @Mock
    private Counter placeBetCounter;

    @Mock
    private BetMapper betMapper;

    @Mock
    private com.bilyoner.riskmanagement.repository.BetRepository betRepository;

    @Mock
    private MatchOddService matchOddService;

    @Mock
    private CacheEvictService cacheEvictService;

    @Mock
    private OddsCalculationService oddsCalculationService;

    @InjectMocks
    private BetServiceImpl betService;

    private BetDO sampleBetDO;

    @BeforeEach
    void setUp() {
        sampleBetDO = new BetDO();
        sampleBetDO.setBetAmount(new BigDecimal("100"));
        sampleBetDO.setSelections(new ArrayList<>());
    }

   @Test
    void shouldThrowWhenSelectionsEmpty() {
        sampleBetDO.setSelections(new ArrayList<>());

        assertThatThrownBy(() -> betService.placeBet(sampleBetDO))
                .isInstanceOf(InvalidBetException.class)
                .hasMessage(BetConstants.ERR_SELECTION_REQUIRED);

        verifyNoInteractions(oddsCalculationService);
    }

    @Test
    void shouldThrowWhenDuplicateResultsInSameMatch() {
        List<BetSelectionDO> selections = new ArrayList<>();
        BetSelectionDO s1 = new BetSelectionDO();
        s1.setMatchId(1L);
        s1.setSelectedResult(MatchResult.valueOf("MS1"));
        selections.add(s1);

        BetSelectionDO s2 = new BetSelectionDO();
        s2.setMatchId(1L);
        s2.setSelectedResult(MatchResult.valueOf("MS1"));
        selections.add(s2);

        sampleBetDO.setSelections(selections);

        assertThatThrownBy(() -> betService.placeBet(sampleBetDO))
                .isInstanceOf(InvalidBetException.class)
                .hasMessage(BetConstants.ERR_DUPLICATE_RESULT);

        verifyNoInteractions(oddsCalculationService);
    }

    @Test
    void shouldThrowWhenRiskLimitExceeded() {
        List<BetSelectionDO> selections = new ArrayList<>();
        BetSelectionDO sel = new BetSelectionDO();
        sel.setMatchId(1L);
        sel.setSelectedResult(MatchResult.valueOf("MS1"));
        selections.add(sel);
        sampleBetDO.setSelections(selections);
        sampleBetDO.setBetAmount(new BigDecimal("1000"));

        MatchOddsDO oddsDO = new MatchOddsDO();
        oddsDO.setCurrentRisk(new BigDecimal("500"));
        oddsDO.setRiskLimit(new BigDecimal("1000")); //calculatedRisk=currentRisk + payout

        when(matchOddService.findAllMatchOddsByMatchIdAndResult(eq(1L), any())).thenReturn(oddsDO);
        when(oddsCalculationService.calculatePayout(any())).thenReturn(new BigDecimal("600"));

        assertThatThrownBy(() -> betService.placeBet(sampleBetDO))
                .isInstanceOf(InvalidBetException.class)
                .hasMessage(BetConstants.ERR_RISK_LIMIT);

        verify(oddsCalculationService).calculatePayout(any());
    }

    @Test
    void shouldPlaceBetSuccessfully() {
        BetSelectionDO selection = new BetSelectionDO();
        selection.setMatchId(1L);
        selection.setSelectedResult(MatchResult.MS1);

        sampleBetDO.setSelections(List.of(selection));
        sampleBetDO.setBetAmount(new BigDecimal("10"));

        MatchOddsDO oddsDO = new MatchOddsDO();
        oddsDO.setCurrentRisk(BigDecimal.ZERO);
        oddsDO.setRiskLimit(new BigDecimal("100000"));
        oddsDO.setOddsValue(new BigDecimal("2.0"));
        oddsDO.setResultType(MatchResult.MS1);

        when(oddsCalculationService.calculatePayout(eq(sampleBetDO))).thenReturn(new BigDecimal("20"));
        when(matchOddService.findAllMatchOddsByMatchId(eq(1L))).thenReturn(List.of(oddsDO));
        when(matchOddService.findAllMatchOddsByMatchIdAndResult(eq(1L), eq(MatchResult.MS1))).thenReturn(oddsDO);
        when(matchService.getMatchById(eq(1L))).thenReturn(MatchDO.builder().id(1L).build());

        doNothing().when(cacheEvictService).evictMatchesList();

        Bet persisted = new Bet();
        when(betMapper.toEntity(any(BetDO.class))).thenReturn(persisted);
        when(betRepository.save(any(Bet.class))).thenAnswer(invocation -> {
            Bet b = invocation.getArgument(0);
            b.setId(123L);
            return b;
        });
        BetDO expectedResponse = new BetDO();
        when(betMapper.toDO(any(Bet.class))).thenReturn(expectedResponse);

        BetDO actual = betService.placeBet(sampleBetDO);

        assertThat(actual).isSameAs(expectedResponse);
        verify(oddsCalculationService).calculatePayout(eq(sampleBetDO));
        verify(matchOddService).findAllMatchOddsByMatchId(eq(1L));
        verify(matchOddService).updateMatchOdds(any(MatchOddsDO.class));
        verify(betRepository).save(any(Bet.class));
        verify(cacheEvictService).evictMatchesList();
        verify(placeBetCounter).increment();
    }
}
