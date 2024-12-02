package com.example.fitnessclubmanagementsystem.services;

import com.example.fitnessclubmanagementsystem.models.Member;
import com.example.fitnessclubmanagementsystem.models.Trainer;
import com.example.fitnessclubmanagementsystem.repositories.MemberRepository;
import com.example.fitnessclubmanagementsystem.repositories.TrainerRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom implementation of UserDetailsService to support user authentication.
 * Handles both trainers and members based on username or email.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for injecting required dependencies.
     *
     * @param memberRepository Repository for accessing member data.
     * @param trainerRepository Repository for accessing trainer data.
     * @param passwordEncoder Password encoder for securely storing passwords.
     */
    public CustomUserDetailsService(MemberRepository memberRepository,
                                    TrainerRepository trainerRepository,
                                    PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.trainerRepository = trainerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Loads user details by username for authentication.
     *
     * @param username The username (or email) provided during login.
     * @return A UserDetails object containing the user's information.
     * @throws UsernameNotFoundException If no user is found with the provided username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check if the username belongs to a trainer
        Trainer trainer = trainerRepository.findByName(username);
        if (trainer != null) {
            logger.info("Trainer found: {}", trainer.getName());
            return User.builder()
                    .username(trainer.getName())
                    .password(trainer.getPassword())
                    .roles("TRAINER")
                    .build();
        }

        // Check if the username belongs to a member
        Member member = memberRepository.findByEmail(username);
        if (member != null) {
            logger.info("Member found: {}", member.getEmail());
            return User.builder()
                    .username(member.getEmail())
                    .password(member.getPassword())
                    .roles("MEMBER")
                    .build();
        }

        logger.error("User not found: {}", username);
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
