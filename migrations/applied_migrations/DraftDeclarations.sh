#!/bin/bash

echo ""
echo "Applying migration DraftDeclarations"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /:lrn/draftDeclarations                       controllers.declarations.DraftDeclarationsController.onPageLoad(lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "draftDeclarations.title = draftDeclarations" >> ../conf/messages.en
echo "draftDeclarations.heading = draftDeclarations" >> ../conf/messages.en

echo "Migration DraftDeclarations completed"
