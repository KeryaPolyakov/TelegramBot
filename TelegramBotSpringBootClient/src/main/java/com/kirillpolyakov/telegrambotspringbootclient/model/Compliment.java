package com.kirillpolyakov.telegrambotspringbootclient.model;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Compliment {

    private long id;

    @NonNull
    private String text;
}
