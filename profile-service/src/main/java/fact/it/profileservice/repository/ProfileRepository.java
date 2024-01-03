package fact.it.profileservice.repository;

import fact.it.profileservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByIdNotNull();

    Profile findByProfileIdEquals(String profileId);
    Profile findByUsernameEquals(String profileName);
}
