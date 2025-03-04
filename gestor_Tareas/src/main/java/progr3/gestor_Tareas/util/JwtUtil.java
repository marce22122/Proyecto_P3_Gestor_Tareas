package progr3.gestor_Tareas.util;

import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtUtil {
	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expirationMs}")
	private long jwtExpirationMs;

	   private SecretKey getSigningKey() {
	        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	    }

	    public String extractUsername(String token) {
	        return extractClaim(token, Claims::getSubject);
	    }

	    public String extractRole(String token) {
	        return extractClaim(token, claims -> claims.get("role", String.class));
	    }

	    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }

	    private Claims extractAllClaims(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey(getSigningKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }

	    public String generateToken(UserDetails userDetails) {
	        String role = userDetails.getAuthorities().stream()
	                .map(auth -> auth.getAuthority())
	                .findFirst()
	                .orElse("ROLE_USER");

	        return Jwts.builder()
	                .setSubject(userDetails.getUsername())
	                .claim("role", role)
	                .setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
	                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
	                .compact();
	    }

	    public boolean validateToken(String token, UserDetails userDetails) {
	        final String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }

	    private boolean isTokenExpired(String token) {
	        return extractClaim(token, Claims::getExpiration).before(new Date());
	    }

	    public UsernamePasswordAuthenticationToken getAuthenticationToken(String token, UserDetails userDetails) {
	        String role = extractRole(token);
	        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

	        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
	    }
}
