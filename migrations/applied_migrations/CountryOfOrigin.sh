#!/bin/bash

echo ""
echo "Applying migration CountryOfOrigin"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/countryOfOrigin                        controllers.CountryOfOriginController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/countryOfOrigin                        controllers.CountryOfOriginController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeCountryOfOrigin                  controllers.CountryOfOriginController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeCountryOfOrigin                  controllers.CountryOfOriginController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "countryOfOrigin.title = countryOfOrigin" >> ../conf/messages.en
echo "countryOfOrigin.heading = countryOfOrigin" >> ../conf/messages.en
echo "countryOfOrigin.checkYourAnswersLabel = countryOfOrigin" >> ../conf/messages.en
echo "countryOfOrigin.error.required = Enter countryOfOrigin" >> ../conf/messages.en
echo "countryOfOrigin.error.length = CountryOfOrigin must be 2 characters or less" >> ../conf/messages.en
echo "countryOfOrigin.change.hidden = CountryOfOrigin" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCountryOfOriginUserAnswersEntry: Arbitrary[(CountryOfOriginPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CountryOfOriginPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCountryOfOriginPage: Arbitrary[CountryOfOriginPage.type] =";\
    print "    Arbitrary(CountryOfOriginPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CountryOfOriginPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration CountryOfOrigin completed"
