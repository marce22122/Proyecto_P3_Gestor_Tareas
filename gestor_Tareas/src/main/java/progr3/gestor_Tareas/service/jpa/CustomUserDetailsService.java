package progr3.gestor_Tareas.service.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import progr3.gestor_Tareas.entity.Usuario;
import progr3.gestor_Tareas.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		// Convertimos el rol del usuario a una autoridad para Spring Security
		List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(usuario.getRole().name()));

		return new User(usuario.getEmail(), usuario.getPassword(), authorities);
	}

}
