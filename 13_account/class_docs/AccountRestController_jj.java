package kr.jinju.myshop.controllers.apis;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.jinju.myshop.helpers.FileHelper;
import kr.jinju.myshop.helpers.Mailhelper;
import kr.jinju.myshop.helpers.RestHelper;
import kr.jinju.myshop.helpers.UtilHelper;
import kr.jinju.myshop.models.Member;
import kr.jinju.myshop.models.UploadItem;
import kr.jinju.myshop.services.MemberService;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
public class AccountRestController {
    @Autowired
    private RestHelper restHelper;

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private UtilHelper utilHelper;

    @Autowired
    private Mailhelper mailhelper;

    /**
     * 아이디 중복 검사
     * @param userId
     * @return
     */
    @GetMapping("/api/account/id_unique_check")
    public Map<String, Object> idUniqueCheck(@RequestParam("user_id") String userId) {
        try {
            memberService.isUniqueUserId(userId);
        } catch (Exception e) {
            return restHelper.badRequest(e);
        }
        
        return restHelper.sendJson();
    }

    /**
     * 이메일 중복 검사
     * @param email
     * @return
     */
    @GetMapping("/api/account/email_unique_check")
    public Map<String, Object> EmailUniqueCheck(
        @SessionAttribute(value = "memberInfo", required = false) Member memberInfo,
        @RequestParam("email") String email) {

        Member input = new Member();
        input.setEmail(email);

        // 로그인 중이라면 현재 회원의 일련번호를 함께 전달한다.
        if (memberInfo != null) {
            input.setId(memberInfo.getId());
            
        }
        
        try {
            memberService.isUniqueEmail(input);
        } catch (Exception e) {
            return restHelper.badRequest(e);
        }
        
        return restHelper.sendJson();
    }

    @PostMapping("/api/account/join")
    public Map<String, Object> join(
        @RequestParam("user_id") String userId,
        @RequestParam("user_pw") String userPw,
        @RequestParam("user_name") String userName,
        @RequestParam("email") String email,
        @RequestParam("phone") String phone,
        @RequestParam("birthday") String birthday,
        @RequestParam("gender") String gender,
        @RequestParam("postcode") String postcode,
        @RequestParam("addr1") String addr1,
        @RequestParam("addr2") String addr2,
        @RequestParam(value = "photo", required = false) MultipartFile photo
    ) {

        // 1) 유효성 검사 생략

        // 2) 아이디 중복 검사

        try {
            memberService.isUniqueUserId(userId);
        } catch (Exception e) {
            return restHelper.badRequest(e);
        }

        // 3) 이메일 중복 검사
        Member input = new Member();
        input.setEmail(email);

        try {
            memberService.isUniqueEmail(input);
        } catch (Exception e) {
            return restHelper.badRequest(e);
        }

        // 4) 업로드 받기
        UploadItem uploadItem = null;
        
        try {
            uploadItem = fileHelper.saveMultipartFile(photo);
        } catch (NullPointerException e) {
            // 업로드 된 항목이 있는 경우는 에러가 아니므로 계속 진행
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Member member = new Member();
        member.setUserId(userId);
        member.setUserPw(userPw);
        member.setUserName(userName);
        member.setEmail(email);
        member.setPhone(phone);
        member.setBirthday(birthday);
        member.setGender(gender);
        member.setPostcode(postcode);
        member.setAddr1(addr1);
        member.setAddr2(addr2);

        // 업로드 된 이미지의 이름을 표시할 필요가 없다면 저장된 경로만 DB에 저장하면 됨
        if (uploadItem != null) {
            member.setPhoto(uploadItem.getFilePath());
        }

        try {
            memberService.addItem(member);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        return restHelper.sendJson();
    }

    @PostMapping("/api/account/find_id")
    public Map<String, Object> findId(
        @RequestParam("user_name") String userName,
        @RequestParam("email") String email) {
        
        Member input = new Member();
        input.setUserName(userName);
        input.setEmail(email);

        Member output = null;

        try {
            output = memberService.findId(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("item", output.getUserId());

        return restHelper.sendJson(data);
    }

    @PutMapping("/api/account/reset_pw")
    public Map<String, Object> resetPw(
        @RequestParam("user_id") String userId,
        @RequestParam("email") String email) {
        
        /** 1) 임시 비밀번호 DB에 갱신하기 */
        String newPassword = utilHelper.randomPassword(8);
        Member input = new Member();
        input.setUserId(userId);
        input.setEmail(email);
        input.setUserPw(newPassword);

        try {
            memberService.resetPw(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        /** 2) 이메일 발송을 위한 템플릿 처리 */
        // 메일 템플릿 파일 경로
        ClassPathResource resource = new ClassPathResource("mail_templates/reset_pw.html");
        String mailTemplatePath = null;
        try {
            mailTemplatePath = resource.getFile().getAbsolutePath();
        } catch (IOException e) {
            return restHelper.serverError("메일 템플릿을 찾을 수 없습니다.");
        }

        // 메일 템플릿 파일 가져오기.
        String template = null;
        try {
            template = fileHelper.readString(mailTemplatePath);
        } catch (Exception e) {
            return restHelper.serverError("메일 템플릿을 읽을 수 없습니다.");
        }

        // 메일 템플릿 안의 치환자 처리
        template = template.replace("{{userId}}", userId);
        template = template.replace("{{password}}", newPassword);

        /** 3) 메일 발송 */
        String subject = userId + "님의 비밀번호가 재성정 되었습니다.";
        
        try {
            mailhelper.sendMail(email, subject, template);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }
        
        return restHelper.sendJson();
    }

    @PostMapping("/api/account/login")
    public Map<String, Object> login(
        // 세션을 사용해야 하므로 request 객체가 필요하다.
        HttpServletRequest request,
        @RequestParam("user_id") String user_id,
        @RequestParam("user_pw") String user_pw) {
        
        /** 1) 입력값에 대한 유효성 검사 */
        // 여기서는 생략

        /** 2) 입력값을 Beans 객체에 저장 */
        Member input = new Member();
        input.setUserId(user_id);
        input.setUserPw(user_pw);

        /** 3) 로그인 시도 */
        Member output = null;

        try {
            output = memberService.login(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        // 프로필 사진의 경로를 URL로 변환
        output.setPhoto(fileHelper.getUrl(output.getPhoto()));
        
        /** 4) 로그인에 성공했다면 회원 정보를 세션에 저장한다. */
        HttpSession session = request.getSession();
        session.setAttribute("memberInfo", output);

        /** 5) 로그인이 처리되었음을 응답한다. */
        return restHelper.sendJson();
    }
    
    @GetMapping("/api/account/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return restHelper.sendJson();
    }
    
    @DeleteMapping("/api/account/out")
    public Map<String, Object> out(
        HttpServletRequest request,
        @SessionAttribute("memberInfo") Member memberInfo,
        @RequestParam("password") String password) {

        // 세션으로부터 추출한 Member객체에 입력받은 비밀번호를 넣어준다.
        memberInfo.setUserPw(password);

        try {
            memberService.out(memberInfo);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        // 로그아웃을 위해 세션을 삭제한다.
        HttpSession session = request.getSession();
        session.invalidate();

        return restHelper.sendJson();
    }

    @PutMapping("/api/account/edit")
    public Map<String, Object> edit(
        HttpServletRequest request,                         // 세션 갱신용
        @SessionAttribute("memberInfo") Member memberInfo,  // 현재 세션 정보 확인용
        @RequestParam("user_pw") String userPw,             // 현재 비밀번호 (정보 확인용)
        @RequestParam("new_user_pw") String newUserPw,      // 신규 비밀번호 (정보 변경용)
        @RequestParam("user_name") String userName,
        @RequestParam("email") String email,
        @RequestParam("phone") String phone,
        @RequestParam("birthday") String birthday,
        @RequestParam("gender") String gender,
        @RequestParam("postcode") String postcode,
        @RequestParam("addr1") String addr1,
        @RequestParam("addr2") String addr2,
        @RequestParam(value = "delete_photo", defaultValue = "N") String deletePhoto,
        @RequestParam(value = "photo", required = false) MultipartFile photo) {

        /** 1) 유효성 검사 생략 */

        /** 2) 이메일 중복 검사 */
        Member input = new Member();
        input.setEmail(email);
        input.setId(memberInfo.getId());

        try {
            memberService.isUniqueEmail(input);
        } catch (Exception e) {
            return restHelper.badRequest(e);
        }

        /** 3) 업로드 처리 */
        UploadItem uploadItem = null;

        try {
            uploadItem = fileHelper.saveMultipartFile(photo);
        } catch (NullPointerException e) {
            // 업로드 된 항목이 있는 경우는 에러가 아니므로 계속
        } catch (Exception e) {
            // 업로드 된 항목이 있으나, 이를 처리하다가 에러가 발생한 경우
            return restHelper.serverError(e);
        }


        /** 4) 정보를 Service에 전달하기 위한 객체 구성 */
        // 아이디는 수정할 필요가 없으므로 설정하지 않는다.
        Member member = new Member();
        member.setId(memberInfo.getId());   // 증요: WHERE절에 사용할 PK설정
        member.setUserPw(userPw);           // 현재 비밀번호 -> WHERE절 사용
        member.setNewUserPw(newUserPw);     // 신규 비밀번호 -> 값이 있을 경우만 갱신
        member.setUserName(userName);
        member.setEmail(email);
        member.setPhone(phone);
        member.setBirthday(birthday);
        member.setGender(gender);
        member.setPostcode(postcode);
        member.setAddr1(addr1);
        member.setAddr2(addr2);

        // 현재 프로필 사진값을 가져온다.
        String currentPhoto = memberInfo.getPhoto();

        // 현재 프로필 사진이 있는 경우
        if (currentPhoto != null && !currentPhoto.equals("")) {
            // 로그인 시에 DB에서 가져온 이미지 파일 경로에 "/files"를 붙여 놨으므로,
            // 이 값을 제거해야 한다.
            currentPhoto = fileHelper.getFilePath(currentPhoto);

            // 기존 사진의 삭제가 요청되었다면?
            if (deletePhoto.equals("Y")) {
                fileHelper.deleteUploadFile(currentPhoto);

                // 업로드 된 사진이 있다면 Beans에 포함한다.
                // 기존 파일이 있을 경우에는 삭제없이는 정보를 갱신하면 안된다.
                if (uploadItem != null) {
                    member.setPhoto(uploadItem.getFilePath());
                } else {
                    // 삭제만 하고 새로운 파일은 업로드 하지 않은 경우
                    // -> Member 클래스에서 photo는 String
                    // -> 기본값이 null이란 으미ㅣ
                    // -> 별도로 처리하지 않는 한 member객체의 photo는 null이란 의미
                    member.setPhoto(null);
                }
            } else {
                // 삭제 요청이 없는 경우는 세션의 사진 경로(=기존 정보)를 기대로 적용하여
                // 기존 사진을 유지하도록 한다.
                member.setPhoto(currentPhoto);
            }
        } else {
            // 업로드 된 사진이 있다면 Beans에 포함한다.
            if (uploadItem != null) {
                member.setPhoto(uploadItem.getFilePath());
            }
        }

        /** 5) DB에 저장 */
        Member output = null;

        try {
            output = memberService.editItem(member);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        // 프로필 사진의 경로를 URL로 변환
        output.setPhoto(fileHelper.getUrl(output.getPhoto()));

        /** 6) 변경된 정보로 세션 갱신 */
        request.getSession().setAttribute("memberInfo", output);

        return restHelper.sendJson();
    }
}
