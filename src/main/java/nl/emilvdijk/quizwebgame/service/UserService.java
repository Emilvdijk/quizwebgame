//package nl.emilvdijk.quizwebgame.service;
//
//import nl.emilvdijk.quizwebgame.entity.MyUser;
//import nl.emilvdijk.quizwebgame.repository.UserRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserService implements UserDetailsService {
//
//    @Autowired
//    UserRepo userRepo;
//
//    /**
//     * save the user to the repository
//     * @param myUser user to be saved
//     */
//    public void save(MyUser myUser){
//        userRepo.save(myUser);
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        final MyUser myUser = this.userRepo.findByUsername(username);
//        if(myUser == null) {
//            throw new UsernameNotFoundException("Unknown user "+ username);
//        }
//        return User.withUsername(myUser.getUsername())
//                .password(myUser.getPassword())
//                .authorities(myUser.getAuthorities())
//                .build();
//    }
//}
