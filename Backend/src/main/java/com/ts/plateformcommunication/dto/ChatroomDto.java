package com.ts.plateformcommunication.dto;
import com.ts.plateformcommunication.Model.Forum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatroomDto {
    private Long id;
    private String name;
    private Forum forum;
}
