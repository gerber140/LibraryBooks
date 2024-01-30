package pl.kurs.librarybooks.config;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeansConfig {
    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }
}
