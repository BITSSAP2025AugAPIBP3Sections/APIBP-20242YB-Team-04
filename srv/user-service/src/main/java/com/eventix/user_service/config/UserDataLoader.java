package com.eventix.user_service.config;

import com.eventix.user_service.model.Role;
import com.eventix.user_service.model.User;
import com.eventix.user_service.repository.RoleRepository;
import com.eventix.user_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class UserDataLoader {

    @Bean
    CommandLineRunner loadUserData(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create roles if they don't exist
            Role attendeeRole = roleRepository.findByName("attendee")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("attendee");
                        return roleRepository.save(role);
                    });
            
            Role organizerRole = roleRepository.findByName("organizer")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("organizer");
                        return roleRepository.save(role);
                    });
            
            if (userRepository.count() == 0) {
                System.out.println("🔄 Loading sample user data...");
                
                // User 1 - Main Organizer
                User user1 = new User();
                user1.setFirstName("Rajesh");
                user1.setLastName("Kumar");
                user1.setPhoneNumber("9876543210");
                user1.setEmail("rajesh.kumar@eventix.com");
                user1.setPassword(passwordEncoder.encode("password123"));
                user1.setActive(true);
                user1.setEmailVerified(true);
                user1.setRoles(Set.of(organizerRole));
                user1.setOrganizerId(1001L);
                userRepository.save(user1);
                
                // User 2 - Tech Organizer
                User user2 = new User();
                user2.setFirstName("Priya");
                user2.setLastName("Sharma");
                user2.setPhoneNumber("9876543211");
                user2.setEmail("priya.sharma@eventix.com");
                user2.setPassword(passwordEncoder.encode("password123"));
                user2.setActive(true);
                user2.setEmailVerified(true);
                user2.setRoles(Set.of(organizerRole));
                user2.setOrganizerId(1002L);
                userRepository.save(user2);
                
                // User 3 - Social Welfare Organizer
                User user3 = new User();
                user3.setFirstName("Amit");
                user3.setLastName("Patel");
                user3.setPhoneNumber("9876543212");
                user3.setEmail("amit.patel@eventix.com");
                user3.setPassword(passwordEncoder.encode("password123"));
                user3.setActive(true);
                user3.setEmailVerified(true);
                user3.setRoles(Set.of(organizerRole));
                user3.setOrganizerId(1003L);
                userRepository.save(user3);
                
                // User 4 - NGO Organizer
                User user4 = new User();
                user4.setFirstName("Meera");
                user4.setLastName("Iyer");
                user4.setPhoneNumber("9876543213");
                user4.setEmail("meera.iyer@eventix.com");
                user4.setPassword(passwordEncoder.encode("password123"));
                user4.setActive(true);
                user4.setEmailVerified(true);
                user4.setRoles(Set.of(organizerRole));
                user4.setOrganizerId(1004L);
                userRepository.save(user4);
                
                // User 5 - Corporate Organizer
                User user5 = new User();
                user5.setFirstName("Arjun");
                user5.setLastName("Mehta");
                user5.setPhoneNumber("9876543214");
                user5.setEmail("arjun.mehta@eventix.com");
                user5.setPassword(passwordEncoder.encode("password123"));
                user5.setActive(true);
                user5.setEmailVerified(true);
                user5.setRoles(Set.of(organizerRole));
                user5.setOrganizerId(1005L);
                userRepository.save(user5);
                
                // User 6 - Community Organizer
                User user6 = new User();
                user6.setFirstName("Kavya");
                user6.setLastName("Nair");
                user6.setPhoneNumber("9876543215");
                user6.setEmail("kavya.nair@eventix.com");
                user6.setPassword(passwordEncoder.encode("password123"));
                user6.setActive(true);
                user6.setEmailVerified(true);
                user6.setRoles(Set.of(organizerRole));
                user6.setOrganizerId(1006L);
                userRepository.save(user6);
                
                // User 7 - Attendee
                User user7 = new User();
                user7.setFirstName("Sneha");
                user7.setLastName("Reddy");
                user7.setPhoneNumber("9876543216");
                user7.setEmail("sneha.reddy@eventix.com");
                user7.setPassword(passwordEncoder.encode("password123"));
                user7.setActive(true);
                user7.setEmailVerified(true);
                user7.setRoles(Set.of(attendeeRole));
                userRepository.save(user7);
                
                // User 8 - Attendee
                User user8 = new User();
                user8.setFirstName("Vikram");
                user8.setLastName("Singh");
                user8.setPhoneNumber("9876543217");
                user8.setEmail("vikram.singh@eventix.com");
                user8.setPassword(passwordEncoder.encode("password123"));
                user8.setActive(true);
                user8.setEmailVerified(true);
                user8.setRoles(Set.of(attendeeRole));
                userRepository.save(user8);
                
                // User 9 - Attendee
                User user9 = new User();
                user9.setFirstName("Ananya");
                user9.setLastName("Joshi");
                user9.setPhoneNumber("9876543218");
                user9.setEmail("ananya.joshi@eventix.com");
                user9.setPassword(passwordEncoder.encode("password123"));
                user9.setActive(true);
                user9.setEmailVerified(true);
                user9.setRoles(Set.of(attendeeRole));
                userRepository.save(user9);
                
                // User 10 - Attendee
                User user10 = new User();
                user10.setFirstName("Rohan");
                user10.setLastName("Desai");
                user10.setPhoneNumber("9876543219");
                user10.setEmail("rohan.desai@eventix.com");
                user10.setPassword(passwordEncoder.encode("password123"));
                user10.setActive(true);
                user10.setEmailVerified(true);
                user10.setRoles(Set.of(attendeeRole));
                userRepository.save(user10);
                
                // User 11 - Attendee
                User user11 = new User();
                user11.setFirstName("Ishita");
                user11.setLastName("Kapoor");
                user11.setPhoneNumber("9876543220");
                user11.setEmail("ishita.kapoor@eventix.com");
                user11.setPassword(passwordEncoder.encode("password123"));
                user11.setActive(true);
                user11.setEmailVerified(true);
                user11.setRoles(Set.of(attendeeRole));
                userRepository.save(user11);
                
                // User 12 - Attendee
                User user12 = new User();
                user12.setFirstName("Karthik");
                user12.setLastName("Ramesh");
                user12.setPhoneNumber("9876543221");
                user12.setEmail("karthik.ramesh@eventix.com");
                user12.setPassword(passwordEncoder.encode("password123"));
                user12.setActive(true);
                user12.setEmailVerified(true);
                user12.setRoles(Set.of(attendeeRole));
                userRepository.save(user12);
                
                // User 13 - Attendee
                User user13 = new User();
                user13.setFirstName("Divya");
                user13.setLastName("Bhat");
                user13.setPhoneNumber("9876543222");
                user13.setEmail("divya.bhat@eventix.com");
                user13.setPassword(passwordEncoder.encode("password123"));
                user13.setActive(true);
                user13.setEmailVerified(true);
                user13.setRoles(Set.of(attendeeRole));
                userRepository.save(user13);
                
                // User 14 - Attendee
                User user14 = new User();
                user14.setFirstName("Aditya");
                user14.setLastName("Verma");
                user14.setPhoneNumber("9876543223");
                user14.setEmail("aditya.verma@eventix.com");
                user14.setPassword(passwordEncoder.encode("password123"));
                user14.setActive(true);
                user14.setEmailVerified(true);
                user14.setRoles(Set.of(attendeeRole));
                userRepository.save(user14);
                
                // User 15 - Attendee
                User user15 = new User();
                user15.setFirstName("Pooja");
                user15.setLastName("Menon");
                user15.setPhoneNumber("9876543224");
                user15.setEmail("pooja.menon@eventix.com");
                user15.setPassword(passwordEncoder.encode("password123"));
                user15.setActive(true);
                user15.setEmailVerified(true);
                user15.setRoles(Set.of(attendeeRole));
                userRepository.save(user15);
                
                System.out.println("✅ Successfully loaded 15 sample users!");
                System.out.println("📊 User IDs: 1-15");
                System.out.println("👥 Users 1-6: organizers (Organizer IDs: 1001-1006)");
                System.out.println("👥 Users 7-15: attendees");
                System.out.println("🔐 All passwords: password123");
                System.out.println("📧 Emails: {firstname.lastname}@eventix.com");
            }
        };
    }
}
