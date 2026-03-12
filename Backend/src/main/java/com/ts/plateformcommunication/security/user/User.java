package com.ts.plateformcommunication.security.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ts.plateformcommunication.Model.Fichier;
import com.ts.plateformcommunication.Model.Message;
import com.ts.plateformcommunication.Model.Tache;
import com.ts.plateformcommunication.security.token.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Long id;
  private String firstname;
  private String lastname;
  private String email;
  private String password;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Enumerated(EnumType.STRING)
  private Role role;

  @JsonIgnore
  @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
  private List<Token> tokens;


  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private List<Fichier> fichiers;
  @JsonIgnore
  @OneToMany(mappedBy = "sender")
  private List<Message> messagesSend;
  @JsonIgnore
  @OneToMany(mappedBy = "receiver")
  private List<Message> messagesRece;
  @JsonIgnore
  @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
  private List<Tache> taches;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // Vérifiez si le rôle est null et renvoyez une liste vide si c'est le cas
    return (role != null) ? role.getAuthorities() : Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return password;
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
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
