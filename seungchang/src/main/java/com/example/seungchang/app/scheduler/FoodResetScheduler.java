package com.example.seungchang.app.scheduler;

import com.example.seungchang.app.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FoodResetScheduler {

    private final FoodRepository foodRepository;

    // 매일 자정(00:00)에 실행
    @Transactional
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void resetDailySales() {
        log.info("하루 판매량 초기화 작업 시작");

        foodRepository.findAll().forEach(food -> {
            food.resetSales();
        });
        foodRepository.flush();
        log.info("하루 판매량 초기화 완료");
    }
}

