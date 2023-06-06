package ru.eldar.socialmedia.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.eldar.socialmedia.dto.user.RegistrationRequest;
import ru.eldar.socialmedia.entity.User;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

/**
 * Mode mapper configuration class
 *
 * @author eldar
 */
@Configuration
public class ModeMapperConfiguration {
    /**
     * Method creates a model mapper bean and configures it
     *
     * @return modelMapper bean
     */
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
