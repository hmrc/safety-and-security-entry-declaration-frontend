#!/bin/bash

echo ""
echo "Applying migration RemovePlaceOfUnloading"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/removePlaceOfUnloading                        controllers.routedetails.RemovePlaceOfUnloadingController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/removePlaceOfUnloading                        controllers.routedetails.RemovePlaceOfUnloadingController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeRemovePlaceOfUnloading                  controllers.routedetails.RemovePlaceOfUnloadingController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeRemovePlaceOfUnloading                  controllers.routedetails.RemovePlaceOfUnloadingController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "removePlaceOfUnloading.title = removePlaceOfUnloading" >> ../conf/messages.en
echo "removePlaceOfUnloading.heading = removePlaceOfUnloading" >> ../conf/messages.en
echo "removePlaceOfUnloading.checkYourAnswersLabel = removePlaceOfUnloading" >> ../conf/messages.en
echo "removePlaceOfUnloading.error.required = Select yes if removePlaceOfUnloading" >> ../conf/messages.en
echo "removePlaceOfUnloading.change.hidden = RemovePlaceOfUnloading" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRemovePlaceOfUnloadingUserAnswersEntry: Arbitrary[(RemovePlaceOfUnloadingPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RemovePlaceOfUnloadingPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRemovePlaceOfUnloadingPage: Arbitrary[RemovePlaceOfUnloadingPage.type] =";\
    print "    Arbitrary(RemovePlaceOfUnloadingPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RemovePlaceOfUnloadingPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration RemovePlaceOfUnloading completed"
