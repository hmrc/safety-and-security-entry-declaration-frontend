#!/bin/bash

echo ""
echo "Applying migration RemoveOverallDocument"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/removeOverallDocument                        controllers.transport.RemoveOverallDocumentController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/removeOverallDocument                        controllers.transport.RemoveOverallDocumentController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeRemoveOverallDocument                  controllers.transport.RemoveOverallDocumentController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeRemoveOverallDocument                  controllers.transport.RemoveOverallDocumentController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "removeOverallDocument.title = removeOverallDocument" >> ../conf/messages.en
echo "removeOverallDocument.heading = removeOverallDocument" >> ../conf/messages.en
echo "removeOverallDocument.checkYourAnswersLabel = removeOverallDocument" >> ../conf/messages.en
echo "removeOverallDocument.error.required = Select yes if removeOverallDocument" >> ../conf/messages.en
echo "removeOverallDocument.change.hidden = RemoveOverallDocument" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRemoveOverallDocumentUserAnswersEntry: Arbitrary[(RemoveOverallDocumentPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RemoveOverallDocumentPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRemoveOverallDocumentPage: Arbitrary[RemoveOverallDocumentPage.type] =";\
    print "    Arbitrary(RemoveOverallDocumentPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RemoveOverallDocumentPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration RemoveOverallDocument completed"
