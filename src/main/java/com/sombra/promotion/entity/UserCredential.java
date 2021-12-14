package com.sombra.promotion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "user_credential")
@Getter
@Setter
@ToString(exclude = {"id", "jwtTokens", "roles"})
@EqualsAndHashCode(exclude = {"id", "jwtTokens", "roles"})
@NoArgsConstructor
public class UserCredential implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_credential_roles",
            joinColumns = {@JoinColumn(name = "user_credential_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Collection<Role> roles = new ArrayList<>();

    @Column(name = "last_logged")
    private LocalDateTime lastLogged;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "deleted")
    private boolean deleted;

    @JsonIgnore
    @OneToOne(mappedBy = "userCredential", cascade = CascadeType.MERGE)
    private JwtToken jwtToken;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().getAuthority()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}

