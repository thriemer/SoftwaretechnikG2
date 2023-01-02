package de.softwaretechnik.coder.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final User user = new User("linus", new BCryptPasswordEncoder().encode("password"));


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals(user.getUsername())) {
            return user;
        }
        throw new UsernameNotFoundException("User " + username + " not found");
    }
}
