#!/bin/bash

echo ""
echo "Applying migration PlaceOfLoading"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/placeOfLoading                        controllers.routedetails.PlaceOfLoadingController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/placeOfLoading                        controllers.routedetails.PlaceOfLoadingController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changePlaceOfLoading                  controllers.routedetails.PlaceOfLoadingController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changePlaceOfLoading                  controllers.routedetails.PlaceOfLoadingController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "placeOfLoading.title = placeOfLoading" >> ../conf/messages.en
echo "placeOfLoading.heading = placeOfLoading" >> ../conf/messages.en
echo "placeOfLoading.country = country" >> ../conf/messages.en
echo "placeOfLoading.place = place" >> ../conf/messages.en
echo "placeOfLoading.checkYourAnswersLabel = PlaceOfLoading" >> ../conf/messages.en
echo "placeOfLoading.error.country.required = Enter country" >> ../conf/messages.en
echo "placeOfLoading.error.place.required = Enter place" >> ../conf/messages.en
echo "placeOfLoading.error.country.length = country must be 2 characters or less" >> ../conf/messages.en
echo "placeOfLoading.error.place.length = place must be 33 characters or less" >> ../conf/messages.en
echo "placeOfLoading.country.change.hidden = country" >> ../conf/messages.en
echo "placeOfLoading.place.change.hidden = place" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPlaceOfLoadingUserAnswersEntry: Arbitrary[(PlaceOfLoadingPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PlaceOfLoadingPage.type]";\
    print "        value <- arbitrary[PlaceOfLoading].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPlaceOfLoadingPage: Arbitrary[PlaceOfLoadingPage.type] =";\
    print "    Arbitrary(PlaceOfLoadingPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPlaceOfLoading: Arbitrary[PlaceOfLoading] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        country <- arbitrary[String]";\
    print "        place <- arbitrary[String]";\
    print "      } yield PlaceOfLoading(country, place)";\
    print "    }";\
    next }1' ../test-utils/generators/ModelGenerators.scala > tmp && mv tmp ../test-utils/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PlaceOfLoadingPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration PlaceOfLoading completed"
