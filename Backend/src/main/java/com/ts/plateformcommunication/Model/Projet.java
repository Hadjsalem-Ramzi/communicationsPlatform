package com.ts.plateformcommunication.Model;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String contenu;
    public Projet() {

    }
    @JsonCreator
    public Projet(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("contenu") String contenu
    ) {
        this.id = id;
        this.name = name;
        this.contenu = contenu;
    }


    @JsonIgnore
    @OneToMany(mappedBy = "projet")
    private List<Tache> taches;
}
