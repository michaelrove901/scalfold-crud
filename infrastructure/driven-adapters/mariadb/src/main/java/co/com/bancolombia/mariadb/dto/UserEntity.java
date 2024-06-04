package co.com.bancolombia.mariadb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("user")
public class UserEntity {
    @Id
    private Long id;
    @NotBlank
    @Column("firstName")
    private String firstName;
    @Column("lastName")
    private String lastName;
    private String email;
    private String avatar;
}
