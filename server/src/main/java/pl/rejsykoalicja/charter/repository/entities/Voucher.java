package pl.rejsykoalicja.charter.repository.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigInteger;

@Builder
@Entity
@Table(name = "VOUCHER")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {
    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 20)
    private BigInteger id;

    @Column(name = "NAME", length = 256, nullable = false)
    private String name;

    @Column(name = "CODE", length = 256, nullable = false, unique = true)
    private String code;

    @Column(name = "AMOUNT", nullable = false)
    private Integer amount;

    @Column(name = "EXCEED_LIMIT", nullable = false)
    private boolean canExceedLimit;

    @OneToOne(mappedBy = "voucher")
    private Payoff payoff;
}
