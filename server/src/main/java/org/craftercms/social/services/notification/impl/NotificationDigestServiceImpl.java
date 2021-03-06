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

package org.craftercms.social.services.notification.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.craftercms.commons.i10n.I10nLogger;
import org.craftercms.profile.api.Profile;
import org.craftercms.social.exceptions.SocialException;
import org.craftercms.social.services.notification.NotificationDigestService;
import org.craftercms.social.services.system.EmailService;
import org.craftercms.social.util.LoggerFactory;
import org.craftercms.social.util.SocialFreemarkerLoader;
import org.craftercms.social.util.profile.ProfileAggregator;

/**
 *
 */
public class NotificationDigestServiceImpl implements NotificationDigestService {

    public static final String DEFAULT_LOADPATH = "classpath:/crafter/social/notifications";

    private I10nLogger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private ProfileAggregator profileAggregator;
    private SocialFreemarkerLoader socialFreemarkerLoader;
    private Configuration cfg;
    private EmailService emailService;
    private String systemDefaultLocale;


    public void setProfileAggregatorImpl(ProfileAggregator profileAggregator) {
        this.profileAggregator = profileAggregator;
    }

    @Override
    public void digest(final List<HashMap> auditDigest, final String profileId, final String type) {
        Profile toSend = profileAggregator.getProfile(profileId);
        if (toSend != null) {
            init();
            final HashMap<Object, Object> dataModel = new HashMap<>(2);
            dataModel.put("profile", toSend);

            for (HashMap hashMap : auditDigest) {
                try {
                    StringWriter writer = new StringWriter();
                    dataModel.put("digest", auditDigest);
                    Template template = cfg.getTemplate(hashMap.get("contextId") + "/" + type,getProfileLocale(toSend
                        .getAttribute("notificationLocale")));
                    final Environment env = template.createProcessingEnvironment(dataModel, writer);
                    env.process();
                    writer.flush();
                    emailService.sendEmail(toSend,writer,type,(String) hashMap.get("contextId"));
                } catch (IOException | TemplateException | SocialException ex) {
                    logger.error("logging.system.notification.errorLoadingTemplate", ex);
                }
            }
        } else {
            logger.error("Unable to send notification to profile {} it does not exist", profileId);
        }
    }

    private Locale getProfileLocale(final Object notificationLocale) {
        if(notificationLocale==null){
            return new Locale(systemDefaultLocale);
        }else{
            return new Locale(notificationLocale.toString());
        }

    }

    public void init() {
        cfg = new Configuration(Configuration.VERSION_2_3_21);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setOutputEncoding("UTF-8");
        cfg.setTemplateLoader(socialFreemarkerLoader);
    }


    public void setSocialFreemarkerLoader(SocialFreemarkerLoader socialFreemarkerLoader) {
        this.socialFreemarkerLoader = socialFreemarkerLoader;
    }

    public void setEmailService(final EmailService emailService) {
        this.emailService = emailService;
    }

    public void setSystemDefaultLocale(final String systemDefaultLocale) {
        this.systemDefaultLocale = systemDefaultLocale;
    }
}
