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

import com.hyeon.database.models.Student;

@Mapper
public interface StudentMapper {
    
    /**
     * 학생 정보를 입력한다
     * PK값은 파라미터로 전달된 input 객체에 참조로 처리된다
     * @param input - 입력할 학생 정보에 대한 모델 객체
     * @return 입력된 데이터 수
     */
    @Insert("INSERT INTO student (name, userid, grade, idnum, birthdate, tel, height, weight, deptno, profno) VALUES (#{name}, #{userid}, #{grade}, #{idnum}, #{birthdate}, #{tel}, #{height}, #{weight}, #{deptno}, #{profno})")
    /**
     *  INSERT문에서 필요한 PK에 대한 옵션 정의
     * => useGeneratedKeys: AUTO_INCREMENT가 적용된 테이블인 경우 사용
     * => KeyProperty: 파라미터로 전달되는 MODEL 객체에서 PK에 대응되는 멤버변수
     * => keyColumn: 테이블의 Primary Key 컬럼명
     */
    @Options(useGeneratedKeys = true, keyProperty = "studno", keyColumn = "studno")
    int insert(Student input);


    /**
     * UPDATE문을 수행하는 메서드 정의
     * @param input - 수정할 데이터에 대한 모델 객체
     * @return 수정된 데이터 수
     */
    @Update("UPDATE student SET name=#{name}, userid=#{userid}, grade=#{grade}, idnum=#{idnum}, birthdate=#{birthdate}, tel=#{tel}, height=#{height}, weight=#{weight}, height=#{height}, profno=#{profno} WHERE studno=#{studno}")
    int update(Student input);


    /**
     * DELETE문을 수행하는 메서드 정의
     * @param input - 삭제할 데이터에 대한 모델 객체
     * @return 
     */
    @Delete("DELETE FROM student WHERE studno = #{studno}")
    int delete(Student input);

    // 학과를 삭제하기 전에 학과에 소속된 학생 데이터 삭제
    @Delete("DELETE FROM student WHERE deptno=#{deptno}")
    int deleteByDeptno(Student input);

    // 교수를 삭제하기 전에 교수에게 소속된 학생들과의 연결 해제
    // => profno 컬럼이 null 허용으로 설정돼야 함
    @Update("UPDATE student SET profno=null WHERE profno=#{profno}")
    int updateByProfno(Student input);


    @Select("SELECT studno, name, userid, grade, idnum, birthdate, tel, height, weight, height, profno FROM student WHERE studno=#{studno}")
    /**
     * 조회 결과와 리턴할 MODEL 객체를 연결하기 위한 규칙 정의
     * => property : MODEL 객체의 멤버변수 이름
     * => column : SELECT문에 명시된 필드 이름 (AS옵션 사용한 경우 별칭으로 명사)
     * import org.apache.ibatis.annotations.Result;
     * import org.apache.ibatis.annotations.Results;
     */
    @Results(id="studentMap", value={
        @Result(property="studno", column="studno"),
        @Result(property="name", column="name"),
        @Result(property="userid", column="userid"),
        @Result(property="grade", column="grade"),
        @Result(property="idnum", column="idnum"),
        @Result(property="birtdate", column="birtdate"),
        @Result(property="tel", column="tel"),
        @Result(property="height", column="height"),
        @Result(property="weight", column="weight"),
        @Result(property="deptno", column="deptno"),
        @Result(property="profno", column="profno")
    })
    Student selectItem(Student input);

    
    @Select("SELECT studno, name, userid, grade, idnum, DATE_FORMAT(birthdate, '%Y-%m-%d') AS birthdate, tel, height, weight, deptno, profno FROM student")
    // 조회 결과와 MODEL의 맵핑이 이전 규칙과 동일한 경우 id값으로 이전 규칙 재사용
    // @Results 에 id 를 설정하면 다른 조회 메서드에서도 설정한 id 를 통해 @Results를 재사용할 수 있다.
    @ResultMap("studentMap")
    List<Student> selectList(Student input);

}
