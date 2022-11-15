package com.godchigam.godchigam.domain.groupbuying.repository;
import com.godchigam.godchigam.domain.groupbuying.entity.JoinStorage;
import com.godchigam.godchigam.domain.groupbuying.entity.Product;
import com.godchigam.godchigam.domain.groupbuying.entity.RequestMessage;
import com.godchigam.godchigam.domain.groupbuying.entity.RequestStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long>{

    @Query("select b from Product b where b.writer.loginId= :loginId ")
    List<Product> findByWriter(@Param("loginId") String loginId);

    @Query("select b from Product b where b.writer.address= :userAddress ")
    List<Product> findByWriter1(@Param("userAddress") String userAddress);
}
