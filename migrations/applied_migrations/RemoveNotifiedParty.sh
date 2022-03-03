#!/bin/bash

echo ""
echo "Applying migration RemoveNotifiedParty"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/removeNotifiedParty                        controllers.consignees.RemoveNotifiedPartyController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/removeNotifiedParty                        controllers.consignees.RemoveNotifiedPartyController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeRemoveNotifiedParty                  controllers.consignees.RemoveNotifiedPartyController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeRemoveNotifiedParty                  controllers.consignees.RemoveNotifiedPartyController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "removeNotifiedParty.title = removeNotifiedParty" >> ../conf/messages.en
echo "removeNotifiedParty.heading = removeNotifiedParty" >> ../conf/messages.en
echo "removeNotifiedParty.checkYourAnswersLabel = removeNotifiedParty" >> ../conf/messages.en
echo "removeNotifiedParty.error.required = Select yes if removeNotifiedParty" >> ../conf/messages.en
echo "removeNotifiedParty.change.hidden = RemoveNotifiedParty" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRemoveNotifiedPartyUserAnswersEntry: Arbitrary[(RemoveNotifiedPartyPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RemoveNotifiedPartyPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRemoveNotifiedPartyPage: Arbitrary[RemoveNotifiedPartyPage.type] =";\
    print "    Arbitrary(RemoveNotifiedPartyPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RemoveNotifiedPartyPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration RemoveNotifiedParty completed"
