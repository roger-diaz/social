package org.craftercms.social.security;

import java.util.LinkedHashSet;
import java.util.List;

import org.craftercms.commons.mongo.MongoDataException;
import org.craftercms.commons.security.permissions.Permission;
import org.craftercms.security.exception.AccessDeniedException;
import org.craftercms.social.repositories.security.PermissionRepository;

/**
 *
 */
public class SocialPermission implements Permission {

    private final String contextId;
    private List<String> profileRoles;
    private PermissionRepository repository;

    public SocialPermission(final List<String> profileRoles, final PermissionRepository repository,
                            final String contextId) {
        this.profileRoles = profileRoles;
        this.repository = repository;
        this.contextId = contextId;
    }

    @Override
    public boolean isAllowed(final String action) {
        try {
            return repository.isAllowed(action, new LinkedHashSet<>(profileRoles), contextId);
        } catch (MongoDataException e) {
            throw new AccessDeniedException("Unable to find Action", e);
        }
    }

}
