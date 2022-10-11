package pl.rejsykoalicja.charter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.rejsykoalicja.charter.repository.entities.Captain;

import java.math.BigInteger;

@Repository
public interface CaptainRepository extends JpaRepository<Captain, BigInteger> {

}
