#!/bin/bash

echo ""
echo "Applying migration UnloadingPlace"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/unloadingPlace                        controllers.goods.UnloadingPlaceController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/unloadingPlace                        controllers.goods.UnloadingPlaceController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeUnloadingPlace                  controllers.goods.UnloadingPlaceController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeUnloadingPlace                  controllers.goods.UnloadingPlaceController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "unloadingPlace.title = Your unloading places" >> ../conf/messages.en
echo "unloadingPlace.heading = Your unloading places" >> ../conf/messages.en
echo "unloadingPlace.option1 = Option 1" >> ../conf/messages.en
echo "unloadingPlace.option2 = Option 2" >> ../conf/messages.en
echo "unloadingPlace.checkYourAnswersLabel = Your unloading places" >> ../conf/messages.en
echo "unloadingPlace.error.required = Select unloadingPlace" >> ../conf/messages.en
echo "unloadingPlace.change.hidden = UnloadingPlace" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryUnloadingPlaceUserAnswersEntry: Arbitrary[(UnloadingPlacePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[UnloadingPlacePage.type]";\
    print "        value <- arbitrary[UnloadingPlace].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryUnloadingPlacePage: Arbitrary[UnloadingPlacePage.type] =";\
    print "    Arbitrary(UnloadingPlacePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryUnloadingPlace: Arbitrary[UnloadingPlace] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(UnloadingPlace.values.toSeq)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(UnloadingPlacePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration UnloadingPlace completed"
