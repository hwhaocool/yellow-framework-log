package com.github.hwhaocool.log;

import com.github.hwhaocool.log.YellowLog4J2LoggingSystem.ProfileEnum;
/**
 * @author yellowtail
 * @since 2022/8/30 22:05
 */
public class ProfileHolder {

    static ProfileEnum profileEnum;

    public static ProfileEnum getProfileEnum() {
        return profileEnum;
    }

    public static void setProfileEnum(ProfileEnum profileEnum) {
        ProfileHolder.profileEnum = profileEnum;
    }
}
