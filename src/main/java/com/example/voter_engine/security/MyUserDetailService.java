package com.example.voter_engine.security;

import com.example.voter_engine.Entity.user;
import com.example.voter_engine.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private userRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       Optional<user> user = Optional.ofNullable(userRepository.findByGmail(email));

       user.orElseThrow(()-> new UsernameNotFoundException("not found"+ email));
       return user.map(MyUserDetails::new).get();
    }
}
