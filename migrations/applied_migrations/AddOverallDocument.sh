#!/bin/bash

echo ""
echo "Applying migration AddOverallDocument"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/addOverallDocument                        controllers.transport.AddOverallDocumentController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/addOverallDocument                        controllers.transport.AddOverallDocumentController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeAddOverallDocument                  controllers.transport.AddOverallDocumentController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeAddOverallDocument                  controllers.transport.AddOverallDocumentController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "addOverallDocument.title = addOverallDocument" >> ../conf/messages.en
echo "addOverallDocument.heading = addOverallDocument" >> ../conf/messages.en
echo "addOverallDocument.checkYourAnswersLabel = addOverallDocument" >> ../conf/messages.en
echo "addOverallDocument.error.required = Select yes if addOverallDocument" >> ../conf/messages.en
echo "addOverallDocument.change.hidden = AddOverallDocument" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddOverallDocumentUserAnswersEntry: Arbitrary[(AddOverallDocumentPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AddOverallDocumentPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddOverallDocumentPage: Arbitrary[AddOverallDocumentPage.type] =";\
    print "    Arbitrary(AddOverallDocumentPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AddOverallDocumentPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AddOverallDocument completed"
