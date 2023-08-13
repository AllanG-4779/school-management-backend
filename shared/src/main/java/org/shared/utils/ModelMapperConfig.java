package org.shared.utils;

import org.mapstruct.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperConfig {
   @Bean
   public ModelMapper modelMapper(){
      return new ModelMapper();
   }
}
