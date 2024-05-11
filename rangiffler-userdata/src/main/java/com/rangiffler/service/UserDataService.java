package com.rangiffler.service;

import com.rangiffler.exception.NotFoundException;
import jakarta.annotation.Nonnull;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.rangiffler.data.CountryEntity;
import com.rangiffler.data.FriendsEntity;
import com.rangiffler.data.UserEntity;
import com.rangiffler.data.repository.CountryRepository;
import com.rangiffler.data.repository.FriendsRepository;
import com.rangiffler.data.repository.UserRepository;
import com.rangiffler.model.CountryJson;
import com.rangiffler.model.FriendStatus;
import com.rangiffler.model.UserJson;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @Transactional
    @KafkaListener(topics = "users", groupId = "userdata")
    public void listener(@Payload UserJson user, ConsumerRecord<String, UserJson> cr) {
        LOG.info("### Kafka topic [users] received message: " + user.username());
        LOG.info("### Kafka consumer record: " + cr.toString());
        UserEntity userDataEntity = new UserEntity();
        userDataEntity.setUsername(user.username());
        //x userDataEntity.setCurrency(DEFAULT_USER_CURRENCY);
        UserEntity userEntity = userRepository.save(userDataEntity);
        LOG.info(String.format(
                "### User '%s' successfully saved to database with id: %s",
                user.username(),
                userEntity.getId()
        ));
    }

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
        var userEntity = userRepository.findByUsernameNotAndSearchQuery(username, pageable, query);

        Slice<UserJson> userJsons = userEntity.map(new Function<>() {
            @Override
            public UserJson apply(UserEntity userEntity) {
                UserJson userJson = new UserJson(
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

                return userJson;
            }
        });
        return userJsons;
    }

    @Transactional(readOnly = true)
    public @Nonnull
    Slice<UserJson> outcomeInvitations(UserEntity requester,
                                       Pageable pageable,
                                       String searchQuery) {
        Slice<UserEntity> userEntities;
        if (searchQuery == null) {
            userEntities = userRepository.findOutcomeInvitations(requester, pageable);

        } else {
            userEntities = userRepository.findOutcomeInvitations(requester, pageable, searchQuery);
        }

        return userEntities.map(userEntity -> new UserJson(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getFirstname(),
                userEntity.getLastName(),
                userEntity.getAvatar() != null && userEntity.getAvatar().length > 0 ? new String(userEntity.getAvatar(), StandardCharsets.UTF_8) : null,
                FriendStatus.INVITATION_SENT,
                new CountryJson(
                        userEntity.getCountry().getId(),
                        userEntity.getCountry().getCode(),
                        userEntity.getCountry().getFlag(),
                        userEntity.getCountry().getName()
                )));
    }

    @Transactional(readOnly = true)
    public @Nonnull
    List<UserJson> outcomeInvitations(UserEntity userEntity) {
        var userEntities = userRepository.findOutcomeInvitations(userEntity);
        return userEntities.stream()
                .map(fe -> UserJson.fromEntity(userEntity))
                .toList();

    }

    @Transactional(readOnly = true)
    public @Nonnull
    List<UserJson> outcomeInvitations(UserEntity userEntity, String query) {
        var userEntities = userRepository.findOutcomeInvitations(userEntity, query);
        return userEntities.stream()
                .map(fe -> UserJson.fromEntity(userEntity))
                .toList();
    }

//    @Transactional(readOnly = true)
//    public @Nonnull
//    List<UserJson> outcomeInvitations(UserEntity userEntity, String query) {
//        return getRequiredUser(userEntity.getId().toString())
//                .getInvites()
//                .stream()
//                .filter(fs -> fs.getStatus().equals(FriendStatus.INVITATION_SENT) && fs.getFriend().)
//                .map(fe -> UserJson.fromEntity(fe.getUser(), FriendStatus.INVITATION_SENT))
//                .toList();
//    }


    @Transactional(readOnly = true)
    public @Nonnull
    Slice<UserJson> incomeInvitations(UserEntity addressee,
                                      Pageable pageable,
                                      String searchQuery) {
        Slice<UserEntity> userEntities;
        if (searchQuery == null) {
            userEntities = userRepository.findIncomeInvitations(addressee, pageable);

        } else {
            userEntities = userRepository.findIncomeInvitations(addressee, pageable, searchQuery);
        }

        return userEntities.map(userEntity1 -> new UserJson(
                userEntity1.getId(),
                userEntity1.getUsername(),
                userEntity1.getFirstname(),
                userEntity1.getLastName(),
                userEntity1.getAvatar() != null && userEntity1.getAvatar().length > 0 ? new String(userEntity1.getAvatar(), StandardCharsets.UTF_8) : null,
                FriendStatus.INVITATION_RECEIVED,
                new CountryJson(
                        userEntity1.getCountry().getId(),
                        userEntity1.getCountry().getCode(),
                        userEntity1.getCountry().getFlag(),
                        userEntity1.getCountry().getName()
                )));
    }


    @Transactional(readOnly = true)
    public @Nonnull
    Slice<UserJson> friends(UserEntity requester,
                            Pageable pageable,
                            String searchQuery) {

        Slice<UserEntity> userEntities = userRepository.findFriends(requester, pageable, searchQuery);

        return userEntities.map(userEntity1 -> new UserJson(
                userEntity1.getId(),
                userEntity1.getUsername(),
                userEntity1.getFirstname(),
                userEntity1.getLastName(),
                userEntity1.getAvatar() != null && userEntity1.getAvatar().length > 0 ? new String(userEntity1.getAvatar(), StandardCharsets.UTF_8) : null,
                FriendStatus.FRIEND,
                new CountryJson(
                        userEntity1.getCountry().getId(),
                        userEntity1.getCountry().getCode(),
                        userEntity1.getCountry().getFlag(),
                        userEntity1.getCountry().getName()
                )));
    }


    @Transactional(readOnly = true)
    public @Nonnull
    List<UserJson> incomeInvitations(@Nonnull String username) {
        return getRequiredUser(username)
                .getInvites()
                .stream()
                .filter(fs -> fs.getStatus().equals(FriendStatus.INVITATION_RECEIVED))
                .map(fe -> UserJson.fromEntity(fe.getUser(), FriendStatus.INVITATION_RECEIVED))
                .toList();
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


    @Transactional
    public @Nonnull
    UserJson removeFriend(String username, @Nonnull UUID userId) {
        UserEntity currentUser = getRequiredUser(username);
        UserEntity friendToRemove = getFriendUser(userId);

        currentUser.removeFriends(friendToRemove);
        currentUser.removeInvites(friendToRemove);
        friendToRemove.removeFriends(currentUser);
        friendToRemove.removeInvites(currentUser);

        userRepository.save(currentUser);
        userRepository.save(friendToRemove);

        return UserJson.fromEntity(friendToRemove);
    }

    @Transactional
    public UserJson addFriend(@Nonnull String username, @Nonnull UUID userId) {
        UserEntity currentUser = getRequiredUser(username);
        UserEntity friendEntity = getFriendUser(userId);

        currentUser.addFriends(FriendStatus.INVITATION_RECEIVED, friendEntity);
        userRepository.save(currentUser);
        return UserJson.fromEntity(friendEntity, FriendStatus.INVITATION_SENT);
    }


    @Transactional
    public @Nonnull
    UserJson acceptInvitation(String username, @Nonnull UUID userId) {
        UserEntity currentUser = getRequiredUser(username);
        UserEntity inviteUser = getFriendUser(userId);

        FriendsEntity invite = currentUser.getInvites()
                .stream()
                .filter(fe -> fe.getUser().getId().equals(inviteUser.getId()))
                .findFirst()
                .orElseThrow();

        invite.setStatus(FriendStatus.FRIEND);
        currentUser.addFriends(FriendStatus.FRIEND, inviteUser);
        userRepository.save(currentUser);

        return UserJson.fromEntity(inviteUser, FriendStatus.FRIEND);
    }

    @Transactional
    public @Nonnull
    UserJson declineInvitation(String username, @Nonnull UUID userId) {
        UserEntity currentUser = getRequiredUser(username);
        UserEntity friendToReject = getFriendUser(userId);

        currentUser.removeFriends(friendToReject);
        currentUser.removeInvites(friendToReject);
        friendToReject.removeFriends(currentUser);
        friendToReject.removeInvites(currentUser);

        userRepository.save(currentUser);
        userRepository.save(friendToReject);

        return UserJson.fromEntity(friendToReject);
    }

    @Nonnull
    UserEntity getRequiredUser(@Nonnull String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new NotFoundException("Can`t find user by username: " + username);
        }
        return user;
    }

    @Nonnull
    UserEntity getFriendUser(@Nonnull UUID userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("Can`t find user by id: " + userId);
        }
        return user.get();
    }

}
