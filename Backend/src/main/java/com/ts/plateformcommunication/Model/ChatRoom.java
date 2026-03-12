package com.ts.plateformcommunication.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "chatroom")
    @ToString.Exclude
    @JsonIgnore
    private List<Message> message;
    @ManyToOne
    private Forum forum;

    @Override
    public String toString() {
        return "ChatRoom{id=" + id + ", name='" + name + "'}";
    }

}
