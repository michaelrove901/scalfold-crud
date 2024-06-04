package co.com.bancolombia.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
public class UserUpdate {
    Long id;
    String firstName;
    String lastName;
    String email;
    String avatar;

}
