package com.hyeon.scheduler.schedulers;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 스케줄러 샘플 클래스
 * 각 메서드가 정해진 스케줄에 따라 자동 실행된다
 * 프로그램명Application.java 파일의 상단에 @EnableScheduling이 추가돼야 한다
 */
@Component
@EnableAsync
public class SchedulerDemo {
    
    /**
     * 해당 메서드가 "끝나는" 시간 기준, 지정된 milliseconds 간격으로 실행
     * 하나의 인스턴스만 항상 실행되도록 해야 할 상황에서 유용
     */
    @Scheduled(fixedDelay = 1000)
    public void sample1() throws InterruptedException {
        System.out.println("[sample1 - fixed delay] 시작" + LocalDateTime.now());
        Thread.sleep(3000);
        System.out.println("[sample1 - fixed delay] 끝" + LocalDateTime.now());
    }


    /**
     * 해당 메서드가 "시작하는" 시간 기준, 지정된 milliseconds 간격으로 실행
     * 모든 실행이 독립적인 경우에 유용
     * 메서드의 수행시간이 지정된 간격보다 긴 경우 메서드가 중복 실행(병렬)될 수 있음
     * 병렬로 scheduler 사용할 경우, Class에 @EnableAsync, Method 에 @Async 추가
     */
    @Async
    @Scheduled(fixedRate = 1000)
    public void sample2() throws InterruptedException {
        System.out.println("[sample2 - fixed rate] 시작" + LocalDateTime.now());
        Thread.sleep(3000);
        System.out.println("[sample2 - fixed rate] 끝" + LocalDateTime.now());
    }


    /**
     * initialDelay 값 이후 처음 실행 되고, fixedDelay 값에 따라 계속 실행
     */
    @Scheduled(initialDelay = 5000, fixedDelay = 1000)
    public void sample3 () throws InterruptedException {
        System.out.println("[sampe3 - initial delay] 시작" + LocalDateTime.now());
        Thread.sleep(3000);
        System.out.println("[sampe3 - initial delay] 끝" + LocalDateTime.now());
    }


    /**
     * 지정된 스케줄에 따라 실행
     *  => 초 분 시 일 월 ?
     * 
     * 1초마다 실행되는 작업    : * * * * * ?
     * 매분 0초에 실행되는 작업 : 0 * * * * ?
     * 매분 10초마다 실행       : 10 * * * * ? 1시0분0초, 1시1분10초, 1시2분10초......
     * 매 10초마다 실행         : 0/10 * * * *? 1시0분0초, 1시0분10초, 1시0분20초......
     * 매시 정각에 실행되는 작업 : 0 0 * * * ?
     * 매일 자정에 실행되는 작업 : 0 0 0 * * ?
     * http://www.cronmaker.com/
     */
    @Scheduled(cron = "0 08 * * * ?")
    public void sample4() throws InterruptedException {
        System.out.println("[sample4 - cron] 시작" + LocalDateTime.now());
        Thread.sleep(3000);
        System.out.println("[sample4 - cron] 끝" + LocalDateTime.now());
    }
}
