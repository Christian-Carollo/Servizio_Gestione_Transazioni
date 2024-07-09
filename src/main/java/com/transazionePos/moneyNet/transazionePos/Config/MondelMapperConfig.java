package com.transazionePos.moneyNet.transazionePos.Config;

import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
// Configurazione di ModelMapper per lo switch tra entity e dto automatico
@Configuration
public class MondelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
