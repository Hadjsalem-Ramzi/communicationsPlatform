package com.ts.plateformcommunication.Model;
import com.ts.plateformcommunication.security.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String contenu;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @ManyToOne
    private Projet projet;
}
