#!/bin/bash

echo ""
echo "Applying migration NationalityOfTransport"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/nationalityOfTransport                        controllers.transport.NationalityOfTransportController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/nationalityOfTransport                        controllers.transport.NationalityOfTransportController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeNationalityOfTransport                  controllers.transport.NationalityOfTransportController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeNationalityOfTransport                  controllers.transport.NationalityOfTransportController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "nationalityOfTransport.title = nationalityOfTransport" >> ../conf/messages.en
echo "nationalityOfTransport.heading = nationalityOfTransport" >> ../conf/messages.en
echo "nationalityOfTransport.checkYourAnswersLabel = nationalityOfTransport" >> ../conf/messages.en
echo "nationalityOfTransport.error.required = Enter nationalityOfTransport" >> ../conf/messages.en
echo "nationalityOfTransport.error.length = NationalityOfTransport must be 100 characters or less" >> ../conf/messages.en
echo "nationalityOfTransport.change.hidden = NationalityOfTransport" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNationalityOfTransportUserAnswersEntry: Arbitrary[(NationalityOfTransportPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[NationalityOfTransportPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNationalityOfTransportPage: Arbitrary[NationalityOfTransportPage.type] =";\
    print "    Arbitrary(NationalityOfTransportPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(NationalityOfTransportPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration NationalityOfTransport completed"
