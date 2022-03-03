#!/bin/bash

echo ""
echo "Applying migration AddPlaceOfLoading"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/addPlaceOfLoading                        controllers.routedetails.AddPlaceOfLoadingController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/addPlaceOfLoading                        controllers.routedetails.AddPlaceOfLoadingController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeAddPlaceOfLoading                  controllers.routedetails.AddPlaceOfLoadingController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeAddPlaceOfLoading                  controllers.routedetails.AddPlaceOfLoadingController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "addPlaceOfLoading.title = addPlaceOfLoading" >> ../conf/messages.en
echo "addPlaceOfLoading.heading = addPlaceOfLoading" >> ../conf/messages.en
echo "addPlaceOfLoading.checkYourAnswersLabel = addPlaceOfLoading" >> ../conf/messages.en
echo "addPlaceOfLoading.error.required = Select yes if addPlaceOfLoading" >> ../conf/messages.en
echo "addPlaceOfLoading.change.hidden = AddPlaceOfLoading" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddPlaceOfLoadingUserAnswersEntry: Arbitrary[(AddPlaceOfLoadingPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AddPlaceOfLoadingPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddPlaceOfLoadingPage: Arbitrary[AddPlaceOfLoadingPage.type] =";\
    print "    Arbitrary(AddPlaceOfLoadingPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AddPlaceOfLoadingPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AddPlaceOfLoading completed"
