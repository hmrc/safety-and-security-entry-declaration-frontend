#!/bin/bash

echo ""
echo "Applying migration AddPlaceOfUnloading"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/addPlaceOfUnloading                        controllers.routedetails.AddPlaceOfUnloadingController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/addPlaceOfUnloading                        controllers.routedetails.AddPlaceOfUnloadingController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeAddPlaceOfUnloading                  controllers.routedetails.AddPlaceOfUnloadingController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeAddPlaceOfUnloading                  controllers.routedetails.AddPlaceOfUnloadingController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "addPlaceOfUnloading.title = addPlaceOfUnloading" >> ../conf/messages.en
echo "addPlaceOfUnloading.heading = addPlaceOfUnloading" >> ../conf/messages.en
echo "addPlaceOfUnloading.checkYourAnswersLabel = addPlaceOfUnloading" >> ../conf/messages.en
echo "addPlaceOfUnloading.error.required = Select yes if addPlaceOfUnloading" >> ../conf/messages.en
echo "addPlaceOfUnloading.change.hidden = AddPlaceOfUnloading" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddPlaceOfUnloadingUserAnswersEntry: Arbitrary[(AddPlaceOfUnloadingPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AddPlaceOfUnloadingPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddPlaceOfUnloadingPage: Arbitrary[AddPlaceOfUnloadingPage.type] =";\
    print "    Arbitrary(AddPlaceOfUnloadingPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AddPlaceOfUnloadingPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AddPlaceOfUnloading completed"
