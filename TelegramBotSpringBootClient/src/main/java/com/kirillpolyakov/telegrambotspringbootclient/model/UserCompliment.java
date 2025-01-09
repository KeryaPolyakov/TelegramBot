package com.kirillpolyakov.telegrambotspringbootclient.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class UserCompliment {

    private long id;

    @NonNull
    private String text;

    @NonNull
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime localDateTime;

    @ToString.Exclude
    @JsonIgnore
    private UserTelegram userTelegram;
}
