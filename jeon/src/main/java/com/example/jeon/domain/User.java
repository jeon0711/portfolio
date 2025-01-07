package com.example.jeon.domain;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User implements UserDetails {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id", updatable = false, nullable = false)
     @Setter(AccessLevel.NONE)
    private Long id;
     @Column(name="email",nullable = false,unique = true)
    private String email;
     @Column(name="password")
    private String password;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_profile_id", referencedColumnName = "id")
    private UserProfile userProfile;

     @Builder
    public User(String email,String password,String auth)
     {
         this.email=email;
         this.password=password;
     }
     @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
     {
         return List.of(new SimpleGrantedAuthority("user"));
     }
     @Override
    public String getUsername()
     {
         return email;
     }
     @Override
    public String getPassword()
     {
         return password;
     }
     @Override
    public boolean isAccountNonExpired()
     {
         return true;
     }
     @Override
    public boolean isAccountNonLocked()
     {
         return true;
     }
     @Override
    public boolean isCredentialsNonExpired()
     {
         return true;
     }
     @Override
    public boolean isEnabled()
     {
         return true;
     }
    public void setUserProfile(UserProfile userProfile) {
        if (this.userProfile != null) {
            this.userProfile.setUser(null); // 기존 관계 제거
        }
        this.userProfile = userProfile;
        if (userProfile != null) {
            userProfile.setUser(this); // 양방향 관계 설정
        }
    }
}
