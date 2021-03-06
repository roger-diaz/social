package org.craftercms.social.security;

import org.bson.types.ObjectId;
import org.craftercms.commons.security.exception.PermissionException;
import org.craftercms.commons.security.permissions.Permission;
import org.craftercms.commons.security.permissions.PermissionResolver;
import org.craftercms.profile.api.Profile;
import org.craftercms.social.domain.UGC;
import org.craftercms.social.exceptions.UGCException;
import org.craftercms.social.repositories.security.PermissionRepository;
import org.craftercms.social.services.ugc.UGCService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class SocialPermissionResolver<T> implements PermissionResolver<Profile, T> {

    protected PermissionRepository permissionRepository;
    protected UGCService ugcService;
    private Logger log = LoggerFactory.getLogger(SocialPermissionResolver.class);

    @Override
    public Permission getGlobalPermission(final Profile subject) throws PermissionException {
        return getPermission(subject, null);
    }

    @Override
    public Permission getPermission(final Profile subject, final T object) throws PermissionException {
        if (object instanceof String) {
            String ugcId = (String)object;
            if (ObjectId.isValid(ugcId)) {
                try {
                    final UGC ugc = ugcService.read(ugcId, SocialSecurityUtils.getContext());
                    if (ugc != null) {
                        if (subject.getId().equals(ugc.getCreatedBy())) {
                            subject.getRoles().add("OWNER");
                        }
                    }
                } catch (UGCException e) {
                    log.error("Unable to find UGC with id " + ugcId, e);
                }
            }
        }
        return new SocialPermission(SocialSecurityUtils.getSocialRoles(), permissionRepository,
            SocialSecurityUtils.getContext());
    }

    public void setPermissionRepository(final PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public void setUgcService(UGCService ugcService) {
        this.ugcService = ugcService;
    }

}
