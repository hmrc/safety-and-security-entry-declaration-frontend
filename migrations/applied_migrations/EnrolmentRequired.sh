#!/bin/bash

echo ""
echo "Applying migration EnrolmentRequired"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /:lrn/enrolmentRequired                       controllers.package.EnrolmentRequiredController.onPageLoad(lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "enrolmentRequired.title = enrolmentRequired" >> ../conf/messages.en
echo "enrolmentRequired.heading = enrolmentRequired" >> ../conf/messages.en

echo "Migration EnrolmentRequired completed"
