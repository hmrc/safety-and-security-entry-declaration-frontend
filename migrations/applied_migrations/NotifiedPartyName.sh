#!/bin/bash

echo ""
echo "Applying migration NotifiedPartyName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/notifiedPartyName                        controllers.NotifiedPartyNameController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/notifiedPartyName                        controllers.NotifiedPartyNameController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeNotifiedPartyName                  controllers.NotifiedPartyNameController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeNotifiedPartyName                  controllers.NotifiedPartyNameController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "notifiedPartyName.title = notifiedPartyName" >> ../conf/messages.en
echo "notifiedPartyName.heading = notifiedPartyName" >> ../conf/messages.en
echo "notifiedPartyName.checkYourAnswersLabel = notifiedPartyName" >> ../conf/messages.en
echo "notifiedPartyName.error.required = Enter notifiedPartyName" >> ../conf/messages.en
echo "notifiedPartyName.error.length = NotifiedPartyName must be 100 characters or less" >> ../conf/messages.en
echo "notifiedPartyName.change.hidden = NotifiedPartyName" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNotifiedPartyNameUserAnswersEntry: Arbitrary[(NotifiedPartyNamePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[NotifiedPartyNamePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNotifiedPartyNamePage: Arbitrary[NotifiedPartyNamePage.type] =";\
    print "    Arbitrary(NotifiedPartyNamePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(NotifiedPartyNamePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration NotifiedPartyName completed"
