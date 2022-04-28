#!/bin/bash

echo ""
echo "Applying migration EORIRequired"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /:lrn/eORIRequired                       controllers.package.EORIRequiredController.onPageLoad(lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "eORIRequired.title = eORIRequired" >> ../conf/messages.en
echo "eORIRequired.heading = eORIRequired" >> ../conf/messages.en

echo "Migration EORIRequired completed"
