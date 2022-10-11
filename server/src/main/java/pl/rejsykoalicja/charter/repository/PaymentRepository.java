package pl.rejsykoalicja.charter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.rejsykoalicja.charter.repository.entities.Charter;
import pl.rejsykoalicja.charter.repository.entities.Payment;

import java.math.BigInteger;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, BigInteger> {
}
