package com.godchigam.godchigam.domain.groupbuying.repository;

import com.godchigam.godchigam.domain.groupbuying.entity.RequestMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestMessageRepository extends JpaRepository<RequestMessage,Long> {
    
}
