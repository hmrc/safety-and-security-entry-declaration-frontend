#!/bin/bash

echo ""
echo "Applying migration SubmittedDeclarations"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /:lrn/submittedDeclarations                       controllers.SubmittedDeclarationsController.onPageLoad(lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "submittedDeclarations.title = submittedDeclarations" >> ../conf/messages.en
echo "submittedDeclarations.heading = submittedDeclarations" >> ../conf/messages.en

echo "Migration SubmittedDeclarations completed"
