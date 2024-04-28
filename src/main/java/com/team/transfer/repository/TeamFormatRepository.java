package com.team.transfer.repository;

import com.team.transfer.domain.FormatType;
import com.team.transfer.domain.TeamFormat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamFormatRepository extends JpaRepository<TeamFormat, Long> {
    TeamFormat findByFormatType(FormatType formatType);
}
