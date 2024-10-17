package com.example.barberlab6.Repository;

import com.example.barberlab6.Models.Client;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClientsRepository extends JpaRepository<Client, Long> {
    @Query("SELECT f FROM Client f " +
            "WHERE (:cliname IS NULL OR LOWER(f.cliname) LIKE LOWER(CONCAT('%', :cliname, '%'))) " +
            "AND (:start_date IS NULL OR f.sessionDateTime >= :start_date) " +
            "AND (:end_date IS NULL OR f.sessionDateTime <= :end_date) " +
            "AND (:service IS NULL OR LOWER(f.service) LIKE LOWER(CONCAT('%', :service, '%')))")
    List<Client> findByParams(
            @Param("cliname") String cliname,
            @Param("start_date") LocalDateTime start_date,
            @Param("end_date") LocalDateTime end_date,
            @Param("service") String service,
            Sort sort);



    @Query("SELECT DATE(f.sessionDateTime), COUNT(f) FROM Client f GROUP BY DATE(f.sessionDateTime)")
    List<Object[]> findPlayIssueStats();

}