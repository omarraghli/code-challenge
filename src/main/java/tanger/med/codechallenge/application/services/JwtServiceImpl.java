package tanger.med.codechallenge.application.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tanger.med.codechallenge.api.interfaces.JwtService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    private static final  String SECRET_KEY = "6d3f5845c0f040710236f42c366643435ed38e4d1c7aa48b41ae12b91816486d";
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public  <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final  Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    public Claims extractAllClaims(String token){
    return Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Map<String,Object> extraClaims,  UserDetails userDetails){
            return Jwts.builder()
                                .setClaims(extraClaims)
                                .setSubject(userDetails.getUsername())
                                .setIssuedAt(new Date(System.currentTimeMillis()))
                                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                                .compact();
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && ! isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return  extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
