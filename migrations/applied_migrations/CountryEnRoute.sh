#!/bin/bash

echo ""
echo "Applying migration CountryEnRoute"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/countryEnRoute                        controllers.CountryEnRouteController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/countryEnRoute                        controllers.CountryEnRouteController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeCountryEnRoute                  controllers.CountryEnRouteController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeCountryEnRoute                  controllers.CountryEnRouteController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "countryEnRoute.title = countryEnRoute" >> ../conf/messages.en
echo "countryEnRoute.heading = countryEnRoute" >> ../conf/messages.en
echo "countryEnRoute.checkYourAnswersLabel = countryEnRoute" >> ../conf/messages.en
echo "countryEnRoute.error.required = Enter countryEnRoute" >> ../conf/messages.en
echo "countryEnRoute.error.length = CountryEnRoute must be 2 characters or less" >> ../conf/messages.en
echo "countryEnRoute.change.hidden = CountryEnRoute" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCountryEnRouteUserAnswersEntry: Arbitrary[(CountryEnRoutePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CountryEnRoutePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCountryEnRoutePage: Arbitrary[CountryEnRoutePage.type] =";\
    print "    Arbitrary(CountryEnRoutePage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CountryEnRoutePage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration CountryEnRoute completed"
