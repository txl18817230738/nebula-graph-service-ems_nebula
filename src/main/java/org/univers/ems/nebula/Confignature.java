package org.univers.ems.nebula;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Confignature {
    @Value("${nebula.address}")
    public String address;

    @Value("${nebula.username}")
    public String username;

    @Value("${nebula.password}")
    public String password;

    @Value("${nebula.reconnect}")
    public Boolean reconnect;

    @Value("${nebula.space}")
    public String spaceName;

}
