#!/bin/bash

echo ""
echo "Applying migration RemoveCountryEnRoute"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/removeCountryEnRoute                        controllers.RemoveCountryEnRouteController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/removeCountryEnRoute                        controllers.RemoveCountryEnRouteController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeRemoveCountryEnRoute                  controllers.RemoveCountryEnRouteController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeRemoveCountryEnRoute                  controllers.RemoveCountryEnRouteController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "removeCountryEnRoute.title = removeCountryEnRoute" >> ../conf/messages.en
echo "removeCountryEnRoute.heading = removeCountryEnRoute" >> ../conf/messages.en
echo "removeCountryEnRoute.checkYourAnswersLabel = removeCountryEnRoute" >> ../conf/messages.en
echo "removeCountryEnRoute.error.required = Select yes if removeCountryEnRoute" >> ../conf/messages.en
echo "removeCountryEnRoute.change.hidden = RemoveCountryEnRoute" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRemoveCountryEnRouteUserAnswersEntry: Arbitrary[(RemoveCountryEnRoutePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RemoveCountryEnRoutePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRemoveCountryEnRoutePage: Arbitrary[RemoveCountryEnRoutePage.type] =";\
    print "    Arbitrary(RemoveCountryEnRoutePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RemoveCountryEnRoutePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration RemoveCountryEnRoute completed"
