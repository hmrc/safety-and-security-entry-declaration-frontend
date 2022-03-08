#!/bin/bash

echo ""
echo "Applying migration AnyOverallDocuments"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/anyOverallDocuments                        controllers.transport.AnyOverallDocumentsController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/anyOverallDocuments                        controllers.transport.AnyOverallDocumentsController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeAnyOverallDocuments                  controllers.transport.AnyOverallDocumentsController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeAnyOverallDocuments                  controllers.transport.AnyOverallDocumentsController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "anyOverallDocuments.title = anyOverallDocuments" >> ../conf/messages.en
echo "anyOverallDocuments.heading = anyOverallDocuments" >> ../conf/messages.en
echo "anyOverallDocuments.checkYourAnswersLabel = anyOverallDocuments" >> ../conf/messages.en
echo "anyOverallDocuments.error.required = Select yes if anyOverallDocuments" >> ../conf/messages.en
echo "anyOverallDocuments.change.hidden = AnyOverallDocuments" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAnyOverallDocumentsUserAnswersEntry: Arbitrary[(AnyOverallDocumentsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AnyOverallDocumentsPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAnyOverallDocumentsPage: Arbitrary[AnyOverallDocumentsPage.type] =";\
    print "    Arbitrary(AnyOverallDocumentsPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AnyOverallDocumentsPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AnyOverallDocuments completed"
