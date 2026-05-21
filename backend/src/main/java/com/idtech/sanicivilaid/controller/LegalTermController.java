package com.idtech.sanicivilaid.controller;

import com.idtech.sanicivilaid.entity.LegalTerm;
import com.idtech.sanicivilaid.repository.LegalTermRepository;
import com.idtech.sanicivilaid.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        Pageable pageable = PageRequest.of(0, 48);
        if (keyword != null && !keyword.isEmpty()) {
            return Result.success(termRepository.searchTerms(keyword, pageable));
        }
        return Result.success(termRepository.findByIsDeletedFalseOrderByFrequencyDesc(pageable));
    }
}