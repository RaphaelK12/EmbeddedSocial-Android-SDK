/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */

package com.microsoft.embeddedsocial.service;

import com.google.firebase.iid.FirebaseInstanceIdService;

import com.microsoft.embeddedsocial.fcm.FcmTokenHolder;
import com.microsoft.embeddedsocial.service.worker.GetFcmIdWorker;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

/**
 * Listens to InstanceID API callbacks.
 */
public class FcmInstanceIdListenerService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FcmTokenHolder.create(this).resetToken();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(GetFcmIdWorker.class).build();
        WorkManager.getInstance().enqueue(workRequest);
    }
}
