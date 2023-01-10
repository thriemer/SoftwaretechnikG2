package de.softwaretechnik.coder.adapter.secondary;

import de.softwaretechnik.coder.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void saveUser_readAgain_usersAreEqual(){
        //arrange
        User user = User.builder().userName("myUser").password("1234").build();
        //act
        userRepository.save(user);
        var actual = userRepository.findByUserName("myUser").get();
        //assert
        Assertions.assertEquals(user.getUsername(),actual.getUsername());
        Assertions.assertEquals(user.getPassword(),actual.getPassword());
        Assertions.assertEquals(user.isAccountNonExpired(),actual.isAccountNonExpired());
        Assertions.assertEquals(user.isAccountNonLocked(),actual.isAccountNonLocked());
        Assertions.assertEquals(user.isCredentialsNonExpired(),actual.isCredentialsNonExpired());
        Assertions.assertEquals(user.isEnabled(),actual.isEnabled());
    }

}
