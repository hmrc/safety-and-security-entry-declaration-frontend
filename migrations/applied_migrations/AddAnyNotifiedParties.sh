#!/bin/bash

echo ""
echo "Applying migration AddAnyNotifiedParties"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/addAnyNotifiedParties                        controllers.consignees.AddAnyNotifiedPartiesController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/addAnyNotifiedParties                        controllers.consignees.AddAnyNotifiedPartiesController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeAddAnyNotifiedParties                  controllers.consignees.AddAnyNotifiedPartiesController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeAddAnyNotifiedParties                  controllers.consignees.AddAnyNotifiedPartiesController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "addAnyNotifiedParties.title = addAnyNotifiedParties" >> ../conf/messages.en
echo "addAnyNotifiedParties.heading = addAnyNotifiedParties" >> ../conf/messages.en
echo "addAnyNotifiedParties.checkYourAnswersLabel = addAnyNotifiedParties" >> ../conf/messages.en
echo "addAnyNotifiedParties.error.required = Select yes if addAnyNotifiedParties" >> ../conf/messages.en
echo "addAnyNotifiedParties.change.hidden = AddAnyNotifiedParties" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddAnyNotifiedPartiesUserAnswersEntry: Arbitrary[(AddAnyNotifiedPartiesPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AddAnyNotifiedPartiesPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddAnyNotifiedPartiesPage: Arbitrary[AddAnyNotifiedPartiesPage.type] =";\
    print "    Arbitrary(AddAnyNotifiedPartiesPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AddAnyNotifiedPartiesPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AddAnyNotifiedParties completed"
