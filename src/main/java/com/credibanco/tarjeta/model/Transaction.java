package com.credibanco.tarjeta.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="Transaction")
public class Transaction {

    private static final long serialVersionUID= -4310027227752446841L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transaction_id;
	

    @Column(name = "transaction_date", nullable = false)
    @NotNull
    private LocalDateTime transactionDate;

    /*El transaction_type tiene dos valores
	* 1 para compras y 2 para recarga*/
    @Column(name = "transaction_type", nullable = false)
    @Min(value = 1)
    @Max(value = 2)
    private Integer transactionType;

    @Column(name = "value", nullable = false)
    @NotNull
    @Min(value = 0)
    private BigDecimal value;

    /*El status_transaction tiene dos valores
   	* 1 para realizada y 2 para anulada*/
    @Column(name = "status_transaction", nullable = false)
    @Min(value = 1)
    @Max(value = 2)
    private Integer statusTransaction;

	
    @ManyToOne(fetch= FetchType.LAZY)
	@JsonIgnoreProperties ( {"hibernateLazyInitializer", "handler"})
	private Card card;
	
}
