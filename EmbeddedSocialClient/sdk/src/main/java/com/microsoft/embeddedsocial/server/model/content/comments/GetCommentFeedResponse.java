/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */

package com.microsoft.embeddedsocial.server.model.content.comments;

import com.microsoft.embeddedsocial.autorest.models.FeedResponseCommentView;
import com.microsoft.embeddedsocial.server.model.ListResponse;
import com.microsoft.embeddedsocial.server.model.view.CommentView;

import java.util.ArrayList;
import java.util.List;

public class GetCommentFeedResponse extends ListResponse<CommentView> {

    private List<CommentView> comments;

    public GetCommentFeedResponse(List<CommentView> comments) {
        this.comments = comments;
    }

    public GetCommentFeedResponse(FeedResponseCommentView response) {
        comments = new ArrayList<>();
        for (com.microsoft.embeddedsocial.autorest.models.CommentView comment : response.getData()) {
            comments.add(new CommentView(comment));
        }
        setContinuationKey(response.getCursor());
    }

    @Override
    public List<CommentView> getData() {
        return comments;
    }
}
