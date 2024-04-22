package rangiffler.data.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rangiffler.data.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findByUsername(@Nonnull String username);

    Slice<UserEntity> findByUsernameNot(@Nonnull String username,
                                        @Nonnull Pageable pageable);

    @Query("select u from UserEntity u where u.username <> :username" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
    Slice<UserEntity> findByUsernameNotAndSearchQuery(@Param("username") String username,
                                                      @Nonnull Pageable pageable,
                                                      @Param("searchQuery") String searchQuery);


    @Query("select u from UserEntity u join FriendsEntity f on u = f.friend" +
            " where f.status = rangiffler.model.FriendStatus.FRIEND and f.user = :requester")
    Slice<UserEntity> findFriends(@Param("requester") UserEntity requester,
                                  @Nonnull Pageable pageable);

    @Query("select u from UserEntity u join FriendsEntity f on u = f.friend" +
            " where f.status = rangiffler.model.FriendStatus.FRIEND and f.user = :requester" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
    Slice<UserEntity> findFriends(@Param("requester") UserEntity requester,
                                  @Nonnull Pageable pageable,
                                  @Param("searchQuery") String searchQuery);

    @Query("select u from UserEntity u join FriendsEntity f on u = f.friend" +
            " where f.status = rangiffler.model.FriendStatus.FRIEND and f.user = :requester")
    List<UserEntity> findFriends(@Param("requester") UserEntity requester);

    @Query("select u from UserEntity u join FriendsEntity f on u = f.friend" +
            " where f.status = rangiffler.model.FriendStatus.FRIEND and f.user = :requester" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
    Slice<UserEntity> findFriends(@Param("requester") UserEntity requester,
                                  @Param("searchQuery") String searchQuery);

    @Query("select u from UserEntity u join FriendsEntity f on u = f.friend" +
            " where f.status = rangiffler.model.FriendStatus.INVITATION_SENT and f.user = :requester")
    Slice<UserEntity> findOutcomeInvitations(@Param("requester") UserEntity requester,
                                             @Nonnull Pageable pageable);

    @Query("select u from UserEntity u join FriendsEntity f on u = f.friend" +
            " where f.status = rangiffler.model.FriendStatus.INVITATION_SENT and f.user = :requester" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
    Slice<UserEntity> findOutcomeInvitations(@Param("requester") UserEntity requester,
                                             @Nonnull Pageable pageable,
                                             @Param("searchQuery") String searchQuery);

    @Query("select u from UserEntity u join FriendsEntity f on u = f.friend" +
            " where f.status = rangiffler.model.FriendStatus.INVITATION_SENT and f.user = :requester")
    List<UserEntity> findOutcomeInvitations(@Param("requester") UserEntity requester);

    @Query("select u from UserEntity u join FriendsEntity f on u = f.friend" +
            " where f.status = rangiffler.model.FriendStatus.INVITATION_SENT and f.user = :requester" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
    List<UserEntity> findOutcomeInvitations(@Param("requester") UserEntity requester,
                                            @Param("searchQuery") String searchQuery);

    @Query("select u from UserEntity u join FriendsEntity f on u = f.user" +
            " where f.status = rangiffler.model.FriendStatus.INVITATION_RECEIVED and f.friend = :addressee")
    Slice<UserEntity> findIncomeInvitations(@Param("addressee") UserEntity addressee,
                                            @Nonnull Pageable pageable);

    @Query("select u from UserEntity u join FriendsEntity f on u = f.user" +
            " where f.status = rangiffler.model.FriendStatus.INVITATION_RECEIVED and f.friend = :addressee" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
    Slice<UserEntity> findIncomeInvitations(@Param("addressee") UserEntity addressee,
                                            @Nonnull Pageable pageable,
                                            @Param("searchQuery") String searchQuery);

    @Query("select u from UserEntity u join FriendsEntity f on u = f.user" +
            " where f.status = rangiffler.model.FriendStatus.INVITATION_RECEIVED and f.friend = :addressee")
    List<UserEntity> findIncomeInvitations(@Param("addressee") UserEntity addressee);

    @Query("select u from UserEntity u join FriendsEntity f on u = f.user" +
            " where f.status = rangiffler.model.FriendStatus.INVITATION_RECEIVED and f.friend = :addressee" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
    List<UserEntity> findIncomeInvitations(@Param("addressee") UserEntity addressee,
                                           @Param("searchQuery") String searchQuery);
}
