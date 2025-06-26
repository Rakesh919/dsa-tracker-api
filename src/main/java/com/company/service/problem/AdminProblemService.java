package com.company.service.problem;

import com.company.entity.problems.AdminProblems;
import com.company.repository.problem.AdminProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminProblemService {

    @Autowired
    private AdminProblemRepository adminProblemRepository;

    public AdminProblems addProblem(AdminProblems dto){
        try{
            return adminProblemRepository.save(dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<AdminProblems> getAllProblems(){
        try{
            return adminProblemRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AdminProblems getProblemById(int id){
        try{
            return adminProblemRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteProblemById(int id){
        try{
            adminProblemRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
