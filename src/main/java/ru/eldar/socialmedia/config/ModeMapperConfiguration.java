package ru.eldar.socialmedia.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.eldar.socialmedia.dto.user.RegistrationRequest;
import ru.eldar.socialmedia.entity.User;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class ModeMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        var mapper = new ModelMapper();

        mapper.getConfiguration()
              .setMatchingStrategy(MatchingStrategies.STRICT)
              .setFieldMatchingEnabled(true)
              .setSkipNullEnabled(true)
              .setFieldAccessLevel(PRIVATE);

        setPasswordMapping(mapper);

        return mapper;
    }

    private void setPasswordMapping(ModelMapper mapper) {
        var mappingType = mapper.typeMap(RegistrationRequest.class, User.class);

        mappingType.addMappings(m -> m.skip(User::setPassword));
    }

}
