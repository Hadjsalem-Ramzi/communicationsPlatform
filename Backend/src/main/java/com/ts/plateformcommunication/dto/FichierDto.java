package com.ts.plateformcommunication.dto;

import com.ts.plateformcommunication.security.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FichierDto {
    private Long id;
    private String name;
    private  String contenu;
    private User user;
}
