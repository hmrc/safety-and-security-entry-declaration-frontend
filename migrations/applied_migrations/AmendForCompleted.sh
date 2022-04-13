#!/bin/bash

echo ""
echo "Applying migration AmendForCompleted"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /:lrn/amendForCompleted                       controllers.declarations.AmendForCompletedController.onPageLoad(lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "amendForCompleted.title = amendForCompleted" >> ../conf/messages.en
echo "amendForCompleted.heading = amendForCompleted" >> ../conf/messages.en

echo "Migration AmendForCompleted completed"
