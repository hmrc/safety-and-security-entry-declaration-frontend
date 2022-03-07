#!/bin/bash

echo ""
echo "Applying migration PlaceOfUnloading"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/placeOfUnloading                        controllers.routedetails.PlaceOfUnloadingController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/placeOfUnloading                        controllers.routedetails.PlaceOfUnloadingController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changePlaceOfUnloading                  controllers.routedetails.PlaceOfUnloadingController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changePlaceOfUnloading                  controllers.routedetails.PlaceOfUnloadingController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "placeOfUnloading.title = placeOfUnloading" >> ../conf/messages.en
echo "placeOfUnloading.heading = placeOfUnloading" >> ../conf/messages.en
echo "placeOfUnloading.country = country" >> ../conf/messages.en
echo "placeOfUnloading.place = place" >> ../conf/messages.en
echo "placeOfUnloading.checkYourAnswersLabel = PlaceOfUnloading" >> ../conf/messages.en
echo "placeOfUnloading.error.country.required = Enter country" >> ../conf/messages.en
echo "placeOfUnloading.error.place.required = Enter place" >> ../conf/messages.en
echo "placeOfUnloading.error.country.length = country must be 2 characters or less" >> ../conf/messages.en
echo "placeOfUnloading.error.place.length = place must be 32 characters or less" >> ../conf/messages.en
echo "placeOfUnloading.country.change.hidden = country" >> ../conf/messages.en
echo "placeOfUnloading.place.change.hidden = place" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPlaceOfUnloadingUserAnswersEntry: Arbitrary[(PlaceOfUnloadingPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PlaceOfUnloadingPage.type]";\
    print "        value <- arbitrary[PlaceOfUnloading].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPlaceOfUnloadingPage: Arbitrary[PlaceOfUnloadingPage.type] =";\
    print "    Arbitrary(PlaceOfUnloadingPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPlaceOfUnloading: Arbitrary[PlaceOfUnloading] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        country <- arbitrary[String]";\
    print "        place <- arbitrary[String]";\
    print "      } yield PlaceOfUnloading(country, place)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PlaceOfUnloadingPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration PlaceOfUnloading completed"
