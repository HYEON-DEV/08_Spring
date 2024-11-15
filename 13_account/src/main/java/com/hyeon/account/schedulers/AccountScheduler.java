package com.hyeon.account.schedulers;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hyeon.account.helpers.FileHelper;
import com.hyeon.account.models.Member;
import com.hyeon.account.services.MemberService;

import lombok.extern.slf4j.Slf4j;

/**
 * 스케줄러 샘플 클래스
 * 각 메서드가 정해진 스케줄에 따라 자동 실행된다
 * 프로그램명Application.java 파일의 상단에 @EnableScheduling이 추가돼야 한다
 */
@Slf4j
@Component
@EnableAsync
public class AccountScheduler {
    
    // 업로드 된 파일이 저장될 경로 (application.preperties로부터 읽어온다)
    @Value("${upload.dir}")
    private String uploadDir;

    @Autowired
    private MemberService memberService;

    @Autowired
    private FileHelper fileHelper;


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
    //@Scheduled(cron = "0 0 4 * * ?") // 매일 오전 4시에 자동 실행
    //@Scheduled(cron = "10 * * * * ?") // 매 분마다 10초에 실행
    //@Scheduled(cron = "0 0/30 * * * ?") // 매 0분, 30분마다 실행
    public void processOutMembers() throws InterruptedException {
        log.debug("탈퇴 회원 정리 시작");
        
        List<Member> outMembers = null;

        try {
            log.debug("탈퇴 회원 조회 및 삭제");
            outMembers = memberService.processOutMembers();
        } catch (Exception e) {
            log.error("탈퇴 회원 조회 및 삭제 실패", e);
            return;
        }

        if (outMembers == null || outMembers.size()==0) {
            log.debug("탈퇴 대상 없음");
            return;
        }

        for ( int i=0; i<outMembers.size(); i++) {      
            Member m = outMembers.get(i);
            // 사용자가 업로드한 프로필 사진의 실제 경로
            fileHelper.deleteUploadFile(m.getPhoto());
        }
    }
}
