#!/bin/bash

echo ""
echo "Applying migration NotifiedPartyEORI"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/notifiedPartyEORI                        controllers.NotifiedPartyEORIController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/notifiedPartyEORI                        controllers.NotifiedPartyEORIController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeNotifiedPartyEORI                  controllers.NotifiedPartyEORIController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeNotifiedPartyEORI                  controllers.NotifiedPartyEORIController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "notifiedPartyEORI.title = notifiedPartyEORI" >> ../conf/messages.en
echo "notifiedPartyEORI.heading = notifiedPartyEORI" >> ../conf/messages.en
echo "notifiedPartyEORI.checkYourAnswersLabel = notifiedPartyEORI" >> ../conf/messages.en
echo "notifiedPartyEORI.error.required = Enter notifiedPartyEORI" >> ../conf/messages.en
echo "notifiedPartyEORI.error.length = NotifiedPartyEORI must be 35 characters or less" >> ../conf/messages.en
echo "notifiedPartyEORI.change.hidden = NotifiedPartyEORI" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNotifiedPartyEORIUserAnswersEntry: Arbitrary[(NotifiedPartyEORIPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[NotifiedPartyEORIPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNotifiedPartyEORIPage: Arbitrary[NotifiedPartyEORIPage.type] =";\
    print "    Arbitrary(NotifiedPartyEORIPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(NotifiedPartyEORIPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration NotifiedPartyEORI completed"
