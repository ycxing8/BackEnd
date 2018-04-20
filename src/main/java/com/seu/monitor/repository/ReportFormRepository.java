package com.seu.monitor.repository;

import com.seu.monitor.entity.ReportForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportFormRepository extends JpaRepository<ReportForm,Integer> {
    //List<ReportForm> findByDateAndContent(String date, String content);
    List<ReportForm> findByMachineIdentifierAndDateAndContent(String machineIdentifier, String date, String content);
    List<ReportForm> findByMachineIdentifierAndDate(String machineIdentifier, String date);
}
