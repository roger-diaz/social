/*
 * Copyright (C) 2007-2014 Crafter Software Corporation.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.social.services.system.impl;

import java.util.Map;

import org.craftercms.social.exceptions.SocialException;
import org.craftercms.social.repositories.system.ContextPreferencesRepository;
import org.craftercms.social.services.system.ContextPreferencesService;

/**
 *
 */
public class ContextPreferencesServiceImpl implements ContextPreferencesService{
    private ContextPreferencesRepository contextPreferencesRepository;

    @Override
    public Map findEmailPreference(final String contextId) throws SocialException {
        return (Map)contextPreferencesRepository.findEmailPreference(contextId);
    }

    @Override
    public String getNotificationEmailTemplate(final String contextId, final String notificationType) throws SocialException {
        return contextPreferencesRepository.findNotificationTemplate(contextId,notificationType);

    }

    public void setContextPreferencesRepository(final ContextPreferencesRepository contextPreferencesRepository) {
        this.contextPreferencesRepository = contextPreferencesRepository;
    }
}
