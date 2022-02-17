#!/bin/bash

echo ""
echo "Applying migration ConsigneeKnown"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/consigneeKnown                        controllers.ConsigneeKnownController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/consigneeKnown                        controllers.ConsigneeKnownController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeConsigneeKnown                  controllers.ConsigneeKnownController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeConsigneeKnown                  controllers.ConsigneeKnownController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "consigneeKnown.title = consigneeKnown" >> ../conf/messages.en
echo "consigneeKnown.heading = consigneeKnown" >> ../conf/messages.en
echo "consigneeKnown.checkYourAnswersLabel = consigneeKnown" >> ../conf/messages.en
echo "consigneeKnown.error.required = Select yes if consigneeKnown" >> ../conf/messages.en
echo "consigneeKnown.change.hidden = ConsigneeKnown" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsigneeKnownUserAnswersEntry: Arbitrary[(ConsigneeKnownPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ConsigneeKnownPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryConsigneeKnownPage: Arbitrary[ConsigneeKnownPage.type] =";\
    print "    Arbitrary(ConsigneeKnownPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ConsigneeKnownPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration ConsigneeKnown completed"
