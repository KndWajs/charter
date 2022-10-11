package pl.rejsykoalicja.charter.repository.entities;

import lombok.Getter;
import pl.rejsykoalicja.charter.enums.Equipment;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "CHARTER")
@Getter
public class Charter {
    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 20)
    private BigInteger id;

    @ElementCollection(targetClass = Equipment.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "CHARTER_EQUIPMENT",
            joinColumns = @JoinColumn(name = "CHARTER_ID"))
    @Column(name = "EQUIPMENT")
    private List<Equipment> equipmentList;

    @Column(name = "CHARTER_FROM", nullable = false)
    private ZonedDateTime from;

    @Column(name = "CHARTER_TO", nullable = false)
    private ZonedDateTime to;

    @ManyToOne
    @JoinColumn(name = "CAPTAIN_FK")
    private Captain captain;

    @Column(name = "CREW", nullable = false)
    private Integer crewNumber;

    @OneToOne
    @JoinColumn(name = "PAYOFF_FK")
    private Payoff payoff;

    @Column(name = "RESERVATION_TIME", nullable = false)
    private ZonedDateTime reservation;

    @Column(name = "THERMS_VERSION", length = 256, nullable = false)
    private String thermsAndConditionVersion;
}
