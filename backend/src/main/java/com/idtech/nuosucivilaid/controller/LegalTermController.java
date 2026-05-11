package com.idtech.nuosucivilaid.controller;

import com.idtech.nuosucivilaid.entity.LegalTerm;
import com.idtech.nuosucivilaid.repository.LegalTermRepository;
import com.idtech.nuosucivilaid.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/civil/term")
public class LegalTermController {

    @Autowired
    private LegalTermRepository termRepository;

    @GetMapping("/list")
    public Result<List<LegalTerm>> list(@RequestParam(required = false) String keyword) {
        log.info("法律术语查询, 关键词: {}", keyword);
        if (keyword != null && !keyword.isEmpty()) {
            return Result.success(termRepository.searchTerms(keyword));
        }
        return Result.success(termRepository.findByIsDeletedFalseOrderByFrequencyDesc());
    }
}