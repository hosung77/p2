package com.github.project2.repository.user;

import com.github.project2.repository.userPrincipal.UserPrincipal;
import lombok.*;
import org.hibernate.Hibernate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", nullable = false)
    private String userName;

    @Column(name = "phone_num",nullable = false, unique = true)
    private String phoneNum;

    @NotNull(message = "주소는 널이어서는 안됩니다.")
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String gender;

    @OneToOne(mappedBy = "user")  // UserPrincipal과 일대일 관계
    private UserPrincipal userPrincipal;  // UserPrincipal을 참조하는 변수

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        UserEntity that = (UserEntity) o;
        return userId != null && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}