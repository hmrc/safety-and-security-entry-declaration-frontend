#!/bin/bash

echo ""
echo "Applying migration AmendForRejected"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /:lrn/amendForRejected                       controllers.declarations.AmendForRejectedController.onPageLoad(lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "amendForRejected.title = amendForRejected" >> ../conf/messages.en
echo "amendForRejected.heading = amendForRejected" >> ../conf/messages.en

echo "Migration AmendForRejected completed"
