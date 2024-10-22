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

import com.hyeon.database.models.Professor;

@Mapper
public interface ProfessorMapper {
    
    /**
     * 교수 정보를 입력한다
     * PK값은 파라미터로 전달된 input 객체에 참조로 처리된다
     * @param input - 입력할 교수 정보에 대한 모델 객체
     * @return 입력된 데이터 수
     */
    @Insert("INSERT INTO professor (" + 
        "name, userid, position, sal, hiredate, comm, deptno) " + 
        "VALUES (#{name}, #{userid}, #{position}, #{sal}, #{hiredate}, #{comm}, #{deptno})")
    /**
     *  INSERT문에서 필요한 PK에 대한 옵션 정의
     * => useGeneratedKeys: AUTO_INCREMENT가 적용된 테이블인 경우 사용
     * => KeyProperty: 파라미터로 전달되는 MODEL 객체에서 PK에 대응되는 멤버변수
     * => keyColumn: 테이블의 Primary Key 컬럼명
     */
    @Options(useGeneratedKeys = true, keyProperty = "profno", keyColumn = "profno")
    int insert(Professor input);


    /**
     * UPDATE문을 수행하는 메서드 정의
     * @param input - 수정할 데이터에 대한 모델 객체
     * @return 수정된 데이터 수
     */
    @Update("UPDATE professor SET name=#{name}, userid=#{userid}, position=#{position}, sal=#{sal}, hiredate=#{hiredate}, comm=#{comm}, deptno=#{deptno} WHERE profno=#{profno}")
    int update(Professor input);


    /**
     * DELETE문을 수행하는 메서드 정의
     * @param input - 삭제할 데이터에 대한 모델 객체
     * @return 
     */
    @Delete("DELETE FROM professor WHERE profno = #{profno}")
    int delete(Professor input);

    @Delete("DELETE FROM professor WHERE deptno=#{deptno}")
    int deleteByDeptno(Professor input);
    

    @Select("SELECT profno, name, userid, position, sal, hiredate, comm, deptno FROM professor WHERE profno=#{profno}")
    /**
     * 조회 결과와 리턴할 MODEL 객체를 연결하기 위한 규칙 정의
     * => property : MODEL 객체의 멤버변수 이름
     * => column : SELECT문에 명시된 필드 이름 (AS옵션 사용한 경우 별칭으로 명사)
     * import org.apache.ibatis.annotations.Result;
     * import org.apache.ibatis.annotations.Results;
     */
    @Results(id="professorMap", value={
        @Result(property="profno", column="profno"),
        @Result(property="name", column="name"),
        @Result(property="userid", column="userid"),
        @Result(property="position", column="position"),
        @Result(property="sal", column="sal"),
        @Result(property="hiredate", column="hiredate"),
        @Result(property="comm", column="comm"),
        @Result(property="deptno", column="deptno")
    })
    Professor selectItem(Professor input);

    
    @Select("SELECT profno, name, userid, position, sal, hiredate, comm, deptno FROM professor")
    // 조회 결과와 MODEL의 맵핑이 이전 규칙과 동일한 경우 id값으로 이전 규칙 재사용
    // @Results 에 id 를 설정하면 다른 조회 메서드에서도 설정한 id 를 통해 @Results를 재사용할 수 있다.
    @ResultMap("professorMap")
    List<Professor> selectList(Professor input);

}
