package com.github.project2.repository.userPrincipal;


import com.github.project2.repository.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_Principal")
public class UserPrincipal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_principal_id")
    private Long userPrincipalId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;  // UserEntity와 1:1 관계

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "userPrincipal")
    private Collection<UserPrincipalRoles> userPrincipalRoles;
}
