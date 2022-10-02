package pl.rejsykoalicja.charter.repository.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "CAPTAIN")
public class Captain {
    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 20)
    private BigInteger id;

    @Column(name = "FIRST_NAME", length = 256, nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", length = 256, nullable = false)
    private String lastName;

    @Column(name = "BIRTH_DATE", nullable = false)
    private ZonedDateTime birthDate;

    @Column(name = "LICENSE", length = 256, nullable = false)
    private String licenseNo;

    @OneToMany(mappedBy = "captain")
    private List<Charter> charter;
}
