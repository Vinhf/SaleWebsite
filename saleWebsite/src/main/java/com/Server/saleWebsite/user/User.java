package com.Server.saleWebsite.user;



import com.Server.saleWebsite.role.Role;
import com.Server.saleWebsite.token.token;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_id;
	private String avt;
	private String full_name;
	private String email;
	@Getter
    private String password;
	private String sex;
	private LocalDate birth_day;
	private String address;
	private String phone;
	private String school_name;
	@Builder.Default
	private boolean status = true;

	private Timestamp create_date;

	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	@BatchSize(size = 10)
	@JsonIgnore // Tránh việc vòng lặp khi serialize
	private List<token> tokens;


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public String getUsername() {
		return email;
	}


    @Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
