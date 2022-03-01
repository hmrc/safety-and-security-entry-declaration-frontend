#!/bin/bash

echo ""
echo "Applying migration CarrierEORI"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /:lrn/carrierEORI                        controllers.CarrierEORIController.onPageLoad(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/carrierEORI                        controllers.CarrierEORIController.onSubmit(mode: Mode = NormalMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "GET        /:lrn/changeCarrierEORI                  controllers.CarrierEORIController.onPageLoad(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes
echo "POST       /:lrn/changeCarrierEORI                  controllers.CarrierEORIController.onSubmit(mode: Mode = CheckMode, lrn: LocalReferenceNumber)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "carrierEORI.title = carrierEORI" >> ../conf/messages.en
echo "carrierEORI.heading = carrierEORI" >> ../conf/messages.en
echo "carrierEORI.checkYourAnswersLabel = carrierEORI" >> ../conf/messages.en
echo "carrierEORI.error.required = Enter carrierEORI" >> ../conf/messages.en
echo "carrierEORI.error.length = CarrierEORI must be 100 characters or less" >> ../conf/messages.en
echo "carrierEORI.change.hidden = CarrierEORI" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCarrierEORIUserAnswersEntry: Arbitrary[(CarrierEORIPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CarrierEORIPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test-utils/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test-utils/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCarrierEORIPage: Arbitrary[CarrierEORIPage.type] =";\
    print "    Arbitrary(CarrierEORIPage)";\
    next }1' ../test-utils/generators/PageGenerators.scala > tmp && mv tmp ../test-utils/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CarrierEORIPage.type, JsValue)] ::";\
    next }1' ../test-utils/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test-utils/generators/UserAnswersGenerator.scala

echo "Migration CarrierEORI completed"
