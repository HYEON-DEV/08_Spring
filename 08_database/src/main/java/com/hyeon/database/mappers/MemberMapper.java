package com.hyeon.database.mappers;

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

import com.hyeon.database.models.Member;

@Mapper
public interface MemberMapper {
    
    /**
     * 회원 정보를 입력한다
     * PK값은 파라미터로 전달된 input 객체에 참조로 처리된다
     * @param input - 입력할 회원 정보에 대한 모델 객체
     * @return 입력된 데이터 수
     */
    @Insert("INSERT INTO members (user_id, user_pw, user_name, email, phone, birthday, gender, postcode, addr1, addr2, is_out, is_admin, login_date, reg_date, edit_date) VALUES (#{userId}, MD5(#{userPw}), #{userName}, #{email}, #{phone}, #{birthday}, #{gender}, #{postcode}, #{addr1}, #{addr2}, 'N', 'N', null, now(), now())")
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
    @Update("UPDATE members SET user_id=#{userId}, user_pw=MD5(#{userPw}), user_name=#{userName}, email=#{email}, phone=#{phone}, birthday=#{birthday}, gender=#{gender}, postcode=#{postcode}, addr1=#{addr1}, addr2=#{addr2}, is_out=#{isOut}, is_admin=#{isAdmin}, login_date=#{loginDate}, reg_date=#{regDate}, edit_date=#{editDate} WHERE id=#{id}")
    public int update(Member input);


    /**
     * DELETE문을 수행하는 메서드 정의
     * @param input - 삭제할 데이터에 대한 모델 객체
     * @return 
     */
    @Delete("DELETE FROM members WHERE id = #{id}")
    public int delete(Member input);


    @Select("SELECT id, user_id, user_pw, user_name, email, phone, birthday, gender, postcode, addr1, addr2, is_out, is_admin, login_date, reg_date, edit_date FROM members WHERE id=#{id}")
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
        @Result(property="phone", column="phoen"),
        @Result(property="birthday", column="birthday"),
        @Result(property="gender", column="gender"),
        @Result(property="postcode", column="postcode"),
        @Result(property="addr1", column="addr1"),
        @Result(property="addr2", column="addr2"),
        @Result(property="isOut", column="is_out"),
        @Result(property="isAdmin", column="is_admin"),
        @Result(property="loginDate", column="login_date"),
        @Result(property="regDate", column="reg_date"),
        @Result(property="editDate", column="edit_date")
    })
    public Member selectItem(Member input);

    
    @Select("SELECT id, user_id, user_pw, user_name, email, phone, DATE_FORMAT(birthday, '%Y-%m-%d') AS birthday, gender, postcode, addr1, addr2, is_out, is_admin, login_date, reg_date, edit_date FROM members")
    // 조회 결과와 MODEL의 맵핑이 이전 규칙과 동일한 경우 id값으로 이전 규칙 재사용
    // @Results 에 id 를 설정하면 다른 조회 메서드에서도 설정한 id 를 통해 @Results를 재사용할 수 있다.
    @ResultMap("membersMap")
    public List<Member> selectList(Member input);

}
