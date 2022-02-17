#!/bin/bash

echo ""
echo "Applying migration AddAnyDocuments"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/addAnyDocuments                        controllers.AddAnyDocumentsController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/addAnyDocuments                        controllers.AddAnyDocumentsController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeAddAnyDocuments                  controllers.AddAnyDocumentsController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeAddAnyDocuments                  controllers.AddAnyDocumentsController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "addAnyDocuments.title = addAnyDocuments" >> ../conf/messages.en
echo "addAnyDocuments.heading = addAnyDocuments" >> ../conf/messages.en
echo "addAnyDocuments.checkYourAnswersLabel = addAnyDocuments" >> ../conf/messages.en
echo "addAnyDocuments.error.required = Select yes if addAnyDocuments" >> ../conf/messages.en
echo "addAnyDocuments.change.hidden = AddAnyDocuments" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddAnyDocumentsUserAnswersEntry: Arbitrary[(AddAnyDocumentsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AddAnyDocumentsPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddAnyDocumentsPage: Arbitrary[AddAnyDocumentsPage.type] =";\
    print "    Arbitrary(AddAnyDocumentsPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AddAnyDocumentsPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AddAnyDocuments completed"
