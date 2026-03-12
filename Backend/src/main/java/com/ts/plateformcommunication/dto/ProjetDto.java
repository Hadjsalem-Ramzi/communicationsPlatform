package com.ts.plateformcommunication.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjetDto {
    private Long id;
    private String name;
    private String contenu;
}
