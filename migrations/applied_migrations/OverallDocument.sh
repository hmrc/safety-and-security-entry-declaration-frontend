#!/bin/bash

echo ""
echo "Applying migration OverallDocument"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/overallDocument                        controllers.transport.OverallDocumentController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/overallDocument                        controllers.transport.OverallDocumentController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeOverallDocument                  controllers.transport.OverallDocumentController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeOverallDocument                  controllers.transport.OverallDocumentController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "overallDocument.title = overallDocument" >> ../conf/messages.en
echo "overallDocument.heading = overallDocument" >> ../conf/messages.en
echo "overallDocument.field1 = field1" >> ../conf/messages.en
echo "overallDocument.field2 = field2" >> ../conf/messages.en
echo "overallDocument.checkYourAnswersLabel = OverallDocument" >> ../conf/messages.en
echo "overallDocument.error.field1.required = Enter field1" >> ../conf/messages.en
echo "overallDocument.error.field2.required = Enter field2" >> ../conf/messages.en
echo "overallDocument.error.field1.length = field1 must be 100 characters or less" >> ../conf/messages.en
echo "overallDocument.error.field2.length = field2 must be 100 characters or less" >> ../conf/messages.en
echo "overallDocument.field1.change.hidden = field1" >> ../conf/messages.en
echo "overallDocument.field2.change.hidden = field2" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryOverallDocumentUserAnswersEntry: Arbitrary[(OverallDocumentPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[OverallDocumentPage.type]";\
    print "        value <- arbitrary[OverallDocument].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryOverallDocumentPage: Arbitrary[OverallDocumentPage.type] =";\
    print "    Arbitrary(OverallDocumentPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryOverallDocument: Arbitrary[OverallDocument] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        field1 <- arbitrary[String]";\
    print "        field2 <- arbitrary[String]";\
    print "      } yield OverallDocument(field1, field2)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(OverallDocumentPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration OverallDocument completed"
