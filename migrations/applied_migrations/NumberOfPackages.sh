#!/bin/bash

echo ""
echo "Applying migration NumberOfPackages"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/numberOfPackages                  controllers.NumberOfPackagesController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/numberOfPackages                  controllers.NumberOfPackagesController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeNumberOfPackages                        controllers.NumberOfPackagesController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeNumberOfPackages                        controllers.NumberOfPackagesController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "numberOfPackages.title = NumberOfPackages" >> ../conf/messages.en
echo "numberOfPackages.heading = NumberOfPackages" >> ../conf/messages.en
echo "numberOfPackages.checkYourAnswersLabel = NumberOfPackages" >> ../conf/messages.en
echo "numberOfPackages.error.nonNumeric = Enter your numberOfPackages using numbers" >> ../conf/messages.en
echo "numberOfPackages.error.required = Enter your numberOfPackages" >> ../conf/messages.en
echo "numberOfPackages.error.wholeNumber = Enter your numberOfPackages using whole numbers" >> ../conf/messages.en
echo "numberOfPackages.error.outOfRange = NumberOfPackages must be between {0} and {1}" >> ../conf/messages.en
echo "numberOfPackages.change.hidden = NumberOfPackages" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNumberOfPackagesUserAnswersEntry: Arbitrary[(NumberOfPackagesPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[NumberOfPackagesPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNumberOfPackagesPage: Arbitrary[NumberOfPackagesPage.type] =";\
    print "    Arbitrary(NumberOfPackagesPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(NumberOfPackagesPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration NumberOfPackages completed"
