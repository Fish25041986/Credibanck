package com.credibanco.tarjeta.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.credibanco.tarjeta.model.Transaction;

public interface ITransactionDao extends JpaRepository<Transaction, Long>{

	@Query("SELECT t FROM Transaction t WHERE t.transaction_id = ?1")
	Optional<Transaction> findByNumberId(Long number);
	
}
