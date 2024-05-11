package com.rangiffler.data;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import com.rangiffler.model.FriendStatus;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "friendship")
@IdClass(FriendsId.class)
public class FriendsEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    private UserEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "addressee_id", referencedColumnName = "id")
    private UserEntity friend;

    @Column(name = "created_date", columnDefinition = "DATE", nullable = false)
    private Date createdDate;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private FriendStatus status;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        FriendsEntity that = (FriendsEntity) o;
        return user != null && Objects.equals(user, that.user)
                && friend != null && Objects.equals(friend, that.friend);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(user, friend);
    }
}
