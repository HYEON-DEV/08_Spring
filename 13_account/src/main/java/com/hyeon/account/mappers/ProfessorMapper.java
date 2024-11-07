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

import com.hyeon.account.models.Professor;

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

    /**
     * 특정 학과에 소속된 교수를 일괄 삭제한다
     * @param input - 삭제할 교수 정보에 대한 모델 객체
     * @return 삭제된 데이터 수
     */
    @Delete("DELETE FROM professor WHERE deptno=#{deptno}")
    int deleteByDeptno(Professor input);
    

    /**
     * 단일행 조회를 수행하는 메서드 정의
     * @param input - 조회할 데이터에 대한 모델 객체
     * @return 조회된 데이터
     */
    @Select("SELECT " +
        "profno, name, userid, position, sal, " +
        "DATE_FORMAT(hiredate, '%Y-%m-%d') AS hiredate, comm, " + 
        "p.deptno AS deptno, dname " + 
        "FROM professor p " + 
        "INNER JOIN department d ON p.deptno = d.deptno " +
        "WHERE profno=#{profno}")
    @Results(id="professorMap", value={
        @Result(property="profno", column="profno"),
        @Result(property="name", column="name"),
        @Result(property="userid", column="userid"),
        @Result(property="position", column="position"),
        @Result(property="sal", column="sal"),
        @Result(property="hiredate", column="hiredate"),
        @Result(property="comm", column="comm"),
        @Result(property="deptno", column="deptno"),
        @Result(property="dname", column="dname") })
    Professor selectItem(Professor input);

    /**
     * 다중행 조회를 수행하는 메서드 정의
     * 소속된 학과 이름을 포함한다
     * @param input - 조회할 데이터에 대한 모델 객체
     * @return 조회된 데이터
     */
    @Select( "<script> " +
        "SELECT " +
        "profno, name, userid, position, sal, " +
        "DATE_FORMAT(hiredate, '%Y-%m-%d') AS hiredate, comm, " + 
        "p.deptno AS deptno, dname " +
        "FROM professor p " +
        "INNER JOIN department d ON p.deptno = d.deptno " +
        "<where> " +
        "<if test='deptno != 0'> p.deptno = #{deptno} </if>" +
        "<if test = 'name != null'> name LIKE concat('%', #{name}, '%') </if> " +
        "<if test = 'userid != null'> OR userid LIKE concat('%', #{userid}, '%') </if> " +
        "</where> " +
        "ORDER BY profno DESC " +
        "<if test = 'listCount > 0'> LIMIT #{offset}, #{listCount} </if> " +
        "</script>" )
    @ResultMap("professorMap")
    List<Professor> selectList(Professor input);

    /**
     * 검색 결과의 수를 조회하는 메서드
     * 목록 조회와 동일한 검색 조건을 적용해야 한다
     * @param input - 조회 조건을 담고 있는 객체
     * @return 조회 결과 수
     */
    @Select( "<script> " + 
        "SELECT COUNT(*) AS cnt " + 
        "FROM professor p " +
        "INNER JOIN department d ON p.deptno = d.deptno " +
        "<where> " + 
        "<if test = 'name != null'> name LIKE concat('%', #{name}, '%') </if> " +
        "<if test = 'userid != null'> OR userid LIKE concat('%', #{userid}, '%') </if> " +
        "</where> " +
        "</script>" )
    public int selectCount(Professor input);

}
