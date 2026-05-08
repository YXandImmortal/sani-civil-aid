package com.idtech.nuosucivilaid.controller;

import com.idtech.nuosucivilaid.entity.Consultation;
import com.idtech.nuosucivilaid.repository.ConsultationRepository;
import com.idtech.nuosucivilaid.service.AuthService;
import com.idtech.nuosucivilaid.vo.Result;
import com.idtech.nuosucivilaid.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/civil/consultation")
public class ConsultationController {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private AuthService authService;

    // 提交新咨询
    @PostMapping("/submit")
    public Result<Void> submit(@RequestBody Consultation consultation) {
        UserInfoVO currentUser = authService.getCurrentUserInfo();
        consultation.setUserId(currentUser.getId());
        consultation.setCreateTime(Instant.now());
        consultation.setStatus(0);
        consultation.setIsDeleted(false);

        consultationRepository.save(consultation);
        log.info("用户 {} 提交了新的法律咨询", currentUser.getUsername());
        return Result.success(null);
    }

    // 获取我的咨询历史
    @GetMapping("/my-list")
    public Result<List<Consultation>> getMyList() {
        UserInfoVO currentUser = authService.getCurrentUserInfo();
        List<Consultation> list = consultationRepository.findByUserIdAndIsDeletedFalseOrderByCreateTimeDesc(currentUser.getId());
        return Result.success(list);
    }
}