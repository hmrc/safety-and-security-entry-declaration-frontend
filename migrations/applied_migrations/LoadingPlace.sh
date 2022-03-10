#!/bin/bash

echo ""
echo "Applying migration LoadingPlace"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/loadingPlace                        controllers.goods.LoadingPlaceController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/loadingPlace                        controllers.goods.LoadingPlaceController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeLoadingPlace                  controllers.goods.LoadingPlaceController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeLoadingPlace                  controllers.goods.LoadingPlaceController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "loadingPlace.title = Your loading places" >> ../conf/messages.en
echo "loadingPlace.heading = Your loading places" >> ../conf/messages.en
echo "loadingPlace.option1 = Option 1" >> ../conf/messages.en
echo "loadingPlace.option2 = Option 2" >> ../conf/messages.en
echo "loadingPlace.checkYourAnswersLabel = Your loading places" >> ../conf/messages.en
echo "loadingPlace.error.required = Select loadingPlace" >> ../conf/messages.en
echo "loadingPlace.change.hidden = LoadingPlace" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryLoadingPlaceUserAnswersEntry: Arbitrary[(LoadingPlacePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[LoadingPlacePage.type]";\
    print "        value <- arbitrary[LoadingPlace].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryLoadingPlacePage: Arbitrary[LoadingPlacePage.type] =";\
    print "    Arbitrary(LoadingPlacePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryLoadingPlace: Arbitrary[LoadingPlace] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(LoadingPlace.values.toSeq)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(LoadingPlacePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration LoadingPlace completed"
