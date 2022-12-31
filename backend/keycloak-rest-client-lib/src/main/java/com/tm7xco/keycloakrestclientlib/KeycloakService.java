package com.tm7xco.keycloakrestclientlib;

import org.springframework.stereotype.Service;

@Service
public interface KeycloakService {

    String getUserBearerToken(String username, String password);

    String getUsernameById(String userId);

    boolean createKeycloakUser(String username, String password, boolean enabled);

    boolean deleteKeycloakUser(String userId);

    boolean updateUserPassword(String userId, String newPassword);

    boolean addRoleToUser(String userId, String roleName);

    boolean revokeRoleFromUser(String userId, String role);

}
