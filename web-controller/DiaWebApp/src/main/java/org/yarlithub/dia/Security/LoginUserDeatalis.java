package org.yarlithub.dia.Security;

/**
 * Created by john on 7/2/14.
 */

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.yarlithub.dia.repo.DataLayer;
import org.yarlithub.dia.repo.object.Garden;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;

public class LoginUserDeatalis implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

        final Garden gn=DataLayer.getGardenByName(username);
        if (gn==null) {
            throw new UsernameNotFoundException(username + " not found");
        }

        //creating dummy user details, should do JDBC operations
        return new UserDetails() {

            private static final long serialVersionUID = 2059202961588104658L;

            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public String getPassword() {
                return gn.getPassword();
            }

            //To DO for authentication.
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                List<SimpleGrantedAuthority> auths = new java.util.ArrayList<SimpleGrantedAuthority>();
                auths.add(new SimpleGrantedAuthority("Admin"));
                return auths;
            }
        };
    }

}
