package com.aboutme.springwebservice.board.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardDailyRepository extends JpaRepository<Enquiry,String> {
}
