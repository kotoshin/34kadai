package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;
@Service
public class ReportService {
    
    private final ReportRepository reportRepository;
    
    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // 日報一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }
 // 従業員保存
    @Transactional
    public ErrorKinds save(Report report) {

        // 日付重複チェック
        if (reportRepository.findByReportDateAndEmployee(report.getReportDate(), report.getEmployee()).size()!=0) {
            return ErrorKinds.DATECHECK_ERROR;
        }

        report.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }
 // 1件を検索
    public Report findById(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }
  //授業員更新
    @Transactional
    public ErrorKinds update(Report report, Integer id) {
        Report e= findById(id);
        report.setEmployee(e.getEmployee());
        if( !report.getReportDate().equals(e.getReportDate())) {
            // 日付重複チェック
            if (reportRepository.findByReportDateAndEmployee(report.getReportDate(), report.getEmployee()).size()!=0) {
                return ErrorKinds.DATECHECK_ERROR;
            }
        }
    
        report.setDeleteFlg(e.isDeleteFlg());
        report.setCreatedAt(e.getCreatedAt());
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
        
    }
 // 従業員削除
    @Transactional
    public ErrorKinds delete(Integer id) {

        Report report = findById(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;
    }
}
