#!/bin/bash

echo ""
echo "Applying migration RemoveDocument"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/removeDocument                        controllers.RemoveDocumentController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/removeDocument                        controllers.RemoveDocumentController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeRemoveDocument                  controllers.RemoveDocumentController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeRemoveDocument                  controllers.RemoveDocumentController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "removeDocument.title = removeDocument" >> ../conf/messages.en
echo "removeDocument.heading = removeDocument" >> ../conf/messages.en
echo "removeDocument.checkYourAnswersLabel = removeDocument" >> ../conf/messages.en
echo "removeDocument.error.required = Select yes if removeDocument" >> ../conf/messages.en
echo "removeDocument.change.hidden = RemoveDocument" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRemoveDocumentUserAnswersEntry: Arbitrary[(RemoveDocumentPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RemoveDocumentPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRemoveDocumentPage: Arbitrary[RemoveDocumentPage.type] =";\
    print "    Arbitrary(RemoveDocumentPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RemoveDocumentPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration RemoveDocument completed"
