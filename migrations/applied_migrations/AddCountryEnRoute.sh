#!/bin/bash

echo ""
echo "Applying migration AddCountryEnRoute"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/addCountryEnRoute                        controllers.AddCountryEnRouteController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/addCountryEnRoute                        controllers.AddCountryEnRouteController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeAddCountryEnRoute                  controllers.AddCountryEnRouteController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeAddCountryEnRoute                  controllers.AddCountryEnRouteController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "addCountryEnRoute.title = addCountryEnRoute" >> ../conf/messages.en
echo "addCountryEnRoute.heading = addCountryEnRoute" >> ../conf/messages.en
echo "addCountryEnRoute.checkYourAnswersLabel = addCountryEnRoute" >> ../conf/messages.en
echo "addCountryEnRoute.error.required = Select yes if addCountryEnRoute" >> ../conf/messages.en
echo "addCountryEnRoute.change.hidden = AddCountryEnRoute" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddCountryEnRouteUserAnswersEntry: Arbitrary[(AddCountryEnRoutePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AddCountryEnRoutePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddCountryEnRoutePage: Arbitrary[AddCountryEnRoutePage.type] =";\
    print "    Arbitrary(AddCountryEnRoutePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AddCountryEnRoutePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration AddCountryEnRoute completed"
