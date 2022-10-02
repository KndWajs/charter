package pl.rejsykoalicja.charter.repository.entities;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigInteger;

@Entity
@Table(name = "VOUCHER")
@Getter
public class Voucher {
    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 20)
    private BigInteger id;

    @Column(name = "NAME", length = 256, nullable = false)
    private String name;

    @Column(name = "TAG", length = 256, nullable = false)
    private String tag;

    @Column(name = "AMOUNT", nullable = false)
    private Integer amount;

    @OneToOne(mappedBy = "voucher")
    private Payoff payoff;
}
