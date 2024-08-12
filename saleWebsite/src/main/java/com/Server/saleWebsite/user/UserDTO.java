package com.Server.saleWebsite.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String avt;
    private String full_name;
    private String email;
    private String password;
    private String sex;
    private LocalDate birth_day;
    private String address;
    private String phone;
    private String school_name;

    private Timestamp create_date;
    private String role;
    private boolean status;
}
