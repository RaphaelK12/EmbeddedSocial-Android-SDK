/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *
 */

package com.microsoft.test.embeddedsocial.test;

import com.microsoft.embeddedsocial.EmbeddedSocialApplication;
import com.microsoft.embeddedsocial.autorest.models.PublisherType;
import com.microsoft.embeddedsocial.base.utils.debug.DebugLog;
import com.microsoft.embeddedsocial.server.EmbeddedSocialServiceProvider;
import com.microsoft.embeddedsocial.server.exception.NetworkRequestException;
import com.microsoft.embeddedsocial.server.model.UserRequest;
import com.microsoft.embeddedsocial.server.model.account.CreateUserRequest;
import com.microsoft.embeddedsocial.server.model.account.DeleteUserRequest;
import com.microsoft.embeddedsocial.server.model.auth.AuthenticationResponse;
import com.microsoft.embeddedsocial.server.model.content.topics.AddTopicRequest;
import com.microsoft.embeddedsocial.server.model.content.topics.RemoveTopicRequest;
import com.microsoft.test.embeddedsocial.TestConstants;
import com.microsoft.test.embeddedsocial.util.StringUtils;

import android.test.ApplicationTestCase;

public abstract class BaseRestServicesTest extends ApplicationTestCase<EmbeddedSocialApplication> {

    private static final int DELAYED_EXECUTION_TIMEOUT = 20 * 1000;

    private EmbeddedSocialServiceProvider serviceProvider;

    public BaseRestServicesTest() {
        super(EmbeddedSocialApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        serviceProvider = new EmbeddedSocialServiceProvider(getContext());
    }

    protected EmbeddedSocialServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    protected <T extends UserRequest> T prepareUserRequest(T userRequest,
                                           AuthenticationResponse response) {

        userRequest.setUserHandle(response.getUserHandle());
        userRequest.setUserSessionSignature(response.getSessionToken());
        userRequest.setAuthorization("Bearer " + response.getSessionToken());
        return userRequest;
    }

    protected AuthenticationResponse createRandomUser() throws NetworkRequestException {
        String firstName = StringUtils.generateName();
        String lastName = StringUtils.generateName();
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com";
        String username = firstName.toLowerCase() + lastName.toLowerCase();
        CreateUserRequest request = new CreateUserRequest
                .Builder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setInstanceId("1")
                // TODO this is broken and requires code to interface with AAD to get a token
                .build();
        return getServiceProvider().getAccountService().createUser(request);

    }

    protected void deleteUser(AuthenticationResponse response) throws NetworkRequestException {
//		UserRequest request = prepareUserRequest(new UserRequest(), response);
        getServiceProvider().getAccountService().deleteUser(new DeleteUserRequest());
    }

    protected String addTopic(AuthenticationResponse authenticationResponse)
            throws NetworkRequestException {
        return addTopic(authenticationResponse,
                TestConstants.TOPIC_TITLE,
                TestConstants.TOPIC_TEXT);
    }

    protected String addTopic(AuthenticationResponse authenticationResponse,
                              String topicTitle,
                              String topicText)
            throws NetworkRequestException {
        AddTopicRequest request = new AddTopicRequest.Builder()
                .setPublisherType(PublisherType.USER)
                .setTopicTitle(topicTitle)
                .setTopicText(topicText)
                .build();

        return serviceProvider.getContentService()
                .addTopic(prepareUserRequest(request, authenticationResponse)).getTopicHandle();
    }

    protected void removeTopic(AuthenticationResponse authenticationResponse, String topicHandle)
            throws NetworkRequestException {
        RemoveTopicRequest removeTopicRequest = new RemoveTopicRequest(topicHandle);
        serviceProvider.getContentService()
                .removeTopic(prepareUserRequest(removeTopicRequest, authenticationResponse));
    }

    protected void delay() {
        try {
            DebugLog.i("WAITING FOR " + DELAYED_EXECUTION_TIMEOUT + " ms");
            Thread.sleep(DELAYED_EXECUTION_TIMEOUT);
        } catch (InterruptedException e) {
            //nothing to do
        }
    }
}
