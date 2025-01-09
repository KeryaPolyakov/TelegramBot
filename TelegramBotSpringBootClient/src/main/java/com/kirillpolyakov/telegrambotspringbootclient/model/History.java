package com.kirillpolyakov.telegrambotspringbootclient.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class History {

    private long id;

    @NonNull
    private String text;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime localDateTime = LocalDateTime.now();

    @JsonIgnore
    @ToString.Exclude
    private UserTelegram userTelegram;
}
