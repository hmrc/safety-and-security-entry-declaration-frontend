#!/bin/bash

echo ""
echo "Applying migration OrganisationAccountRequired"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /:lrn/organisationAccountRequired                       controllers.package.OrganisationAccountRequiredController.onPageLoad(lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "organisationAccountRequired.title = organisationAccountRequired" >> ../conf/messages.en
echo "organisationAccountRequired.heading = organisationAccountRequired" >> ../conf/messages.en

echo "Migration OrganisationAccountRequired completed"
