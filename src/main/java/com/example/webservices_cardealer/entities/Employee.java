package com.example.webservices_cardealer.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    private static final long serialVersionUID = -4711394998133225957L;
    @Id
    private String employeeId;
    @NotEmpty(message = "Firstname can not be empty")
    @Size(min = 3, max = 10, message = "Firstname length invalid")
    private String firstname;
    @NotEmpty(message = "Lastname can not be empty")
    @Size(min = 3, max = 10 , message = "Lastname length invalid")
    private String lastname;
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate birthdate;
    @Pattern(regexp = "([0-9]){2,4}-([0-9]){5,8}", message = "Phone number not valid")
    private String phone;
    @Email(message = "E-mail address invalid")
    private String email;
    @Size(min = 4, max = 10, message = "Username length invalid")
    @NotBlank(message = "Username must contain a value")
    @Field("username")
    @Indexed(unique = true)
    private String username;
    @Size(min = 4, max = 10, message = "Password length invalid")
    @NotBlank(message = "Password must contain a value")
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private List<String> acl;
}
