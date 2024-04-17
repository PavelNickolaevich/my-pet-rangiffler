package rangiffler.service;

import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rangiffler.data.CountryEntity;
import rangiffler.data.FriendsEntity;
import rangiffler.data.UserEntity;
import rangiffler.data.repository.CountryRepository;
import rangiffler.data.repository.FriendsRepository;
import rangiffler.data.repository.UserRepository;
import rangiffler.exception.NotFoundException;
import rangiffler.model.CountryJson;
import rangiffler.model.FriendJson;
import rangiffler.model.FriendStatus;
import rangiffler.model.UserJson;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;

@Component
public class UserDataService {

    private static final Logger LOG = LoggerFactory.getLogger(UserDataService.class);
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final CountryRepository countryRepository;


    @Autowired
    public UserDataService(UserRepository userRepository, FriendsRepository friendsRepository, CountryRepository countryRepository) {
        this.userRepository = userRepository;
        this.friendsRepository = friendsRepository;
        this.countryRepository = countryRepository;
    }

    //    @Transactional
//   @KafkaListener(topics = "users", groupId = "userdata")
//    public void listener(@Payload UserJson user, ConsumerRecord<String, UserJson> cr) {
//        LOG.info("### Kafka topic [users] received message: " + user.username());
//        LOG.info("### Kafka consumer record: " + cr.toString());
//        UserEntity userDataEntity = new UserEntity();
//        userDataEntity.setUsername(user.username());
//        userDataEntity.setCurrency(DEFAULT_USER_CURRENCY);
//        UserEntity userEntity = userRepository.save(userDataEntity);
//        LOG.info(String.format(
//                "### User '%s' successfully saved to database with id: %s",
//                user.username(),
//                userEntity.getId()
//        ));
//    }
    @Transactional
    public @Nonnull
    UserJson updateUser(@Nonnull UserJson user) {
        UserEntity userEntity = userRepository.findByUsername(user.username());
        if (userEntity == null) {
            throw new NotFoundException("Can`t find user by username: " + user.username());
        }
        userEntity.setFirstname(user.firstname());
        userEntity.setLastName(user.surname());
        userEntity.setAvatar(user.avatar() != null ? user.avatar().getBytes(StandardCharsets.UTF_8) : null);

        CountryEntity countryEntity = countryRepository.findByCode(user.country().code());

        userEntity.setCountry(countryEntity);
        UserEntity saved = userRepository.save(userEntity);

        return UserJson.fromEntity(saved);
    }


    @Transactional
    public @Nonnull
    CountryEntity getCountry(String code) {
        if (code == null) {
            throw new NotFoundException("Can`t find country by code: " + code);
        }
        return countryRepository.findByCode(code);
    }

    @Transactional(readOnly = true)
    public @Nonnull
    UserJson getCurrentUserOrCreateIfAbsent(@Nonnull String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setUsername(username);
            return UserJson.fromEntity(userRepository.save(userEntity));
        } else {
            return UserJson.fromEntity(userEntity);
        }
    }

    @Transactional(readOnly = true)
    public @Nonnull
    UserJson getCurrentUser(@Nonnull String username) {
        return UserJson.fromEntity(getRequiredUser(username));
    }


//    @Transactional(readOnly = true)
//    public  @Nonnull
//    Slice<List<UserJson>> allUsers(String username, Pageable pageable, String query) {
//        Set<UserJson> result = new HashSet<>();
//        for (UserEntity user : userRepository.findByUsernameNotAndSearchQuery(username, pageable, query)) {
//            result.add(UserJson.fromEntity(user));
//        }
//
//        return new ArrayList<>();
//    }

    @Transactional(readOnly = true)
    public @Nonnull
    Slice<UserJson> allUsers(String username, Pageable pageable, String query) {
        var a = userRepository.findByUsernameNotAndSearchQuery(username, pageable, query);

//        Slice<UserJson> dtos  = a.map(this::convertToObjectDto);
//        return dtos;

        Slice<UserJson> b = a.map(new Function<UserEntity, UserJson>() {
            @Override
            public UserJson apply(UserEntity userEntity) {
                UserJson dto = new UserJson(
                        userEntity.getId(),
                        userEntity.getUsername(),
                        userEntity.getFirstname(),
                        userEntity.getLastName(),
                        userEntity.getAvatar() != null && userEntity.getAvatar().length > 0 ? new String(userEntity.getAvatar(), StandardCharsets.UTF_8) : null,
                        null,
                        new CountryJson(
                                userEntity.getCountry().getId(),
                                userEntity.getCountry().getCode(),
                                userEntity.getCountry().getFlag(),
                                userEntity.getCountry().getName()
                        ));

                return dto;
            }
        });
        return b;
    }


//    @Transactional(readOnly = true)
//    public @Nonnull
//    List<UserJson> allUsers(@Nonnull String username) {
//        Set<UserJson> result = new HashSet<>();
//        for (UserEntity user : userRepository.findByUsernameNotAndSearchQuery(username, )) {
//            List<FriendsEntity> sendInvites = user.getFriends();
//            List<FriendsEntity> receivedInvites = user.getInvites();
//
//            if (!sendInvites.isEmpty() || !receivedInvites.isEmpty()) {
//                Optional<FriendsEntity> inviteToMe = sendInvites.stream()
//                        .filter(i -> i.getFriend().getUsername().equals(username))
//                        .findFirst();
//
//                Optional<FriendsEntity> inviteFromMe = receivedInvites.stream()
//                        .filter(i -> i.getUser().getUsername().equals(username))
//                        .findFirst();
//
//                if (inviteToMe.isPresent()) {
//                    FriendsEntity invite = inviteToMe.get();
//                    result.add(UserJson.fromEntity(user, invite.getCreatedDate() != null
//                            ? FriendStatus.INVITATION_RECEIVED
//                            : FriendStatus.FRIEND));
//                }
//                if (inviteFromMe.isPresent()) {
//                    FriendsEntity invite = inviteFromMe.get();
//                    result.add(UserJson.fromEntity(user,invite.getCreatedDate() != null
//                            ? FriendStatus.INVITATION_SENT
//                            : FriendStatus.FRIEND));
//                }
//            } else {
//                result.add(UserJson.fromEntity(user));
//            }
//        }
//        return new ArrayList<>(result);
//    }

//    @Transactional(readOnly = true)
//    public @Nonnull
//    List<UserJson> allUsers(@Nonnull String username) {
//        Set<UserJson> result = new HashSet<>();
//        for (UserEntity user : userRepository.findByUsernameNot(username)) {
//            List<FriendsEntity> sendInvites = user.getFriends();
//            List<FriendsEntity> receivedInvites = user.getInvites();
//
//            if (!sendInvites.isEmpty() || !receivedInvites.isEmpty()) {
//                Optional<FriendsEntity> inviteToMe = sendInvites.stream()
//                        .filter(i -> i.getFriend().getUsername().equals(username))
//                        .findFirst();
//
//                Optional<FriendsEntity> inviteFromMe = receivedInvites.stream()
//                        .filter(i -> i.getUser().getUsername().equals(username))
//                        .findFirst();
//
//                if (inviteToMe.isPresent()) {
//                    FriendsEntity invite = inviteToMe.get();
//                    result.add(UserJson.fromEntity(user, invite.getCreatedDate() != null
//                            ? FriendStatus.INVITATION_RECEIVED
//                            : FriendStatus.FRIEND));
//                }
//                if (inviteFromMe.isPresent()) {
//                    FriendsEntity invite = inviteFromMe.get();
//                    result.add(UserJson.fromEntity(user,invite.getCreatedDate() != null
//                            ? FriendStatus.INVITATION_SENT
//                            : FriendStatus.FRIEND));
//                }
//            } else {
//                result.add(UserJson.fromEntity(user));
//            }
//        }
//        return new ArrayList<>(result);
//    }

//    @Transactional(readOnly = true)
//    public @Nonnull
//    List<UserJson> friends(@Nonnull String username, boolean includePending) {
//        return getRequiredUser(username)
//                .getFriends()
//                .stream()
//                .filter(fe -> includePending || !fe.isPending())
//                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.isPending()
//                        ? FriendState.INVITE_SENT
//                        : FriendState.FRIEND))
//                .toList();
//    }

    public List<UserJson> friends(String username, boolean includePending) {
        return userRepository.findByUsername(username)
                .getFriends().stream().filter(fe -> fe.getStatus().equals(FriendStatus.FRIEND))
                .map(fe -> UserJson.fromEntity(fe.getFriend())).toList();
    }


    @Transactional(readOnly = true)
    public @Nonnull
    List<UserJson> invitations(@Nonnull String username) {
        return getRequiredUser(username)
                .getInvites()
                .stream()
                .map(fe -> UserJson.fromEntity(fe.getUser(), FriendStatus.INVITATION_SENT))
                .toList();
    }


    @Transactional
    public UserJson addFriend(@Nonnull String username, @Nonnull FriendJson friend) {
        UserEntity currentUser = getRequiredUser(username);
        UserEntity friendEntity = getRequiredUser(friend.username());

        currentUser.addFriends(FriendStatus.FRIEND, friendEntity);
        userRepository.save(currentUser);
        return UserJson.fromEntity(friendEntity, FriendStatus.INVITATION_SENT);
    }

    @Transactional
    public @Nonnull
    List<UserJson> acceptInvitation(@Nonnull String username, @Nonnull FriendJson invitation) {
        UserEntity currentUser = getRequiredUser(username);
        UserEntity inviteUser = getRequiredUser(invitation.username());

        FriendsEntity invite = currentUser.getInvites()
                .stream()
                .filter(fe -> fe.getUser().getUsername().equals(inviteUser.getUsername()))
                .findFirst()
                .orElseThrow();

        //  invite.setCreatedDate(new Date());
        currentUser.addFriends(FriendStatus.FRIEND, inviteUser);
        userRepository.save(currentUser);

        return currentUser
                .getFriends()
                .stream()
                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.getCreatedDate() != null
                        ? FriendStatus.INVITATION_RECEIVED
                        : FriendStatus.FRIEND))
                .toList();
    }

//    @Transactional
//    public @Nonnull
//    List<UserJson> declineInvitation(@Nonnull String username, @Nonnull FriendJson invitation) {
//        UserEntity currentUser = getRequiredUser(username);
//        UserEntity friendToDecline = getRequiredUser(invitation.username());
//
//        currentUser.removeInvites(friendToDecline);
//        friendToDecline.removeFriends(currentUser);
//
//        userRepository.save(currentUser);
//        userRepository.save(friendToDecline);
//
//        return currentUser.getInvites()
//                .stream()
//              //  .filter(FriendsEntity::isPending)
//                .map(fe -> UserJson.fromEntity(fe.getUser(), FriendStatus.INVITATION_RECEIVED)
//                .toList();
//    }

    @Transactional
    public @Nonnull
    List<UserJson> removeFriend(@Nonnull String username, @Nonnull String friendUsername) {
        UserEntity currentUser = getRequiredUser(username);
        UserEntity friendToRemove = getRequiredUser(friendUsername);

        currentUser.removeFriends(friendToRemove);
        currentUser.removeInvites(friendToRemove);
        friendToRemove.removeFriends(currentUser);
        friendToRemove.removeInvites(currentUser);

        userRepository.save(currentUser);
        userRepository.save(friendToRemove);

        return currentUser
                .getFriends()
                .stream()
                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.getStatus().equals(FriendStatus.FRIEND)
                        ? FriendStatus.INVITATION_RECEIVED
                        : FriendStatus.INVITATION_SENT))
                .toList();
    }

    @Nonnull
    UserEntity getRequiredUser(@Nonnull String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new NotFoundException("Can`t find user by username: " + username);
        }
        return user;
    }


}
