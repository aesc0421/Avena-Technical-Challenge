package com.avenaio.technical_test.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * User entity for the habit tracking system
 */
@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @NotBlank
    @Size(min = 3, max = 50)
    @Indexed(unique = true)
    private String username;

    @NotBlank
    @Size(max = 100)
    @Field("first_name")
    private String firstName;

    @NotBlank
    @Size(max = 100)
    @Field("last_name")
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 100)
    @Indexed(unique = true)
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    @Field("habit_records")
    private List<String> habitRecordIds = new ArrayList<>();

    public String getFullName() {
        return firstName + " " + lastName;
    }
}