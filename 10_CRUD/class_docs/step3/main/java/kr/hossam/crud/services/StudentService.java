package kr.hossam.crud.services;

import java.util.List;

import kr.hossam.crud.models.Student;

public interface StudentService {
    public Student addItem(Student params) throws Exception;

    public Student editItem(Student params) throws Exception;

    public int deleteItem(Student params) throws Exception;

    public Student getItem(Student params) throws Exception;

    public List<Student> getList(Student params) throws Exception;

    public int getCount(Student params) throws Exception;
}
