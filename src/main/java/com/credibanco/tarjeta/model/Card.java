package com.credibanco.tarjeta.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name="card")
public class Card {

	private static final long serialVersionUID= -4310027227752446841L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_card;

	//The cardTipe has two values ​​1debit 2credit
	@Column(name = "card_type", nullable = false)
    @Min(value = 1)
    @Max(value = 2)
    private Integer cardType;

    @Column(name = "card_number", length = 16, nullable = true)
    private String cardNumber;

    @Column(name = "client_name", nullable = false)
    @NotBlank
    private String clientName;

    @Column(name = "client_last_name", nullable = false)
    @NotBlank
    private String clientLastName;

    @Column(name = "creation_date", nullable = false)
    @Past
    private LocalDateTime creationDate;

    @Column(name = "expiration_date", nullable = false)
    @Future
    private LocalDateTime expirationDate;

    //The status card has two values ​​0 for deactivated and 1 for activated
    @Column(name = "status_card", nullable = true, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean statusCard;

    //The locked card has two values ​​1 for unlocked and 0 for locked
    @Column(name = "locked_card", nullable = true,columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean lockedCard;

    @Column(name = "balance", nullable = true,  columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    @Min(value = 0)
    private BigDecimal balance;
}
