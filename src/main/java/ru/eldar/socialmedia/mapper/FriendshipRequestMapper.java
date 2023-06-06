package ru.eldar.socialmedia.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.eldar.socialmedia.dto.FriendshipRequestDto;
import ru.eldar.socialmedia.entity.FriendshipRequest;

@Component
@RequiredArgsConstructor
public class FriendshipRequestMapper {

    private final ModelMapper mapper;

    public FriendshipRequestDto toDto(FriendshipRequest request) {
        return mapper.map(request, FriendshipRequestDto.class);
    }
}
