package pl.rejsykoalicja.charter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.rejsykoalicja.charter.repository.entities.Voucher;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, BigInteger> {

    Optional<Voucher> getByCode(String tag);
}
