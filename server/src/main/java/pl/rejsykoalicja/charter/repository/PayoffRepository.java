package pl.rejsykoalicja.charter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.rejsykoalicja.charter.repository.entities.Payment;
import pl.rejsykoalicja.charter.repository.entities.Payoff;

import java.math.BigInteger;

@Repository
public interface PayoffRepository extends JpaRepository<Payoff, BigInteger> {
}
