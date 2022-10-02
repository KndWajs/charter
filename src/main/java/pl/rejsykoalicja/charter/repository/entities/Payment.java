package pl.rejsykoalicja.charter.repository.entities;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;

@Entity
@Table(name = "PAYMENT")
@Getter
public class Payment {
    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 20)
    private BigInteger id;

    @Column(name = "TOTAL_COST", columnDefinition = "NUMERIC(7,3)", nullable = false)
    private BigDecimal amount;

    @Column(name = "PAYMENT_DATE", nullable = false)
    private ZonedDateTime paymentDate;

    @Column(name = "PAID", nullable = false)
    private Boolean paid;

    @Column(name = "TAG", length = 256, nullable = false)
    private String tag;

    @ManyToOne
    @JoinColumn(name = "PAYOFF_FK")
    private Payoff payoff;
}
