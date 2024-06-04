package co.com.bancolombia.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder(toBuilder = true)
public class UserRequest {

    Long id;
}
