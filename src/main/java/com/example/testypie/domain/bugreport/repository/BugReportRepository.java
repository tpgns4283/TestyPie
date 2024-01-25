package com.example.testypie.domain.bugreport.repository;

import com.example.testypie.domain.bugreport.entity.BugReport;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = BugReport.class, idClass = Long.class)
public interface BugReportRepository {
    BugReport save(BugReport bugReport);

}
