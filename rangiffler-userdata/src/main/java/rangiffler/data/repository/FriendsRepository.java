package rangiffler.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rangiffler.data.FriendsEntity;

import java.util.List;
import java.util.UUID;

public interface FriendsRepository extends JpaRepository<FriendsEntity, UUID> {

    List<FriendsEntity> findAllByUserId(UUID userId);
}
