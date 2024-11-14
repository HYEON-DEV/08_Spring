package com.hyeon.account.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;

import com.hyeon.account.models.Member;

@Mapper
public interface MemberMapper {
    
    /**
     * 회원 정보를 입력한다
     * PK값은 파라미터로 전달된 input 객체에 참조로 처리된다
     * @param input - 입력할 회원 정보에 대한 모델 객체
     * @return 입력된 데이터 수
     */
    @Insert("INSERT INTO members (user_id, user_pw, user_name, email, phone, birthday, gender, postcode, addr1, addr2, photo, is_out, is_admin, login_date, reg_date, edit_date) VALUES (#{userId}, MD5(#{userPw}), #{userName}, #{email}, #{phone}, #{birthday}, #{gender}, #{postcode}, #{addr1}, #{addr2}, #{photo}, 'N', 'N', null, now(), now())")
    /**
     *  INSERT문에서 필요한 PK에 대한 옵션 정의
     * => useGeneratedKeys: AUTO_INCREMENT가 적용된 테이블인 경우 사용
     * => KeyProperty: 파라미터로 전달되는 MODEL 객체에서 PK에 대응되는 멤버변수
     * => keyColumn: 테이블의 Primary Key 컬럼명
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int insert(Member input);


    /**
     * UPDATE문을 수행하는 메서드 정의
     * @param input - 수정할 데이터에 대한 모델 객체
     * @return 수정된 데이터 수
     */
    @Update("<script> " +
        "UPDATE members \n" + 
        "SET user_name=#{userName}, \n" +
        "<if test='newUserPw != null and newUserPw != \"\"'> user_pw = MD5(#{newUserPw}), </if> \n"+
        "email=#{email}, phone=#{phone}, \n" +
        "birthday=#{birthday}, gender=#{gender}, postcode=#{postcode}, addr1=#{addr1}, addr2=#{addr2}, \n" + 
        //"photo=#{photo}, \n" +
        "edit_date=NOW() \n" + 
        "WHERE id=#{id} AND user_pw = MD5(#{userPw}) \n" +
        "</script>")
    public int update(Member input);


    /**
     * DELETE문을 수행하는 메서드 정의
     * @param input - 삭제할 데이터에 대한 모델 객체
     * @return 
     */
    @Delete("DELETE FROM members WHERE id = #{id}")
    public int delete(Member input);


    @Select("SELECT id, user_id, user_pw, user_name, email, phone, DATE_FORMAT(birthday,'%Y-%m-%d') AS birthday, gender, postcode, addr1, addr2, photo, is_out, is_admin, login_date, reg_date, edit_date FROM members WHERE id=#{id}")
    /**
     * 조회 결과와 리턴할 MODEL 객체를 연결하기 위한 규칙 정의
     * => property : MODEL 객체의 멤버변수 이름
     * => column : SELECT문에 명시된 필드 이름 (AS옵션 사용한 경우 별칭으로 명사)
     * import org.apache.ibatis.annotations.Result;
     * import org.apache.ibatis.annotations.Results;
     */
    @Results(id="membersMap", value={
        @Result(property="id", column="id"),
        @Result(property="userId", column="user_id"),
        @Result(property="userPw", column="user_pw"),
        @Result(property="userName", column="user_name"),
        @Result(property="email", column="email"),
        @Result(property="phone", column="phone"),
        @Result(property="birthday", column="birthday"),
        @Result(property="gender", column="gender"),
        @Result(property="postcode", column="postcode"),
        @Result(property="addr1", column="addr1"),
        @Result(property="addr2", column="addr2"),
        @Result(property="photo", column="photo"),
        @Result(property="isOut", column="is_out"),
        @Result(property="isAdmin", column="is_admin"),
        @Result(property="loginDate", column="login_date"),
        @Result(property="regDate", column="reg_date"),
        @Result(property="editDate", column="edit_date")
    })
    public Member selectItem(Member input);

    
    @Select("SELECT id, user_id, user_pw, user_name, email, phone, DATE_FORMAT(birthday, '%Y-%m-%d') AS birthday, gender, postcode, addr1, addr2, photo, is_out, is_admin, login_date, reg_date, edit_date FROM members")
    // 조회 결과와 MODEL의 맵핑이 이전 규칙과 동일한 경우 id값으로 이전 규칙 재사용
    // @Results 에 id 를 설정하면 다른 조회 메서드에서도 설정한 id 를 통해 @Results를 재사용할 수 있다.
    @ResultMap("membersMap")
    public List<Member> selectList(Member input);


    @Select("<script>" +
            "SELECT COUNT(*) FROM members " +
            "<where> " +
            "<if test='userId != null'> user_id = #{userId} </if> " +
            "<if test='email != null'> email = #{email} </if> " +
            "<if test='id != 0'> AND id != #{id} </if> " +
            "</where> " +
            "</script>")
    public int selectCount(Member input);



    /**
     * 아이디 찾기
     */
    @Select(
        "SELECT user_id FROM members " +
        "WHERE user_name = #{userName} AND email = #{email} AND is_out='N'" 
    )
    @ResultMap("membersMap")
    public Member findId(Member input);


    /**
     * 비밀번호 수정
     */
    @Update("UPDATE members SET user_pw = MD5(#{userPw})" + 
            "WHERE user_id = #{userId} AND email = #{email} AND is_out='N'")
    public int resetPw(Member input);


    /**
     * 로그인
     */
    @Select(
        "SELECT " +
        "id, user_id, user_pw, user_name, email, phone, " +
        "birthday, gender, postcode, addr1, addr2, photo, " +
        "is_out, is_admin, login_date, reg_date, edit_date " +
        "FROM members " +
        "WHERE user_id=#{userId} AND user_pw=MD5(#{userPw}) AND is_out='N'")
    @ResultMap("membersMap")
    public Member login(Member input);


    /**
     * 로그인 날짜 업데이트
     */
    @Update("UPDATE members SET login_date=NOW() WHERE id=#{id} AND is_out='N'")
    public int updateLoginDate(Member input);


    /**
     * 회원 탈퇴
     */
    @Update("UPDATE members \n" + 
    "SET is_out='Y', edit_date=NOW() \n" +
    "WHERE id=#{id} AND user_pw=MD5(#{userPw}) AND is_out='N'")
    public int out(Member input);
    /* 
    UPDATE members SET is_out='N', edit_date=NOW() 
    WHERE id=13 AND user_pw=MD5('1234qwer');
    */


    /**
     * 탈퇴하고 특정 시간이 지난 회원의 photo 조회
     */
    @Select("SELECT photo FROM members \n" +
            "WHERE is_out='Y' AND \n" +
                "edit_date < DATE_ADD( NOW(), interval -30 second ) AND \n" +
                "photo IS NOT NULL")
    @ResultMap("membersMap")
    public List<Member> selectOutMembersPhoto();

    /*
    UPDATE members SET is_out='Y', edit_date=NOW() WHERE id=11;
     */

    /**
     * 탈퇴한 회원 삭제
     */
    @Delete ("DELETE FROM members \n" +
        "WHERE is_out='Y' AND \n" +
        "edit_date < DATE_ADD( NOW(), interval -30 second )")
    public int deleteOutMembers();
}
