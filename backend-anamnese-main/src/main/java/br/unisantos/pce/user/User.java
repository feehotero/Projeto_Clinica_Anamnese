package br.unisantos.pce.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Getter
@Table(name = "tb_usuario")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_matricula")
	private Integer id;

	@Column(name = "nm_usuario", length = 60, nullable = false, unique = true)
	private String login;

	@Column(name = "nm_nome_completo", length = 100, nullable = false)
	private String nome;

	@Column(name = "nm_senha", length = 60, nullable = false)
	private String password;

	@Column(name = "user_role", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole role;

	@Column(name = "dt_criacao", nullable = false)
	private LocalDateTime criadoEm;

	@PrePersist
	protected void onCreate() {
		criadoEm = LocalDateTime.now();
	}

	public User(String login, String password, String nome, UserRole role) {
		this.login = login;
		this.password = password;
		this.nome = nome;
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (this.role == UserRole.ADMIN) {
			return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
		}

		return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.login;
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