#!/bin/bash

echo ""
echo "Applying migration RemovePlaceOfLoading"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/removePlaceOfLoading                        controllers.routedetails.RemovePlaceOfLoadingController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/removePlaceOfLoading                        controllers.routedetails.RemovePlaceOfLoadingController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeRemovePlaceOfLoading                  controllers.routedetails.RemovePlaceOfLoadingController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeRemovePlaceOfLoading                  controllers.routedetails.RemovePlaceOfLoadingController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "removePlaceOfLoading.title = removePlaceOfLoading" >> ../conf/messages.en
echo "removePlaceOfLoading.heading = removePlaceOfLoading" >> ../conf/messages.en
echo "removePlaceOfLoading.checkYourAnswersLabel = removePlaceOfLoading" >> ../conf/messages.en
echo "removePlaceOfLoading.error.required = Select yes if removePlaceOfLoading" >> ../conf/messages.en
echo "removePlaceOfLoading.change.hidden = RemovePlaceOfLoading" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRemovePlaceOfLoadingUserAnswersEntry: Arbitrary[(RemovePlaceOfLoadingPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RemovePlaceOfLoadingPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRemovePlaceOfLoadingPage: Arbitrary[RemovePlaceOfLoadingPage.type] =";\
    print "    Arbitrary(RemovePlaceOfLoadingPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RemovePlaceOfLoadingPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration RemovePlaceOfLoading completed"
