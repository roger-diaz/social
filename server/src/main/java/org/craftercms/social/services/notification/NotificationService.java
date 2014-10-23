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
package org.craftercms.social.services.notification;

import org.craftercms.social.exceptions.NotificationException;

/**
 *
 */
public interface NotificationService {

    public static final String WEEKLY = "weekly";
    public static final String DAILY = "daily";

    void subscribeUser(final String userId, final String threadId, final String type) throws NotificationException;

    void notify(final String type);

    void unSubscribeUser(final String userId, String threadId) throws NotificationException;

    boolean isBeenWatch(final String ugcId, final String profileId) throws NotificationException;
}