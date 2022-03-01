#!/bin/bash

echo ""
echo "Applying migration AddNotifiedParty"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/addNotifiedParty                        controllers.consignees.AddNotifiedPartyController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/addNotifiedParty                        controllers.consignees.AddNotifiedPartyController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeAddNotifiedParty                  controllers.consignees.AddNotifiedPartyController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeAddNotifiedParty                  controllers.consignees.AddNotifiedPartyController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "addNotifiedParty.title = addNotifiedParty" >> ../conf/messages.en
echo "addNotifiedParty.heading = addNotifiedParty" >> ../conf/messages.en
echo "addNotifiedParty.checkYourAnswersLabel = addNotifiedParty" >> ../conf/messages.en
echo "addNotifiedParty.error.required = Select yes if addNotifiedParty" >> ../conf/messages.en
echo "addNotifiedParty.change.hidden = AddNotifiedParty" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddNotifiedPartyUserAnswersEntry: Arbitrary[(AddNotifiedPartyPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AddNotifiedPartyPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddNotifiedPartyPage: Arbitrary[AddNotifiedPartyPage.type] =";\
    print "    Arbitrary(AddNotifiedPartyPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AddNotifiedPartyPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AddNotifiedParty completed"
