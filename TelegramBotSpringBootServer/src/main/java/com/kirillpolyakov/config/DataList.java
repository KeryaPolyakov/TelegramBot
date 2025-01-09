package com.kirillpolyakov.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "project-settings")
public class DataList {

    private List<Compliment> list;

}
