/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */

package com.microsoft.embeddedsocial.sdk;

import com.microsoft.embeddedsocial.ui.theme.ThemeGroup;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Embedded Social library options.
 */
@SuppressWarnings("FieldCanBeLocal")
public final class Options {

    private Application application = null;
    private IdProviders idProviders = null;
    private DrawTheme theme = null;

    private Options() {
    }

    void verify() {
        checkValueIsNotNull("application", application);
        checkValueIsNotNull("idProviders", idProviders);
        checkValueIsNotNull("idProviders.facebook", idProviders.facebook);
        checkValueIsNotNull("idProviders.twitter", idProviders.twitter);
        checkValueIsNotNull("idProviders.google", idProviders.google);
        checkValueIsNotNull("idProviders.microsoft", idProviders.microsoft);
        checkValueIsNotEmpty("application.serverUrl", application.serverUrl);
        checkValueIsNotEmpty("application.appKey", application.appKey);
        checkValueIsNotEmpty("idProviders.facebook.clientId", idProviders.facebook.clientId);
        checkValueIsNotEmpty("idProviders.microsoft.clientId", idProviders.microsoft.clientId);
        checkValueIsNotEmpty("idProviders.google.clientId", idProviders.google.clientId);

        if (application.numberOfCommentsToShow <= 0) {
            throwInvalidConfigException("application.numberOfCommentsToShow must be greater then 0");
        }
        if (application.numberOfRepliesToShow <= 0) {
            throwInvalidConfigException("application.numberOfRepliesToShow must be greater then 0");
        }
        if (!(idProviders.facebook.loginEnabled
            || idProviders.google.loginEnabled
            || idProviders.microsoft.loginEnabled
            || idProviders.twitter.loginEnabled)) {
            throwInvalidConfigException("login via at least one social network must be enabled");
        }
    }

    private void throwInvalidConfigException(String message) {
        throw new IllegalArgumentException("Invalid EmbeddedSocial configuration file: " + message);
    }

    private void checkValueIsNotEmpty(String name, String value) {
        if (TextUtils.isEmpty(value)) {
            throw new IllegalArgumentException("field \"" + name + " must be not empty");
        }
    }

    private void checkValueIsNotNull(String name, Object value) {
        if (value == null) {
            throwInvalidConfigException("field \"" + name + "\" is missed");
        }
    }

    public int getNumberOfCommentsToShow() {
        return application.numberOfCommentsToShow;
    }

    public int getNumberOfRepliesToShow() {
        return application.numberOfRepliesToShow;
    }

    public boolean isFacebookLoginEnabled() {
        return idProviders.facebook.loginEnabled;
    }

    public boolean isTwitterLoginEnabled() {
        return idProviders.twitter.loginEnabled;
    }

    public boolean isMicrosoftLoginEnabled() {
        return idProviders.microsoft.loginEnabled;
    }

    public boolean isGoogleLoginEnabled() {
        return idProviders.google.loginEnabled;
    }

    public String getServerUrl() {
        return application.serverUrl;
    }

    public String getAppKey() {
        return application.appKey;
    }

    public void setAppKey(String appKey) {
        application.appKey = appKey;
    }

    public boolean isSearchEnabled() {
        return application.searchEnabled;
    }

    public boolean showGalleryView() {
        return application.showGalleryView;
    }

    public boolean userTopicsEnabled() { return application.userTopicsEnabled; }

    public boolean userRelationsEnabled() { return application.userRelationsEnabled; }

    public String getTelemetryToken() {
        return application.telemetryToken;
    }

    public void setTelemetryToken(String telemetryToken) {
        application.telemetryToken = telemetryToken;
    }

    public List<String> disableNavigationDrawerForActivities() {
        return application.disableNavigationDrawerForActivities;
    }

    public String getFacebookApplicationId() {
        return idProviders.facebook.clientId;
    }

    public String getMicrosoftClientId() {
        return idProviders.microsoft.clientId;
    }

    public String getGoogleClientId() {
        return idProviders.google.clientId;
    }

    public ThemeGroup getThemeGroup() {
        return (theme == null || theme.name == null) ? ThemeGroup.LIGHT : theme.name;
    }

    public int getBaseTheme() {
        switch (getThemeGroup()) {
            case DARK:
                return R.style.EmbeddedSocialSdkAppTheme_Dark_Base;
            case LIGHT:
            default:
                return R.style.EmbeddedSocialSdkAppTheme_LightBase;
        }
    }

    /**
     * General application's options.
     */
    private static class Application {

        private static final int DEFAULT_NUMBER_OF_DISCUSSION_ITEMS = 20;

        private String serverUrl = null;
        private String appKey = null;
        private String telemetryToken = null;
        private boolean searchEnabled = true;
        private boolean showGalleryView = true;
        private boolean userTopicsEnabled = true;
        private boolean userRelationsEnabled = true;
        private List<String> disableNavigationDrawerForActivities = new ArrayList<>();
        private int numberOfCommentsToShow = DEFAULT_NUMBER_OF_DISCUSSION_ITEMS;
        private int numberOfRepliesToShow = DEFAULT_NUMBER_OF_DISCUSSION_ITEMS;
    }

    /**
     * Configs for an Id provider.
     */
    private static class IdProvider {
        private boolean loginEnabled = true;
        private String clientId = null;
    }

    /**
     * Choices for a third party Id providers currently supported.
     */
    @SuppressWarnings("unused")
    private static class IdProviders {
        private IdProvider facebook;
        private IdProvider google;
        private IdProvider microsoft;
        private IdProvider twitter;
    }

    private class DrawTheme {
        private ThemeGroup name;
    }
}
