package com.parvez.spring_jpa.repository;

import com.parvez.spring_jpa.model.SalaryAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryAuditRepository extends JpaRepository<SalaryAudit, Long> {

}
