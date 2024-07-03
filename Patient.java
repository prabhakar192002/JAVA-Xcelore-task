public class Patient {
    package com.example.demo.entity;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Size(min = 3)
    private String name;
    @NotEmpty
    @Size(max = 20)
    private String city;
    @Email
    private String email;
    @Pattern(regexp = "\\d{10}")
    private String phoneNumber;
    @NotEmpty
    private String symptom;

}

    
}
