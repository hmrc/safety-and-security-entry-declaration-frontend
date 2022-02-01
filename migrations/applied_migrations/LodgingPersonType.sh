#!/bin/bash

echo ""
echo "Applying migration LodgingPersonType"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /lodgingPersonType                        controllers.LodgingPersonTypeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /lodgingPersonType                        controllers.LodgingPersonTypeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeLodgingPersonType                  controllers.LodgingPersonTypeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeLodgingPersonType                  controllers.LodgingPersonTypeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "lodgingPersonType.title = lodgingPersonType" >> ../conf/messages.en
echo "lodgingPersonType.heading = lodgingPersonType" >> ../conf/messages.en
echo "lodgingPersonType.carrier = Carrier" >> ../conf/messages.en
echo "lodgingPersonType.representative = Representative" >> ../conf/messages.en
echo "lodgingPersonType.checkYourAnswersLabel = lodgingPersonType" >> ../conf/messages.en
echo "lodgingPersonType.error.required = Select lodgingPersonType" >> ../conf/messages.en
echo "lodgingPersonType.change.hidden = LodgingPersonType" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryLodgingPersonTypeUserAnswersEntry: Arbitrary[(LodgingPersonTypePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[LodgingPersonTypePage.type]";\
    print "        value <- arbitrary[LodgingPersonType].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryLodgingPersonTypePage: Arbitrary[LodgingPersonTypePage.type] =";\
    print "    Arbitrary(LodgingPersonTypePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryLodgingPersonType: Arbitrary[LodgingPersonType] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(LodgingPersonType.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(LodgingPersonTypePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Migration LodgingPersonType completed"
