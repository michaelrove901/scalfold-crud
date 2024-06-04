package co.com.bancolombia.api.dto;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserCreate {
  @NotBlank
  private String firstName;
  private String lastName;
  private String email;
  private String avatar;

}
