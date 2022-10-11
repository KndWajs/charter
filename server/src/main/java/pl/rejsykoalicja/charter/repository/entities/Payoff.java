package pl.rejsykoalicja.charter.repository.entities;

import lombok.Getter;
import pl.rejsykoalicja.charter.enums.Discount;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "PAYOFF")
@Getter
public class Payoff {
    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 20)
    private BigInteger id;

    @Column(name = "TOTAL_COST", columnDefinition = "NUMERIC(7,3)")
    private BigDecimal totalCost;

    @Column(name = "DISCOUNT")
    private Integer discountValue;

    @ElementCollection(targetClass = Discount.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "PAYOFF_DISCOUNTS",
            joinColumns = @JoinColumn(name = "PAYOFF_ID"))
    @Column(name = "DISCOUNT")
    private List<Discount> discounts;

    @OneToOne
    @JoinColumn(name = "VOUCHER_FK", referencedColumnName = "ID")
    private Voucher voucher;

    @OneToMany(mappedBy = "payoff")
    private List<Payment> payments;

    @OneToOne(mappedBy = "payoff")
    private Charter charter;
}
